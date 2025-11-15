package ec.edu.monster.dto;

import java.math.BigDecimal;
import java.util.List;

public class VentaResponse {
    
    private boolean ventaExitosa;
    private String mensaje;
    private Integer idFactura;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
    private String formaPago;
    private String estadoFactura;
    private List<DetalleVentaResponse> detalles;
    
    public VentaResponse() {}
    
    public boolean isVentaExitosa() {
        return ventaExitosa;
    }
    
    public void setVentaExitosa(boolean ventaExitosa) {
        this.ventaExitosa = ventaExitosa;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public Integer getIdFactura() {
        return idFactura;
    }
    
    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public BigDecimal getDescuento() {
        return descuento;
    }
    
    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    public String getFormaPago() {
        return formaPago;
    }
    
    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }
    
    public String getEstadoFactura() {
        return estadoFactura;
    }
    
    public void setEstadoFactura(String estadoFactura) {
        this.estadoFactura = estadoFactura;
    }
    
    public List<DetalleVentaResponse> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<DetalleVentaResponse> detalles) {
        this.detalles = detalles;
    }
    
    @Override
    public String toString() {
        return "VentaResponse{" + "ventaExitosa=" + ventaExitosa + ", mensaje=" + mensaje + ", idFactura=" + idFactura + ", subtotal=" + subtotal + ", descuento=" + descuento + ", total=" + total + ", formaPago=" + formaPago + ", estadoFactura=" + estadoFactura + '}';
    }
}
