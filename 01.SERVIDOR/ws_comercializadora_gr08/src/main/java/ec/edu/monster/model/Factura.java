package ec.edu.monster.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "FACTURA")
public class Factura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idFactura")
    private Integer idFactura;
    
    @Column(name = "cedula", length = 10)
    private String cedula;
    
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;
    
    @Column(name = "formaPago", nullable = false, length = 20)
    private String formaPago;
    
    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "descuento", precision = 12, scale = 2)
    private BigDecimal descuento;
    
    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;
    
    @Column(name = "estado", nullable = false, length = 15)
    private String estado;
    
    public Factura() {}
    
    public Factura(String cedula, String formaPago, BigDecimal subtotal, BigDecimal descuento, BigDecimal total, String estado) {
        this.cedula = cedula;
        this.fecha = LocalDate.now();
        this.formaPago = formaPago;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
        this.estado = estado;
    }
    
    public Integer getIdFactura() {
        return idFactura;
    }
    
    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }
    
    public String getCedula() {
        return cedula;
    }
    
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    public LocalDate getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    
    public String getFormaPago() {
        return formaPago;
    }
    
    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
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
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public boolean esEfectivo() {
        return "EFECTIVO".equalsIgnoreCase(formaPago);
    }
    
    public boolean esCreditoDirecto() {
        return "CREDITO_DIRECTO".equalsIgnoreCase(formaPago);
    }
    
    @Override
    public String toString() {
        return "Factura{" + "idFactura=" + idFactura + ", cedula=" + cedula + ", fecha=" + fecha + ", formaPago=" + formaPago + ", subtotal=" + subtotal + ", descuento=" + descuento + ", total=" + total + ", estado=" + estado + '}';
    }
}
