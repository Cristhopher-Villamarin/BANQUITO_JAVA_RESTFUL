package ec.edu.monster.model;

import java.math.BigDecimal;

public class DetalleFactura {
    private Integer id;
    private Integer idElectrodomestico;
    private String nombreElectrodomestico;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private BigDecimal subtotalLinea;

    public DetalleFactura() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdElectrodomestico() {
        return idElectrodomestico;
    }

    public void setIdElectrodomestico(Integer idElectrodomestico) {
        this.idElectrodomestico = idElectrodomestico;
    }

    public String getNombreElectrodomestico() {
        return nombreElectrodomestico;
    }

    public void setNombreElectrodomestico(String nombreElectrodomestico) {
        this.nombreElectrodomestico = nombreElectrodomestico;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal != null ? subtotal : subtotalLinea;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getSubtotalLinea() {
        return subtotalLinea;
    }

    public void setSubtotalLinea(BigDecimal subtotalLinea) {
        this.subtotalLinea = subtotalLinea;
        this.subtotal = subtotalLinea;
    }
}
