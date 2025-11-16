package ec.edu.monster.model;

import java.math.BigDecimal;

public class ElectrodomesticoRequest {
    private String nombre;
    private BigDecimal precioVenta;

    public ElectrodomesticoRequest() {}

    public ElectrodomesticoRequest(String nombre, BigDecimal precioVenta) {
        this.nombre = nombre;
        this.precioVenta = precioVenta;
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
}
