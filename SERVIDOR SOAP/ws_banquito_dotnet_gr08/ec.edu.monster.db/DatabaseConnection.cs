using System.Data.SqlClient;

namespace ec.edu.monster.db
{
    public static class DatabaseConnection
    {
        private const string ConnectionString =
            "Server=LAPTOP-5MJB3O7R\\SQLEXPRESS;Database=banquito_credito;Integrated Security=True;TrustServerCertificate=True;";

        public static SqlConnection GetConnection()
        {
            var connection = new SqlConnection(ConnectionString);
            connection.Open();
            return connection;
        }
    }
}
