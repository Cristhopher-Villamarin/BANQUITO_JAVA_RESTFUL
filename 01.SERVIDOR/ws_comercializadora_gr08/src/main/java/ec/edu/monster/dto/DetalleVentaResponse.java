package ec.edu.monster.dto;

import java.math.BigDecimal;

public class DetalleVentaResponse {
    
    private String idElectrodomestico;
    private String nombreElectrodomestico;
    private String cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotalLinea;
    
    public DetalleVentaResponse() {}
    
    public DetalleVentaResponse(String idElectrodomestico, String nombreElectrodomestico, String cantidad, BigDecimal precioUnitario, BigDecimal subtotalLinea) {
        this.idElectrodomestico = idElectrodomestico;
        this.nombreElectrodomestico = nombreElectrodomestico;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotalLinea = subtotalLinea;
    }
    
    public String getIdElectrodomestico() {
        return idElectrodomestico;
    }
    
    public void setIdElectrodomestico(String idElectrodomestico) {
        this.idElectrodomestico = idElectrodomestico;
    }
    
    public String getNombreElectrodomestico() {
        return nombreElectrodomestico;
    }
    
    public void setNombreElectrodomestico(String nombreElectrodomestico) {
        this.nombreElectrodomestico = nombreElectrodomestico;
    }
    
    public String getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
    
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    
    public BigDecimal getSubtotalLinea() {
        return subtotalLinea;
    }
    
    public void setSubtotalLinea(BigDecimal subtotalLinea) {
        this.subtotalLinea = subtotalLinea;
    }
    
    @Override
    public String toString() {
        return "DetalleVentaResponse{" + "idElectrodomestico=" + idElectrodomestico + ", nombreElectrodomestico=" + nombreElectrodomestico + ", cantidad=" + cantidad + ", precioUnitario=" + precioUnitario + ", subtotalLinea=" + subtotalLinea + '}';
    }
}
