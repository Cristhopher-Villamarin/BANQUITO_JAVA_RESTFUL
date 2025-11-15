package ec.edu.monster.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ELECTRODOMESTICO")
public class Electrodomestico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idElectrodomestico")
    private Integer idElectrodomestico;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "precioVenta", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioVenta;
    
    public Electrodomestico() {}
    
    public Electrodomestico(String nombre, BigDecimal precioVenta) {
        this.nombre = nombre;
        this.precioVenta = precioVenta;
    }
    
    public Integer getIdElectrodomestico() {
        return idElectrodomestico;
    }
    
    public void setIdElectrodomestico(Integer idElectrodomestico) {
        this.idElectrodomestico = idElectrodomestico;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }
    
    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }
    
    @Override
    public String toString() {
        return "Electrodomestico{" + "idElectrodomestico=" + idElectrodomestico + ", nombre=" + nombre + ", precioVenta=" + precioVenta + '}';
    }
}
