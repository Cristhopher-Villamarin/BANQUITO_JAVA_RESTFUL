package ec.edu.monster.dto;

import java.math.BigDecimal;

public class ElectrodomesticoResponse {
    
    private Integer idElectrodomestico;
    private String nombre;
    private BigDecimal precioVenta;
    private String fotoUrl;
    
    public ElectrodomesticoResponse() {}
    
    public ElectrodomesticoResponse(Integer idElectrodomestico, String nombre, BigDecimal precioVenta, String fotoUrl) {
        this.idElectrodomestico = idElectrodomestico;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.fotoUrl = fotoUrl;
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
    
    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
    
    @Override
    public String toString() {
        return "ElectrodomesticoResponse{" + "idElectrodomestico=" + idElectrodomestico + ", nombre='" + nombre + '\'' + ", precioVenta=" + precioVenta + '}';
    }
}
