package ec.edu.monster.dto;

import java.util.List;

public class VentaRequest {
    
    private String cedulaCliente;
    private String nombreCliente;
    private String formaPago;
    private Integer plazoMeses;
    private List<DetalleVentaRequest> detalles;
    
    public VentaRequest() {}
    
    public String getCedulaCliente() {
        return cedulaCliente;
    }
    
    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }
    
    public String getNombreCliente() {
        return nombreCliente;
    }
    
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
    
    public String getFormaPago() {
        return formaPago;
    }
    
    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }
    
    public Integer getPlazoMeses() {
        return plazoMeses;
    }
    
    public void setPlazoMeses(Integer plazoMeses) {
        this.plazoMeses = plazoMeses;
    }
    
    public List<DetalleVentaRequest> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<DetalleVentaRequest> detalles) {
        this.detalles = detalles;
    }
    
    @Override
    public String toString() {
        return "VentaRequest{" + "cedulaCliente=" + cedulaCliente + ", nombreCliente=" + nombreCliente + ", formaPago=" + formaPago + ", detalles=" + detalles + '}';
    }
}
