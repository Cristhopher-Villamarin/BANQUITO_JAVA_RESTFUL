package ec.edu.monster.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "DETALLE_FACTURA")
public class DetalleFactura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDetalle")
    private Integer idDetalle;
    
    @Column(name = "idFactura", nullable = false)
    private Integer idFactura;
    
    @Column(name = "idElectrodomestico", nullable = false)
    private Integer idElectrodomestico;
    
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
    
    @Column(name = "precioUnitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;
    
    @Column(name = "subtotalLinea", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotalLinea;
    
    public DetalleFactura() {}
    
    public DetalleFactura(Integer idFactura, Integer idElectrodomestico, Integer cantidad, BigDecimal precioUnitario, BigDecimal subtotalLinea) {
        this.idFactura = idFactura;
        this.idElectrodomestico = idElectrodomestico;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotalLinea = subtotalLinea;
    }
    
    public Integer getIdDetalle() {
        return idDetalle;
    }
    
    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }
    
    public Integer getIdFactura() {
        return idFactura;
    }
    
    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }
    
    public Integer getIdElectrodomestico() {
        return idElectrodomestico;
    }
    
    public void setIdElectrodomestico(Integer idElectrodomestico) {
        this.idElectrodomestico = idElectrodomestico;
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
    
    public BigDecimal getSubtotalLinea() {
        return subtotalLinea;
    }
    
    public void setSubtotalLinea(BigDecimal subtotalLinea) {
        this.subtotalLinea = subtotalLinea;
    }
    
    @Override
    public String toString() {
        return "DetalleFactura{" + "idDetalle=" + idDetalle + ", idFactura=" + idFactura + ", idElectrodomestico=" + idElectrodomestico + ", cantidad=" + cantidad + ", precioUnitario=" + precioUnitario + ", subtotalLinea=" + subtotalLinea + '}';
    }
}
