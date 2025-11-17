using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.ServiceModel;
using ec.edu.monster.db;
using ec.edu.monster.model;
using ws_banquito_dotnet_gr08;
using ws_comer_dotnet_gr08;
using BanquitoService = ws_banquito_dotnet_gr08.IService1;

namespace ec.edu.monster.service
{
    public class VentaService
    {
        private static readonly decimal DESCUENTO_EFECTIVO = 0.33m;
        private const string BANQUITO_SOAP_URL = "http://localhost:8733/Design_Time_Addresses/ws_banquito_dotnet_gr08/Service1/";

        public VentaResponse ProcesarVenta(VentaRequest request)
        {
            var response = new VentaResponse();

            using (var connection = DatabaseConnection.GetConnection())
            using (var transaction = connection.BeginTransaction())
            {
                try
                {
                    if (!string.Equals(request.FormaPago, "EFECTIVO", StringComparison.OrdinalIgnoreCase) &&
                        !string.Equals(request.FormaPago, "CREDITO_DIRECTO", StringComparison.OrdinalIgnoreCase))
                    {
                        response.VentaExitosa = false;
                        response.Mensaje = "Forma de pago no válida. Use EFECTIVO o CREDITO_DIRECTO";
                        return response;
                    }

                    var electrodomesticos = new List<Electrodomestico>();
                    decimal subtotal = 0m;

                    foreach (var detalle in request.Detalles)
                    {
                        var electrodomestico = BuscarElectrodomestico(connection, transaction, int.Parse(detalle.IdElectrodomestico));
                        if (electrodomestico == null)
                        {
                            transaction.Rollback();
                            response.VentaExitosa = false;
                            response.Mensaje = "Electrodoméstico con ID " + detalle.IdElectrodomestico + " no encontrado";
                            return response;
                        }

                        electrodomesticos.Add(electrodomestico);
                        var subtotalLinea = electrodomestico.PrecioVenta * decimal.Parse(detalle.Cantidad);
                        subtotal += subtotalLinea;
                    }

                    decimal descuento = 0m;
                    decimal total = subtotal;

                    if (string.Equals(request.FormaPago, "EFECTIVO", StringComparison.OrdinalIgnoreCase))
                    {
                        descuento = Math.Round(subtotal * DESCUENTO_EFECTIVO, 2, MidpointRounding.AwayFromZero);
                        total = subtotal - descuento;
                    }

                    if (string.Equals(request.FormaPago, "CREDITO_DIRECTO", StringComparison.OrdinalIgnoreCase))
                    {
                        var plazo = request.PlazoMeses ?? 12;
                        var creditoAprobado = VerificarCreditoBanquito(request.CedulaCliente, total, plazo);
                        if (!creditoAprobado)
                        {
                            transaction.Rollback();
                            response.VentaExitosa = false;
                            response.Mensaje = "Crédito no aprobado por el banco Banquito";
                            return response;
                        }
                    }

                    CrearOActualizarCliente(connection, transaction, request.CedulaCliente, request.NombreCliente);

                    var idFactura = CrearFactura(connection, transaction, request, subtotal, descuento, total);
                    if (!idFactura.HasValue)
                    {
                        throw new Exception("No se pudo obtener el identificador de la factura creada");
                    }

                    var detallesResponse = new List<DetalleVentaResponse>();
                    for (int i = 0; i < request.Detalles.Count; i++)
                    {
                        var detalleRequest = request.Detalles[i];
                        var electrodomestico = electrodomesticos[i];
                        var subtotalLinea = electrodomestico.PrecioVenta * decimal.Parse(detalleRequest.Cantidad);

                        CrearDetalleFactura(connection, transaction, idFactura.Value, detalleRequest.IdElectrodomestico, detalleRequest.Cantidad, electrodomestico.PrecioVenta, subtotalLinea);

                        detallesResponse.Add(new DetalleVentaResponse
                        {
                            IdElectrodomestico = detalleRequest.IdElectrodomestico,
                            NombreElectrodomestico = electrodomestico.Nombre,
                            Cantidad = detalleRequest.Cantidad,
                            PrecioUnitario = electrodomestico.PrecioVenta,
                            SubtotalLinea = subtotalLinea
                        });
                    }

                    transaction.Commit();

                    response.VentaExitosa = true;
                    response.Mensaje = "Venta procesada exitosamente";
                    response.IdFactura = idFactura;
                    response.Subtotal = subtotal;
                    response.Descuento = descuento;
                    response.Total = total;
                    response.FormaPago = request.FormaPago;
                    response.EstadoFactura = string.Equals(request.FormaPago, "EFECTIVO", StringComparison.OrdinalIgnoreCase)
                        ? "PAGADA"
                        : "APROBADA";
                    response.Detalles = detallesResponse;
                }
                catch (Exception ex)
                {
                    transaction.Rollback();
                    response.VentaExitosa = false;
                    response.Mensaje = "Error en el proceso de venta: " + ex.Message;
                }
            }

            return response;
        }

        private Electrodomestico BuscarElectrodomestico(SqlConnection connection, SqlTransaction transaction, int idElectrodomestico)
        {
            using (var cmd = connection.CreateCommand())
            {
                cmd.Transaction = transaction;
                cmd.CommandText = "SELECT * FROM ELECTRODOMESTICO WHERE idElectrodomestico = @id";
                cmd.Parameters.AddWithValue("@id", idElectrodomestico);

                using (var reader = cmd.ExecuteReader())
                {
                    if (reader.Read())
                    {
                        return new Electrodomestico
                        {
                            IdElectrodomestico = Convert.ToInt32(reader["idElectrodomestico"]),
                            Nombre = Convert.ToString(reader["nombre"]),
                            PrecioVenta = Convert.ToDecimal(reader["precioVenta"])
                        };
                    }
                }
            }

            return null;
        }

        private bool VerificarCreditoBanquito(string cedula, decimal monto, int plazoMeses)
        {
            var binding = new BasicHttpBinding();
            var endpoint = new EndpointAddress(BANQUITO_SOAP_URL);
            var factory = new ChannelFactory<BanquitoService>(binding, endpoint);
            BanquitoService client = factory.CreateChannel();

            try
            {
                var request = new EvaluacionCreditoRequest
                {
                    Cedula = cedula,
                    MontoElectrodomestico = monto,
                    PlazoMeses = plazoMeses
                };

                var response = client.EvaluarCredito(request);
                var channel = (IClientChannel)client;
                if (channel.State == CommunicationState.Faulted)
                {
                    channel.Abort();
                    factory.Abort();
                }
                else
                {
                    channel.Close();
                    factory.Close();
                }

                return response != null && response.CreditoAprobado == true;
            }
            catch
            {
                try
                {
                    ((IClientChannel)client).Abort();
                    factory.Abort();
                }
                catch
                {
                    // Ignorar errores de cierre
                }

                return false;
            }
        }
        private void CrearOActualizarCliente(SqlConnection connection, SqlTransaction transaction, string cedula, string nombre)
        {
            using (var checkCmd = connection.CreateCommand())
            {
                checkCmd.Transaction = transaction;
                checkCmd.CommandText = "SELECT COUNT(*) FROM CLIENTE WHERE cedula = @cedula";
                checkCmd.Parameters.AddWithValue("@cedula", cedula);

                var count = Convert.ToInt32(checkCmd.ExecuteScalar());
                if (count == 0)
                {
                    using (var insertCmd = connection.CreateCommand())
                    {
                        insertCmd.Transaction = transaction;
                        insertCmd.CommandText = "INSERT INTO CLIENTE (cedula, nombre) VALUES (@cedula, @nombre)";
                        insertCmd.Parameters.AddWithValue("@cedula", cedula);
                        insertCmd.Parameters.AddWithValue("@nombre", nombre);
                        insertCmd.ExecuteNonQuery();
                    }
                }
            }
        }

        private int? CrearFactura(SqlConnection connection, SqlTransaction transaction, VentaRequest request,
            decimal subtotal, decimal descuento, decimal total)
        {
            var estadoFactura = string.Equals(request.FormaPago, "EFECTIVO", StringComparison.OrdinalIgnoreCase)
                ? "PAGADA"
                : "APROBADA";

            using (var cmd = connection.CreateCommand())
            {
                cmd.Transaction = transaction;
                cmd.CommandText = "INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) " +
                                  "OUTPUT INSERTED.idFactura " +
                                  "VALUES (@cedula, CAST(GETDATE() AS DATE), @formaPago, @subtotal, @descuento, @total, @estado)";

                cmd.Parameters.AddWithValue("@cedula", request.CedulaCliente);
                cmd.Parameters.AddWithValue("@formaPago", request.FormaPago);
                cmd.Parameters.AddWithValue("@subtotal", subtotal);
                cmd.Parameters.AddWithValue("@descuento", descuento);
                cmd.Parameters.AddWithValue("@total", total);
                cmd.Parameters.AddWithValue("@estado", estadoFactura);

                var result = cmd.ExecuteScalar();
                if (result != null && result != DBNull.Value)
                {
                    return Convert.ToInt32(result);
                }
            }

            return null;
        }

        private void CrearDetalleFactura(SqlConnection connection, SqlTransaction transaction, int idFactura,
            string idElectrodomestico, string cantidad, decimal precioUnitario, decimal subtotalLinea)
        {
            using (var cmd = connection.CreateCommand())
            {
                cmd.Transaction = transaction;
                cmd.CommandText =
                    "INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea) " +
                    "VALUES (@idFactura, @idElectrodomestico, @cantidad, @precioUnitario, @subtotalLinea)";
                cmd.Parameters.AddWithValue("@idFactura", idFactura);
                cmd.Parameters.AddWithValue("@idElectrodomestico", int.Parse(idElectrodomestico));
                cmd.Parameters.AddWithValue("@cantidad", int.Parse(cantidad));
                cmd.Parameters.AddWithValue("@precioUnitario", precioUnitario);
                cmd.Parameters.AddWithValue("@subtotalLinea", subtotalLinea);
                cmd.ExecuteNonQuery();
            }
        }

        public List<Electrodomestico> ListarElectrodomesticos()
        {
            var electrodomesticos = new List<Electrodomestico>();

            using (var connection = DatabaseConnection.GetConnection())
            using (var cmd = connection.CreateCommand())
            {
                cmd.CommandText = "SELECT * FROM ELECTRODOMESTICO ORDER BY nombre";

                using (var reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        electrodomesticos.Add(new Electrodomestico
                        {
                            IdElectrodomestico = Convert.ToInt32(reader["idElectrodomestico"]),
                            Nombre = Convert.ToString(reader["nombre"]),
                            PrecioVenta = Convert.ToDecimal(reader["precioVenta"])
                        });
                    }
                }
            }

            return electrodomesticos;
        }
    }
}
