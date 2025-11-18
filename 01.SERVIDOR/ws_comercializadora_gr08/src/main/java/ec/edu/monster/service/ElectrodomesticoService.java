package ec.edu.monster.service;

import ec.edu.monster.dto.ElectrodomesticoRequest;
import ec.edu.monster.dto.ElectrodomesticoResponse;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.db.DatabaseConnection;

import jakarta.enterprise.context.ApplicationScoped;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class ElectrodomesticoService {
    
    private static final Logger LOGGER = Logger.getLogger(ElectrodomesticoService.class.getName());
    
    public List<ElectrodomesticoResponse> listarTodos() {
        List<ElectrodomesticoResponse> electrodomesticos = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM ELECTRODOMESTICO ORDER BY nombre";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    ElectrodomesticoResponse electrodomestico = new ElectrodomesticoResponse(
                        rs.getInt("idElectrodomestico"),
                        rs.getString("nombre"),
                        rs.getBigDecimal("precioVenta"),
                        rs.getString("fotoUrl")
                    );
                    electrodomesticos.add(electrodomestico);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error listando electrodomésticos", e);
        }
        
        return electrodomesticos;
    }
    
    public ElectrodomesticoResponse buscarPorId(Integer id) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM ELECTRODOMESTICO WHERE idElectrodomestico = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new ElectrodomesticoResponse(
                        rs.getInt("idElectrodomestico"),
                        rs.getString("nombre"),
                        rs.getBigDecimal("precioVenta"),
                        rs.getString("fotoUrl")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error buscando electrodoméstico", e);
        }
        
        return null;
    }
    
    public ElectrodomesticoResponse crear(ElectrodomesticoRequest request) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO ELECTRODOMESTICO (nombre, precioVenta, fotoUrl) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, request.getNombre());
                stmt.setBigDecimal(2, request.getPrecioVenta());
                stmt.setString(3, request.getFotoUrl());
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        Integer id = rs.getInt(1);
                        return new ElectrodomesticoResponse(id, request.getNombre(), request.getPrecioVenta(), request.getFotoUrl());
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creando electrodoméstico", e);
        }
        
        return null;
    }
    
    public ElectrodomesticoResponse actualizar(Integer id, ElectrodomesticoRequest request) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Verificar si el electrodoméstico existe
            ElectrodomesticoResponse existente = buscarPorId(id);
            if (existente == null) {
                return null;
            }
            
            String sql = "UPDATE ELECTRODOMESTICO SET nombre = ?, precioVenta = ?, fotoUrl = ? WHERE idElectrodomestico = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, request.getNombre());
                stmt.setBigDecimal(2, request.getPrecioVenta());
                stmt.setString(3, request.getFotoUrl());
                stmt.setInt(4, id);
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    return new ElectrodomesticoResponse(id, request.getNombre(), request.getPrecioVenta(), request.getFotoUrl());
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error actualizando electrodoméstico", e);
        }
        
        return null;
    }
    
    public boolean eliminar(Integer id) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Verificar si el electrodoméstico existe
            ElectrodomesticoResponse existente = buscarPorId(id);
            if (existente == null) {
                return false;
            }
            
            // Verificar si tiene detalles de factura asociados
            String checkSql = "SELECT COUNT(*) FROM DETALLE_FACTURA WHERE idElectrodomestico = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, id);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    LOGGER.log(Level.WARNING, "No se puede eliminar electrodoméstico con detalles de factura asociados");
                    return false;
                }
            }
            
            String sql = "DELETE FROM ELECTRODOMESTICO WHERE idElectrodomestico = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error eliminando electrodoméstico", e);
        }
        
        return false;
    }
}
