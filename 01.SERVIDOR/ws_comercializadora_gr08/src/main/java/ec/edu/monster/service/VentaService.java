package ec.edu.monster.service;

import ec.edu.monster.db.DatabaseConnection;
import ec.edu.monster.dto.*;
import ec.edu.monster.model.DetalleFactura;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.model.Factura;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VentaService {
    
    private static final Logger LOGGER = Logger.getLogger(VentaService.class.getName());
    private static final String BANQUITO_API_URL = "http://localhost:8080/ws_banquito_gr08/api";
    private static final BigDecimal DESCUENTO_EFECTIVO = new BigDecimal("0.33");
    
    public VentaResponse procesarVenta(VentaRequest request) {
        VentaResponse response = new VentaResponse();
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            
            // Validar forma de pago
            if (!"EFECTIVO".equalsIgnoreCase(request.getFormaPago()) && !"CREDITO_DIRECTO".equalsIgnoreCase(request.getFormaPago())) {
                response.setVentaExitosa(false);
                response.setMensaje("Forma de pago no válida. Use EFECTIVO o CREDITO_DIRECTO");
                return response;
            }
            
            // Obtener información de electrodomésticos y calcular totales
            List<Electrodomestico> electrodomesticos = new ArrayList<>();
            BigDecimal subtotal = BigDecimal.ZERO;
            
            for (DetalleVentaRequest detalle : request.getDetalles()) {
                Electrodomestico electrodomestico = buscarElectrodomestico(connection, Integer.parseInt(detalle.getIdElectrodomestico()));
                if (electrodomestico == null) {
                    connection.rollback();
                    response.setVentaExitosa(false);
                    response.setMensaje("Electrodoméstico con ID " + detalle.getIdElectrodomestico() + " no encontrado");
                    return response;
                }
                
                electrodomesticos.add(electrodomestico);
                BigDecimal subtotalLinea = electrodomestico.getPrecioVenta().multiply(new BigDecimal(detalle.getCantidad()));
                subtotal = subtotal.add(subtotalLinea);
            }
            
            // Calcular descuento y total
            BigDecimal descuento = BigDecimal.ZERO;
            BigDecimal total = subtotal;
            
            if ("EFECTIVO".equalsIgnoreCase(request.getFormaPago())) {
                descuento = subtotal.multiply(DESCUENTO_EFECTIVO).setScale(2, RoundingMode.HALF_UP);
                total = subtotal.subtract(descuento);
            }
            
            // Verificar crédito directo si aplica
            if ("CREDITO_DIRECTO".equalsIgnoreCase(request.getFormaPago())) {
                boolean creditoAprobado = verificarCreditoBanquito(request.getCedulaCliente(), total, request.getPlazoMeses());
                if (!creditoAprobado) {
                    connection.rollback();
                    response.setVentaExitosa(false);
                    response.setMensaje("Crédito no aprobado por el banco Banquito");
                    return response;
                }
            }
            
            // Crear o actualizar cliente
            crearOActualizarCliente(connection, request.getCedulaCliente(), request.getNombreCliente());
            
            // Crear factura
            Integer idFactura = crearFactura(connection, request, subtotal, descuento, total);
            
            // Crear detalles de factura
            List<DetalleVentaResponse> detallesResponse = new ArrayList<>();
            for (int i = 0; i < request.getDetalles().size(); i++) {
                DetalleVentaRequest detalleRequest = request.getDetalles().get(i);
                Electrodomestico electrodomestico = electrodomesticos.get(i);
                
                BigDecimal subtotalLinea = electrodomestico.getPrecioVenta().multiply(new BigDecimal(detalleRequest.getCantidad()));
                
                crearDetalleFactura(connection, idFactura, detalleRequest.getIdElectrodomestico(), 
                                 detalleRequest.getCantidad(), electrodomestico.getPrecioVenta(), subtotalLinea);
                
                detallesResponse.add(new DetalleVentaResponse(
                    detalleRequest.getIdElectrodomestico(),
                    electrodomestico.getNombre(),
                    detalleRequest.getCantidad(),
                    electrodomestico.getPrecioVenta(),
                    subtotalLinea
                ));
            }
            
            connection.commit();
            
            // Configurar respuesta
            response.setVentaExitosa(true);
            response.setMensaje("Venta procesada exitosamente");
            response.setIdFactura(idFactura);
            response.setSubtotal(subtotal);
            response.setDescuento(descuento);
            response.setTotal(total);
            response.setFormaPago(request.getFormaPago());
            
            // Establecer estado según forma de pago
            if ("EFECTIVO".equalsIgnoreCase(request.getFormaPago())) {
                response.setEstadoFactura("PAGADA");
            } else if ("CREDITO_DIRECTO".equalsIgnoreCase(request.getFormaPago())) {
                response.setEstadoFactura("APROBADA");
            }
            
            response.setDetalles(detallesResponse);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error procesando venta", e);
            response.setVentaExitosa(false);
            response.setMensaje("Error en el proceso de venta");
        }
        
        return response;
    }
    
    private Electrodomestico buscarElectrodomestico(Connection connection, Integer idElectrodomestico) throws SQLException {
        String sql = "SELECT * FROM ELECTRODOMESTICO WHERE idElectrodomestico = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idElectrodomestico);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Electrodomestico electrodomestico = new Electrodomestico();
                electrodomestico.setIdElectrodomestico(rs.getInt("idElectrodomestico"));
                electrodomestico.setNombre(rs.getString("nombre"));
                electrodomestico.setPrecioVenta(rs.getBigDecimal("precioVenta"));
                return electrodomestico;
            }
        }
        return null;
    }
    
    private boolean verificarCreditoBanquito(String cedula, BigDecimal monto, Integer plazoMeses) {
        try {
            // Llamar al servicio del banco Banquito
            java.net.URL url = new java.net.URL(BANQUITO_API_URL + "/creditos/evaluar");
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            // Construir JSON request
            String jsonInputString = String.format(
                "{\"cedula\":\"%s\",\"montoElectrodomestico\":%s,\"plazoMeses\":%d}",
                cedula, monto.toString().replace(',', '.'), plazoMeses != null ? plazoMeses : 12
            );
            
            LOGGER.log(Level.INFO, "Enviando solicitud a Banquito: {0}", jsonInputString);
            
            try (java.io.OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            int responseCode = conn.getResponseCode();
            LOGGER.log(Level.INFO, "Response Code de Banquito: {0}", responseCode);
            
            if (responseCode == 200) {
                try (java.io.BufferedReader br = new java.io.BufferedReader(
                        new java.io.InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    
                    // Simple JSON parsing para verificar si el crédito fue aprobado
                    String jsonResponse = response.toString();
                    LOGGER.log(Level.INFO, "Respuesta de Banquito: {0}", jsonResponse);
                    
                    boolean aprobado = jsonResponse.contains("\"creditoAprobado\":true");
                    LOGGER.log(Level.INFO, "Crédito aprobado: {0}", aprobado);
                    
                    return aprobado;
                }
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error verificando crédito con Banquito", e);
        }
        
        return false;
    }
    
    private void crearOActualizarCliente(Connection connection, String cedula, String nombre) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM CLIENTE WHERE cedula = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, cedula);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                // Insertar nuevo cliente
                String insertSql = "INSERT INTO CLIENTE (cedula, nombre) VALUES (?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    insertStmt.setString(1, cedula);
                    insertStmt.setString(2, nombre);
                    insertStmt.executeUpdate();
                }
            }
        }
    }
    
    private Integer crearFactura(Connection connection, VentaRequest request, BigDecimal subtotal, BigDecimal descuento, BigDecimal total) throws SQLException {
        // Determinar estado según forma de pago
        String estadoFactura = "EFECTIVO".equalsIgnoreCase(request.getFormaPago()) ? "PAGADA" : "APROBADA";
        
        String sql = "INSERT INTO FACTURA (cedula, fecha, formaPago, subtotal, descuento, total, estado) " +
                    "VALUES (?, CURDATE(), ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, request.getCedulaCliente());
            stmt.setString(2, request.getFormaPago());
            stmt.setBigDecimal(3, subtotal);
            stmt.setBigDecimal(4, descuento);
            stmt.setBigDecimal(5, total);
            stmt.setString(6, estadoFactura);
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return null;
    }
    
    private void crearDetalleFactura(Connection connection, Integer idFactura, String idElectrodomestico, 
                                   String cantidad, BigDecimal precioUnitario, BigDecimal subtotalLinea) throws SQLException {
        String sql = "INSERT INTO DETALLE_FACTURA (idFactura, idElectrodomestico, cantidad, precioUnitario, subtotalLinea) " +
                    "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFactura);
            stmt.setInt(2, Integer.parseInt(idElectrodomestico));
            stmt.setInt(3, Integer.parseInt(cantidad));
            stmt.setBigDecimal(4, precioUnitario);
            stmt.setBigDecimal(5, subtotalLinea);
            stmt.executeUpdate();
        }
    }
    
    public List<Electrodomestico> listarElectrodomesticos() {
        List<Electrodomestico> electrodomesticos = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM ELECTRODOMESTICO ORDER BY nombre";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Electrodomestico electrodomestico = new Electrodomestico();
                    electrodomestico.setIdElectrodomestico(rs.getInt("idElectrodomestico"));
                    electrodomestico.setNombre(rs.getString("nombre"));
                    electrodomestico.setPrecioVenta(rs.getBigDecimal("precioVenta"));
                    electrodomesticos.add(electrodomestico);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error listando electrodomésticos", e);
        }
        
        return electrodomesticos;
    }
}
