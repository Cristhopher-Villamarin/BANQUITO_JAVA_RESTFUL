package ec.edu.monster.model;

import java.math.BigDecimal;

public class Electrodomestico {
    private Integer idElectrodomestico;
    private String nombre;
    private BigDecimal precioVenta;

    public Electrodomestico() {}

    public Electrodomestico(Integer idElectrodomestico, String nombre, BigDecimal precioVenta) {
        this.idElectrodomestico = idElectrodomestico;
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
        return String.format("ID: %-3d | %-40s | $%.2f", idElectrodomestico, nombre, precioVenta);
    }
}
