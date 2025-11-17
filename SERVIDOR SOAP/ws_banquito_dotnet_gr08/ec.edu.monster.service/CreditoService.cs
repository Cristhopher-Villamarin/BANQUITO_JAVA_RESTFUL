using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using ec.edu.monster.db;
using ec.edu.monster.model;
using ws_banquito_dotnet_gr08;

namespace ec.edu.monster.service
{
    public class CreditoService
    {
        private const decimal TasaAnual = 16.00m;

        public List<CreditoResponse> ObtenerCreditosActivosPorCliente(string cedula)
        {
            var creditos = new List<CreditoResponse>();

            using (var connection = DatabaseConnection.GetConnection())
            using (var cmd = connection.CreateCommand())
            {
                cmd.CommandText = "SELECT idCredito, cedula, montoSolicitado, montoAprobado, plazoMeses, tasaAnual, cuotaFija, estado, fechaSolicitud, fechaAprobacion " +
                                  "FROM CREDITO WHERE cedula = @cedula AND estado = 'ACTIVO' ORDER BY fechaAprobacion DESC";
                cmd.Parameters.AddWithValue("@cedula", cedula?.Trim());

                using (var reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        var credito = new CreditoResponse
                        {
                            IdCredito = Convert.ToInt32(reader["idCredito"]),
                            Cedula = Convert.ToString(reader["cedula"]),
                            MontoSolicitado = Convert.ToDecimal(reader["montoSolicitado"]),
                            MontoAprobado = Convert.ToDecimal(reader["montoAprobado"]),
                            PlazoMeses = Convert.ToInt32(reader["plazoMeses"]),
                            TasaAnual = Convert.ToDecimal(reader["tasaAnual"]),
                            CuotaFija = Convert.ToDecimal(reader["cuotaFija"]),
                            Estado = Convert.ToString(reader["estado"]),
                            FechaSolicitud = reader["fechaSolicitud"] == DBNull.Value
                                ? null
                                : Convert.ToDateTime(reader["fechaSolicitud"]).ToString("yyyy-MM-dd"),
                            FechaAprobacion = reader["fechaAprobacion"] == DBNull.Value
                                ? null
                                : Convert.ToDateTime(reader["fechaAprobacion"]).ToString("yyyy-MM-dd")
                        };

                        creditos.Add(credito);
                    }
                }
            }

            return creditos;
        }

        public EvaluacionCreditoResponse VerificarSujetoCredito(string cedula)
        {
            var response = new EvaluacionCreditoResponse
            {
                CreditoAprobado = null
            };

            try
            {
                using (var connection = DatabaseConnection.GetConnection())
                {
                    var cliente = BuscarCliente(connection, cedula);
                    if (cliente == null)
                    {
                        response.SujetoCredito = false;
                        response.Mensaje = "El solicitante no es cliente del banco";
                        return response;
                    }

                    if (cliente.EsCasado() && cliente.Edad < 25)
                    {
                        response.SujetoCredito = false;
                        response.Mensaje = "Cliente casado menor a 25 años no es sujeto de crédito";
                        return response;
                    }

                    var tieneCreditoActivo = VerificarCreditoActivo(connection, cedula);
                    if (tieneCreditoActivo)
                    {
                        response.SujetoCredito = false;
                        response.Mensaje = "El cliente ya tiene un crédito activo";
                        return response;
                    }

                    var tieneDepositoUltimoMes = VerificarDepositoUltimoMes(connection, cedula);
                    if (!tieneDepositoUltimoMes)
                    {
                        response.SujetoCredito = false;
                        response.Mensaje = "El cliente no tiene depósitos en el último mes";
                        return response;
                    }

                    var montoMaximo = CalcularMontoMaximoCredito(connection, cedula);
                    response.SujetoCredito = true;
                    response.Mensaje = "Cliente cumple requisitos para ser sujeto de crédito";
                    response.MontoMaximoCredito = montoMaximo;
                }
            }
            catch (Exception ex)
            {
                response.Mensaje = "Error en el proceso de verificación" + (ex.Message != null ? ": " + ex.Message : string.Empty);
            }

            return response;
        }

        public EvaluacionCreditoResponse EvaluarCredito(EvaluacionCreditoRequest request)
        {
            var response = new EvaluacionCreditoResponse();

            try
            {
                using (var connection = DatabaseConnection.GetConnection())
                {
                    var cliente = BuscarCliente(connection, request.Cedula);
                    if (cliente == null)
                    {
                        response.SujetoCredito = false;
                        response.Mensaje = "El solicitante no es cliente del banco";
                        return response;
                    }

                    if (cliente.EsCasado() && cliente.Edad < 25)
                    {
                        response.SujetoCredito = false;
                        response.Mensaje = "Cliente casado menor a 25 años no es sujeto de crédito";
                        return response;
                    }

                    var tieneCreditoActivo = VerificarCreditoActivo(connection, request.Cedula);
                    if (tieneCreditoActivo)
                    {
                        response.SujetoCredito = false;
                        response.Mensaje = "El cliente ya tiene un crédito activo";
                        return response;
                    }

                    var tieneDepositoUltimoMes = VerificarDepositoUltimoMes(connection, request.Cedula);
                    if (!tieneDepositoUltimoMes)
                    {
                        response.SujetoCredito = false;
                        response.Mensaje = "El cliente no tiene depósitos en el último mes";
                        return response;
                    }

                    var montoMaximo = CalcularMontoMaximoCredito(connection, request.Cedula);

                    response.SujetoCredito = true;
                    response.MontoMaximoCredito = montoMaximo;

                    if (request.MontoElectrodomestico <= montoMaximo)
                    {
                        response.CreditoAprobado = true;
                        response.Mensaje = "Crédito aprobado";

                        var cuotaMensual = CalcularCuotaFija(request.MontoElectrodomestico, request.PlazoMeses);
                        response.CuotaMensual = cuotaMensual;

                        try
                        {
                            var idCredito = CrearCredito(connection, request.Cedula, request.MontoElectrodomestico, request.PlazoMeses, cuotaMensual);
                            response.IdCredito = idCredito;

                            if (idCredito.HasValue)
                            {
                                var tabla = GenerarTablaAmortizacion(idCredito.Value, request.MontoElectrodomestico, request.PlazoMeses, cuotaMensual);
                                response.TablaAmortizacion = tabla;

                                GuardarTablaAmortizacion(connection, idCredito.Value, tabla);
                            }
                        }
                        catch (Exception creditError)
                        {
                            response.CreditoAprobado = false;
                            response.Mensaje = "Error al crear el crédito: " + creditError.Message;
                            response.IdCredito = null;
                            response.CuotaMensual = null;
                            response.TablaAmortizacion = null;
                        }
                    }
                    else
                    {
                        response.CreditoAprobado = false;
                        response.Mensaje = "Monto del electrodoméstico excede el crédito máximo autorizado";
                    }
                }
            }
            catch (Exception e)
            {
                response.SujetoCredito = false;
                response.CreditoAprobado = false;
                response.Mensaje = "Error en el proceso de evaluación: " + e.Message;
            }

            return response;
        }

        private Cliente BuscarCliente(SqlConnection connection, string cedula)
        {
            using (var cmd = connection.CreateCommand())
            {
                cmd.CommandText = "SELECT * FROM CLIENTE WHERE cedula = @cedula";
                cmd.Parameters.AddWithValue("@cedula", cedula);

                using (var reader = cmd.ExecuteReader())
                {
                    if (reader.Read())
                    {
                        return new Cliente
                        {
                            Cedula = Convert.ToString(reader["cedula"]),
                            Nombre = Convert.ToString(reader["nombre"]),
                            FechaNacimiento = Convert.ToDateTime(reader["fecha_nacimiento"]),
                            EstadoCivil = Convert.ToString(reader["estado_civil"])
                        };
                    }
                }
            }

            return null;
        }

        private bool VerificarCreditoActivo(SqlConnection connection, string cedula)
        {
            using (var cmd = connection.CreateCommand())
            {
                cmd.CommandText = "SELECT COUNT(*) FROM CREDITO WHERE cedula = @cedula AND estado = 'ACTIVO'";
                cmd.Parameters.AddWithValue("@cedula", cedula);

                var count = Convert.ToInt32(cmd.ExecuteScalar());
                return count > 0;
            }
        }

        private bool VerificarDepositoUltimoMes(SqlConnection connection, string cedula)
        {
            using (var cmd = connection.CreateCommand())
            {
                cmd.CommandText =
                    "SELECT COUNT(*) FROM MOVIMIENTO m " +
                    "JOIN CUENTA c ON m.numCuenta = c.numCuenta " +
                    "WHERE c.cedula = @cedula AND m.tipo = 'DEP' " +
                    "AND m.fecha >= DATEADD(MONTH, -1, CAST(GETDATE() AS DATE))";

                cmd.Parameters.AddWithValue("@cedula", cedula);

                var count = Convert.ToInt32(cmd.ExecuteScalar());
                return count > 0;
            }
        }

        private decimal CalcularMontoMaximoCredito(SqlConnection connection, string cedula)
        {
            var promedioDepositos = CalcularPromedioDepositos(connection, cedula);
            var promedioRetiros = CalcularPromedioRetiros(connection, cedula);

            var diferencia = promedioDepositos - promedioRetiros;
            var sesentaPorciento = diferencia * 0.60m;
            var montoMaximo = sesentaPorciento * 9m;

            return Math.Round(montoMaximo, 2, MidpointRounding.AwayFromZero);
        }

        private decimal CalcularPromedioDepositos(SqlConnection connection, string cedula)
        {
            using (var cmd = connection.CreateCommand())
            {
                cmd.CommandText =
                    "SELECT COALESCE(AVG(m.valor), 0) as promedio FROM MOVIMIENTO m " +
                    "JOIN CUENTA c ON m.numCuenta = c.numCuenta " +
                    "WHERE c.cedula = @cedula AND m.tipo = 'DEP' " +
                    "AND m.fecha >= DATEADD(MONTH, -3, CAST(GETDATE() AS DATE))";

                cmd.Parameters.AddWithValue("@cedula", cedula);

                var result = cmd.ExecuteScalar();
                return result == DBNull.Value ? 0m : Convert.ToDecimal(result);
            }
        }

        private decimal CalcularPromedioRetiros(SqlConnection connection, string cedula)
        {
            using (var cmd = connection.CreateCommand())
            {
                cmd.CommandText =
                    "SELECT COALESCE(AVG(m.valor), 0) as promedio FROM MOVIMIENTO m " +
                    "JOIN CUENTA c ON m.numCuenta = c.numCuenta " +
                    "WHERE c.cedula = @cedula AND m.tipo = 'RET' " +
                    "AND m.fecha >= DATEADD(MONTH, -3, CAST(GETDATE() AS DATE))";

                cmd.Parameters.AddWithValue("@cedula", cedula);

                var result = cmd.ExecuteScalar();
                return result == DBNull.Value ? 0m : Convert.ToDecimal(result);
            }
        }

        private decimal CalcularCuotaFija(decimal monto, int plazoMeses)
        {
            var tasaPeriodo = 0.16m / 12m;
            var unoMasTasa = 1m + tasaPeriodo;
            var potenciaPositiva = PowDecimal(unoMasTasa, plazoMeses);
            var potenciaNegativa = 1m / potenciaPositiva;
            var denominador = 1m - potenciaNegativa;

            var cuota = monto * tasaPeriodo / denominador;
            return Math.Round(cuota, 2, MidpointRounding.AwayFromZero);
        }

        private static decimal PowDecimal(decimal baseValue, int exponent)
        {
            decimal result = 1m;
            for (int i = 0; i < exponent; i++)
            {
                result *= baseValue;
            }
            return result;
        }

        private int? CrearCredito(SqlConnection connection, string cedula, decimal monto, int plazo, decimal cuota)
        {
            if (string.IsNullOrWhiteSpace(cedula))
            {
                throw new Exception("La cédula no puede ser nula o vacía");
            }

            if (monto <= 0)
            {
                throw new Exception("El monto debe ser mayor que cero");
            }

            if (plazo <= 0)
            {
                throw new Exception("El plazo debe ser mayor que cero");
            }

            if (cuota <= 0)
            {
                throw new Exception("La cuota debe ser mayor que cero");
            }

            using (var cmd = connection.CreateCommand())
            {
                cmd.CommandText =
                    "INSERT INTO CREDITO (cedula, montoSolicitado, montoAprobado, plazoMeses, tasaAnual, cuotaFija, estado, fechaSolicitud, fechaAprobacion) " +
                    "VALUES (@cedula, @montoSolicitado, @montoAprobado, @plazoMeses, @tasaAnual, @cuotaFija, 'ACTIVO', CAST(GETDATE() AS DATE), CAST(GETDATE() AS DATE))";

                cmd.Parameters.AddWithValue("@cedula", cedula.Trim());
                cmd.Parameters.AddWithValue("@montoSolicitado", monto);
                cmd.Parameters.AddWithValue("@montoAprobado", monto);
                cmd.Parameters.AddWithValue("@plazoMeses", plazo);
                cmd.Parameters.AddWithValue("@tasaAnual", TasaAnual);
                cmd.Parameters.AddWithValue("@cuotaFija", cuota);

                cmd.ExecuteNonQuery();

                cmd.CommandText = "SELECT CAST(SCOPE_IDENTITY() AS INT);";
                cmd.Parameters.Clear();

                var result = cmd.ExecuteScalar();
                if (result != null && result != DBNull.Value)
                {
                    return Convert.ToInt32(result);
                }
            }

            return null;
        }

        private List<AmortizacionDTO> GenerarTablaAmortizacion(int idCredito, decimal monto, int plazo, decimal cuota)
        {
            var tabla = new List<AmortizacionDTO>();
            var saldo = monto;
            var tasaPeriodo = 0.16m / 12m;

            for (int i = 1; i <= plazo; i++)
            {
                var interesPagado = Math.Round(saldo * tasaPeriodo, 2, MidpointRounding.AwayFromZero);
                var capitalPagado = cuota - interesPagado;
                saldo -= capitalPagado;

                if (i == plazo)
                {
                    capitalPagado += saldo;
                    cuota += saldo;
                    saldo = 0m;
                }

                tabla.Add(new AmortizacionDTO
                {
                    NumeroCuota = i,
                    ValorCuota = cuota,
                    InteresPagado = interesPagado,
                    CapitalPagado = capitalPagado,
                    Saldo = saldo
                });
            }

            return tabla;
        }

        private void GuardarTablaAmortizacion(SqlConnection connection, int idCredito, List<AmortizacionDTO> tabla)
        {
            using (var cmd = connection.CreateCommand())
            {
                cmd.CommandText =
                    "INSERT INTO AMORTIZACION (idCredito, numeroCuota, valorCuota, interesPagado, capitalPagado, saldo) " +
                    "VALUES (@idCredito, @numeroCuota, @valorCuota, @interesPagado, @capitalPagado, @saldo)";

                cmd.Parameters.Add("@idCredito", SqlDbType.Int);
                cmd.Parameters.Add("@numeroCuota", SqlDbType.Int);
                cmd.Parameters.Add("@valorCuota", SqlDbType.Decimal).Precision = 18;
                cmd.Parameters.Add("@interesPagado", SqlDbType.Decimal).Precision = 18;
                cmd.Parameters.Add("@capitalPagado", SqlDbType.Decimal).Precision = 18;
                cmd.Parameters.Add("@saldo", SqlDbType.Decimal).Precision = 18;

                cmd.Parameters["@valorCuota"].Scale = 2;
                cmd.Parameters["@interesPagado"].Scale = 2;
                cmd.Parameters["@capitalPagado"].Scale = 2;
                cmd.Parameters["@saldo"].Scale = 2;

                foreach (var dto in tabla)
                {
                    cmd.Parameters["@idCredito"].Value = idCredito;
                    cmd.Parameters["@numeroCuota"].Value = dto.NumeroCuota;
                    cmd.Parameters["@valorCuota"].Value = dto.ValorCuota;
                    cmd.Parameters["@interesPagado"].Value = dto.InteresPagado;
                    cmd.Parameters["@capitalPagado"].Value = dto.CapitalPagado;
                    cmd.Parameters["@saldo"].Value = dto.Saldo;

                    cmd.ExecuteNonQuery();
                }
            }
        }

        public List<AmortizacionDTO> ObtenerTablaAmortizacion(int idCredito)
        {
            var tabla = new List<AmortizacionDTO>();

            using (var connection = DatabaseConnection.GetConnection())
            using (var cmd = connection.CreateCommand())
            {
                cmd.CommandText =
                    "SELECT numeroCuota, valorCuota, interesPagado, capitalPagado, saldo " +
                    "FROM AMORTIZACION WHERE idCredito = @idCredito ORDER BY numeroCuota";

                cmd.Parameters.AddWithValue("@idCredito", idCredito);

                using (var reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        var dto = new AmortizacionDTO
                        {
                            NumeroCuota = Convert.ToInt32(reader["numeroCuota"]),
                            ValorCuota = Convert.ToDecimal(reader["valorCuota"]),
                            InteresPagado = Convert.ToDecimal(reader["interesPagado"]),
                            CapitalPagado = Convert.ToDecimal(reader["capitalPagado"]),
                            Saldo = Convert.ToDecimal(reader["saldo"])
                        };

                        tabla.Add(dto);
                    }
                }
            }

            return tabla;
        }

        public TablaAmortizacionResponse ObtenerTablaAmortizacionParaComercializadora(int idCredito)
        {
            using (var connection = DatabaseConnection.GetConnection())
            using (var cmd = connection.CreateCommand())
            {
                cmd.CommandText =
                    "SELECT cr.idCredito, cr.cedula, cr.montoAprobado, cr.plazoMeses, cr.cuotaFija, cr.tasaAnual, cl.nombre " +
                    "FROM CREDITO cr " +
                    "JOIN CLIENTE cl ON cr.cedula = cl.cedula " +
                    "WHERE cr.idCredito = @idCredito";

                cmd.Parameters.AddWithValue("@idCredito", idCredito);

                using (var reader = cmd.ExecuteReader())
                {
                    if (!reader.Read())
                    {
                        return null;
                    }

                    var idCreditoResult = Convert.ToInt32(reader["idCredito"]);
                    var cedula = Convert.ToString(reader["cedula"]);
                    var nombreCliente = Convert.ToString(reader["nombre"]);
                    var montoTotal = Convert.ToDecimal(reader["montoAprobado"]);
                    var plazoMeses = Convert.ToInt32(reader["plazoMeses"]);
                    var cuotaMensual = Convert.ToDecimal(reader["cuotaFija"]);
                    var tasaInteres = Convert.ToDecimal(reader["tasaAnual"]);

                    reader.Close();

                    var tabla = ObtenerTablaAmortizacion(idCredito);
                    var cuotas = new List<CuotaAmortizacionResponse>();

                    foreach (var dto in tabla)
                    {
                        var cuota = new CuotaAmortizacionResponse
                        {
                            NumeroCuota = dto.NumeroCuota,
                            Capital = dto.CapitalPagado,
                            Interes = dto.InteresPagado,
                            Cuota = dto.ValorCuota,
                            SaldoFinal = dto.Saldo
                        };

                        cuotas.Add(cuota);
                    }

                    return new TablaAmortizacionResponse
                    {
                        IdFactura = idCreditoResult,
                        NombreCliente = nombreCliente,
                        CedulaCliente = cedula,
                        MontoTotal = montoTotal,
                        TasaInteres = tasaInteres,
                        NumeroCuotas = plazoMeses,
                        CuotaMensual = cuotaMensual,
                        Cuotas = cuotas
                    };
                }
            }
        }
    }
}
