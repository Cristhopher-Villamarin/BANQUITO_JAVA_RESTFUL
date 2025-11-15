package ec.edu.monster.test;

import ec.edu.monster.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        try {
            System.out.println("Probando conexión a la base de datos...");
            Connection connection = DatabaseConnection.getConnection();
            
            if (connection != null && !connection.isClosed()) {
                System.out.println("✅ Conexión exitosa a la base de datos!");
                System.out.println("URL: " + connection.getMetaData().getURL());
                System.out.println("Usuario: " + connection.getMetaData().getUserName());
                connection.close();
            } else {
                System.out.println("❌ No se pudo establecer la conexión");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
