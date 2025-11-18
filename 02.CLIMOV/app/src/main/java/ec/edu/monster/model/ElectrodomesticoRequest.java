package ec.edu.monster.model;

import java.math.BigDecimal;

public class ElectrodomesticoRequest {
    private String nombre;
    private BigDecimal precioVenta;
    private String fotoUrl;

    public ElectrodomesticoRequest() {}

    public ElectrodomesticoRequest(String nombre, BigDecimal precioVenta, String fotoUrl) {
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.fotoUrl = fotoUrl;
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

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}

