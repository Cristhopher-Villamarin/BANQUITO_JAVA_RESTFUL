package ec.edu.monster.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ElectrodomesticoRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
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
    
    @Override
    public String toString() {
        return "ElectrodomesticoRequest{" + "nombre='" + nombre + '\'' + ", precioVenta=" + precioVenta + '}';
    }
}
