package ec.edu.monster.service;

import ec.edu.monster.db.DatabaseConnection;
import ec.edu.monster.dto.AmortizacionDTO;
import ec.edu.monster.dto.EvaluacionCreditoRequest;
import ec.edu.monster.dto.EvaluacionCreditoResponse;
import ec.edu.monster.model.Amortizacion;
import ec.edu.monster.model.Cliente;
import ec.edu.monster.model.Credito;
import ec.edu.monster.model.Cuenta;
import ec.edu.monster.model.Movimiento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreditoService {
    
    private static final Logger LOGGER = Logger.getLogger(CreditoService.class.getName());
    private static final BigDecimal TASA_ANUAL = new BigDecimal("16.00");
    private static final BigDecimal TASA_MENSUAL = TASA_ANUAL.divide(new BigDecimal("12"), 6, RoundingMode.HALF_UP);
    
    public EvaluacionCreditoResponse verificarSujetoCredito(String cedula) {
        EvaluacionCreditoResponse response = new EvaluacionCreditoResponse();
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            
            // Verificar si es cliente del banco
            Cliente cliente = buscarCliente(connection, cedula);
            if (cliente == null) {
                response.setSujetoCredito(false);
                response.setMensaje("El solicitante no es cliente del banco");
                return response;
            }
            
            // Verificar regla de edad para casados
            if (cliente.esCasado() && cliente.getEdad() < 25) {
                response.setSujetoCredito(false);
                response.setMensaje("Cliente casado menor a 25 años no es sujeto de crédito");
                return response;
            }
            
            // Verificar si tiene crédito activo
            boolean tieneCreditoActivo = verificarCreditoActivo(connection, cedula);
            if (tieneCreditoActivo) {
                response.setSujetoCredito(false);
                response.setMensaje("El cliente ya tiene un crédito activo");
                return response;
            }
            
            // Verificar transacciones del último mes
            boolean tieneDepositoUltimoMes = verificarDepositoUltimoMes(connection, cedula);
            if (!tieneDepositoUltimoMes) {
                response.setSujetoCredito(false);
                response.setMensaje("El cliente no tiene depósitos en el último mes");
                return response;
            }
            
            // Calcular monto máximo de crédito
            BigDecimal montoMaximo = calcularMontoMaximoCredito(connection, cedula);
            
            response.setSujetoCredito(true);
            response.setMensaje("Cliente cumple requisitos para ser sujeto de crédito");
            response.setMontoMaximoCredito(montoMaximo);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error en verificación de sujeto de crédito", e);
            response.setMensaje("Error en el proceso de verificación");
        }
        
        return response;
    }
    
    public EvaluacionCreditoResponse evaluarCredito(EvaluacionCreditoRequest request) {
        EvaluacionCreditoResponse response = new EvaluacionCreditoResponse();
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            
            // Verificar si el solicitante es cliente del banco
            Cliente cliente = buscarCliente(connection, request.getCedula());
            if (cliente == null) {
                response.setSujetoCredito(false);
                response.setMensaje("El solicitante no es cliente del banco");
                return response;
            }
            
            // Verificar regla de edad para casados
            if (cliente.esCasado() && cliente.getEdad() < 25) {
                response.setSujetoCredito(false);
                response.setMensaje("Cliente casado menor a 25 años no es sujeto de crédito");
                return response;
            }
            
            // Verificar si tiene crédito activo
            boolean tieneCreditoActivo = verificarCreditoActivo(connection, request.getCedula());
            if (tieneCreditoActivo) {
                response.setSujetoCredito(false);
                response.setMensaje("El cliente ya tiene un crédito activo");
                return response;
            }
            
            // Verificar transacciones del último mes
            boolean tieneDepositoUltimoMes = verificarDepositoUltimoMes(connection, request.getCedula());
            if (!tieneDepositoUltimoMes) {
                response.setSujetoCredito(false);
                response.setMensaje("El cliente no tiene depósitos en el último mes");
                return response;
            }
            
            // Calcular monto máximo de crédito
            BigDecimal montoMaximo = calcularMontoMaximoCredito(connection, request.getCedula());
            
            response.setSujetoCredito(true);
            response.setMontoMaximoCredito(montoMaximo);
            
            // Verificar si el crédito es aprobado
            if (request.getMontoElectrodomestico().compareTo(montoMaximo) <= 0) {
                response.setCreditoAprobado(true);
                response.setMensaje("Crédito aprobado");
                
                // Crear crédito y tabla de amortización
                BigDecimal cuotaMensual = calcularCuotaFija(request.getMontoElectrodomestico(), request.getPlazoMeses());
                response.setCuotaMensual(cuotaMensual);
                
                Integer idCredito = crearCredito(connection, request.getCedula(), request.getMontoElectrodomestico(), request.getPlazoMeses(), cuotaMensual);
                response.setIdCredito(idCredito);
                
                List<AmortizacionDTO> tablaAmortizacion = generarTablaAmortizacion(idCredito, request.getMontoElectrodomestico(), request.getPlazoMeses(), cuotaMensual);
                response.setTablaAmortizacion(tablaAmortizacion);
                
                // Guardar tabla de amortización
                guardarTablaAmortizacion(connection, idCredito, tablaAmortizacion);
                
            } else {
                response.setCreditoAprobado(false);
                response.setMensaje("Monto del electrodoméstico excede el crédito máximo autorizado");
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error en evaluación de crédito", e);
            response.setMensaje("Error en el proceso de evaluación");
        }
        
        return response;
    }
    
    private Cliente buscarCliente(Connection connection, String cedula) throws SQLException {
        String sql = "SELECT * FROM CLIENTE WHERE cedula = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Cliente(
                    rs.getString("cedula"),
                    rs.getString("nombre"),
                    rs.getDate("fecha_nacimiento").toLocalDate(),
                    rs.getString("estado_civil")
                );
            }
        }
        return null;
    }
    
    private boolean verificarCreditoActivo(Connection connection, String cedula) throws SQLException {
        String sql = "SELECT COUNT(*) FROM CREDITO WHERE cedula = ? AND estado IN ('ACTIVO', 'PENDIENTE')";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    private boolean verificarDepositoUltimoMes(Connection connection, String cedula) throws SQLException {
        String sql = "SELECT COUNT(*) FROM MOVIMIENTO m " +
                    "JOIN CUENTA c ON m.numCuenta = c.numCuenta " +
                    "WHERE c.cedula = ? AND m.tipo = 'DEP' " +
                    "AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    private BigDecimal calcularMontoMaximoCredito(Connection connection, String cedula) throws SQLException {
        // Promedio depósitos últimos 3 meses
        BigDecimal promedioDepositos = calcularPromedioDepositos(connection, cedula);
        
        // Promedio retiros últimos 3 meses
        BigDecimal promedioRetiros = calcularPromedioRetiros(connection, cedula);
        
        // Calcular monto máximo: ((Promedio Depósitos – Promedio Retiros) * 60%) * 9
        BigDecimal diferencia = promedioDepositos.subtract(promedioRetiros);
        BigDecimal sesentaPorciento = diferencia.multiply(new BigDecimal("0.60"));
        BigDecimal montoMaximo = sesentaPorciento.multiply(new BigDecimal("9"));
        
        // Redondear a 2 decimales al final
        return montoMaximo.setScale(2, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calcularPromedioDepositos(Connection connection, String cedula) throws SQLException {
        String sql = "SELECT COALESCE(AVG(m.valor), 0) as promedio FROM MOVIMIENTO m " +
                    "JOIN CUENTA c ON m.numCuenta = c.numCuenta " +
                    "WHERE c.cedula = ? AND m.tipo = 'DEP' " +
                    "AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("promedio");
            }
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calcularPromedioRetiros(Connection connection, String cedula) throws SQLException {
        String sql = "SELECT COALESCE(AVG(m.valor), 0) as promedio FROM MOVIMIENTO m " +
                    "JOIN CUENTA c ON m.numCuenta = c.numCuenta " +
                    "WHERE c.cedula = ? AND m.tipo = 'RET' " +
                    "AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("promedio");
            }
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calcularCuotaFija(BigDecimal monto, Integer plazoMeses) {
        // Cuota = (Monto * TasaPeriodo) / (1 - (1+TasaPeriodo)^-NúmeroCuotas)
        // TasaPeriodo = 16%/12
        BigDecimal tasaPeriodo = new BigDecimal("0.16").divide(new BigDecimal("12"), 10, RoundingMode.HALF_UP);
        BigDecimal unoMasTasa = BigDecimal.ONE.add(tasaPeriodo);
        
        // Calcular (1+TasaPeriodo)^-NúmeroCuotas
        BigDecimal potenciaPositiva = unoMasTasa.pow(plazoMeses);
        BigDecimal potenciaNegativa = BigDecimal.ONE.divide(potenciaPositiva, 12, RoundingMode.HALF_UP);
        
        // Calcular denominador: 1 - ((1+TasaPeriodo)^-NúmeroCuotas)
        BigDecimal denominador = BigDecimal.ONE.subtract(potenciaNegativa);
        
        // Calcular cuota: (Monto * TasaPeriodo) / denominador
        BigDecimal cuota = monto.multiply(tasaPeriodo).divide(denominador, 2, RoundingMode.HALF_UP);
        
        return cuota;
    }
    
    private Integer crearCredito(Connection connection, String cedula, BigDecimal monto, Integer plazo, BigDecimal cuota) throws SQLException {
        String sql = "INSERT INTO CREDITO (cedula, montoSolicitado, montoAprobado, plazoMeses, tasaAnual, cuotaFija, estado, fechaSolicitud, fechaAprobacion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, 'APROBADO', CURDATE(), CURDATE())";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cedula);
            stmt.setBigDecimal(2, monto);
            stmt.setBigDecimal(3, monto);
            stmt.setInt(4, plazo);
            stmt.setBigDecimal(5, TASA_ANUAL);
            stmt.setBigDecimal(6, cuota);
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return null;
    }
    
    private List<AmortizacionDTO> generarTablaAmortizacion(Integer idCredito, BigDecimal monto, Integer plazo, BigDecimal cuota) {
        List<AmortizacionDTO> tabla = new ArrayList<>();
        BigDecimal saldo = monto;
        // Usar la misma tasa periódica que en el cálculo de cuota
        BigDecimal tasaPeriodo = new BigDecimal("0.16").divide(new BigDecimal("12"), 10, RoundingMode.HALF_UP);
        
        for (int i = 1; i <= plazo; i++) {
            BigDecimal interesPagado = saldo.multiply(tasaPeriodo).setScale(2, RoundingMode.HALF_UP);
            BigDecimal capitalPagado = cuota.subtract(interesPagado);
            saldo = saldo.subtract(capitalPagado);
            
            // Para la última cuota, ajustar por redondeos
            if (i == plazo) {
                capitalPagado = capitalPagado.add(saldo);
                cuota = cuota.add(saldo);
                saldo = BigDecimal.ZERO;
            }
            
            tabla.add(new AmortizacionDTO(i, cuota, interesPagado, capitalPagado, saldo));
        }
        
        return tabla;
    }
    
    private void guardarTablaAmortizacion(Connection connection, Integer idCredito, List<AmortizacionDTO> tabla) throws SQLException {
        String sql = "INSERT INTO AMORTIZACION (idCredito, numeroCuota, valorCuota, interesPagado, capitalPagado, saldo) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (AmortizacionDTO dto : tabla) {
                stmt.setInt(1, idCredito);
                stmt.setInt(2, dto.getNumeroCuota());
                stmt.setBigDecimal(3, dto.getValorCuota());
                stmt.setBigDecimal(4, dto.getInteresPagado());
                stmt.setBigDecimal(5, dto.getCapitalPagado());
                stmt.setBigDecimal(6, dto.getSaldo());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
}
