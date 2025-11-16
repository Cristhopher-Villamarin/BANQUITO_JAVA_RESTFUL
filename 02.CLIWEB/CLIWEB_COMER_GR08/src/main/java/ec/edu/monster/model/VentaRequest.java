package ec.edu.monster.model;

import java.util.List;

public class VentaRequest {
    private String cedulaCliente;
    private String nombreCliente;
    private String formaPago;
    private Integer plazoMeses;
    private List<DetalleVentaRequest> detalles;

    public VentaRequest() {}

    public VentaRequest(String cedulaCliente, String nombreCliente, String formaPago, Integer plazoMeses, List<DetalleVentaRequest> detalles) {
        this.cedulaCliente = cedulaCliente;
        this.nombreCliente = nombreCliente;
        this.formaPago = formaPago;
        this.plazoMeses = plazoMeses;
        this.detalles = detalles;
    }

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
}
