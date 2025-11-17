using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using ec.edu.monster.db;
using ws_comer_dotnet_gr08;

namespace ec.edu.monster.service
{
    public class ElectrodomesticoService
    {
        public List<ElectrodomesticoResponse> ListarTodos()
        {
            var electrodomesticos = new List<ElectrodomesticoResponse>();

            using (var connection = DatabaseConnection.GetConnection())
            using (var cmd = connection.CreateCommand())
            {
                cmd.CommandText = "SELECT * FROM ELECTRODOMESTICO ORDER BY nombre";

                using (var reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        var e = new ElectrodomesticoResponse
                        {
                            IdElectrodomestico = Convert.ToInt32(reader["idElectrodomestico"]),
                            Nombre = Convert.ToString(reader["nombre"]),
                            PrecioVenta = Convert.ToDecimal(reader["precioVenta"])
                        };
                        electrodomesticos.Add(e);
                    }
                }
            }

            return electrodomesticos;
        }

        public ElectrodomesticoResponse BuscarPorId(int id)
        {
            using (var connection = DatabaseConnection.GetConnection())
            using (var cmd = connection.CreateCommand())
            {
                cmd.CommandText = "SELECT * FROM ELECTRODOMESTICO WHERE idElectrodomestico = @id";
                cmd.Parameters.AddWithValue("@id", id);

                using (var reader = cmd.ExecuteReader())
                {
                    if (reader.Read())
                    {
                        return new ElectrodomesticoResponse
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

        public ElectrodomesticoResponse Crear(ElectrodomesticoRequest request)
        {
            using (var connection = DatabaseConnection.GetConnection())
            using (var cmd = connection.CreateCommand())
            {
                cmd.CommandText = "INSERT INTO ELECTRODOMESTICO (nombre, precioVenta) VALUES (@nombre, @precioVenta)";
                cmd.Parameters.AddWithValue("@nombre", request.Nombre);
                cmd.Parameters.AddWithValue("@precioVenta", request.PrecioVenta);

                cmd.ExecuteNonQuery();

                cmd.CommandText = "SELECT CAST(SCOPE_IDENTITY() AS INT);";
                cmd.Parameters.Clear();

                var idObj = cmd.ExecuteScalar();
                if (idObj != null && idObj != DBNull.Value)
                {
                    var id = Convert.ToInt32(idObj);
                    return new ElectrodomesticoResponse
                    {
                        IdElectrodomestico = id,
                        Nombre = request.Nombre,
                        PrecioVenta = request.PrecioVenta
                    };
                }
            }

            return null;
        }

        public ElectrodomesticoResponse Actualizar(int id, ElectrodomesticoRequest request)
        {
            var existente = BuscarPorId(id);
            if (existente == null)
            {
                return null;
            }

            using (var connection = DatabaseConnection.GetConnection())
            using (var cmd = connection.CreateCommand())
            {
                cmd.CommandText = "UPDATE ELECTRODOMESTICO SET nombre = @nombre, precioVenta = @precioVenta WHERE idElectrodomestico = @id";
                cmd.Parameters.AddWithValue("@nombre", request.Nombre);
                cmd.Parameters.AddWithValue("@precioVenta", request.PrecioVenta);
                cmd.Parameters.AddWithValue("@id", id);

                var rows = cmd.ExecuteNonQuery();
                if (rows > 0)
                {
                    return new ElectrodomesticoResponse
                    {
                        IdElectrodomestico = id,
                        Nombre = request.Nombre,
                        PrecioVenta = request.PrecioVenta
                    };
                }
            }

            return null;
        }

        public bool Eliminar(int id)
        {
            var existente = BuscarPorId(id);
            if (existente == null)
            {
                return false;
            }

            using (var connection = DatabaseConnection.GetConnection())
            using (var checkCmd = connection.CreateCommand())
            {
                checkCmd.CommandText = "SELECT COUNT(*) FROM DETALLE_FACTURA WHERE idElectrodomestico = @id";
                checkCmd.Parameters.AddWithValue("@id", id);
                var count = Convert.ToInt32(checkCmd.ExecuteScalar());
                if (count > 0)
                {
                    return false;
                }
            }

            using (var connection = DatabaseConnection.GetConnection())
            using (var cmd = connection.CreateCommand())
            {
                cmd.CommandText = "DELETE FROM ELECTRODOMESTICO WHERE idElectrodomestico = @id";
                cmd.Parameters.AddWithValue("@id", id);
                var rows = cmd.ExecuteNonQuery();
                return rows > 0;
            }
        }
    }
}
