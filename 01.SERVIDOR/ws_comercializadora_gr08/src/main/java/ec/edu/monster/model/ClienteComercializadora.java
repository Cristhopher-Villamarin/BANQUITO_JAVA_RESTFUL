package ec.edu.monster.model;

import jakarta.persistence.*;

@Entity
@Table(name = "CLIENTE")
public class ClienteComercializadora {
    
    @Id
    @Column(name = "cedula", length = 10)
    private String cedula;
    
    @Column(name = "nombre", length = 100)
    private String nombre;
    
    public ClienteComercializadora() {}
    
    public ClienteComercializadora(String cedula, String nombre) {
        this.cedula = cedula;
        this.nombre = nombre;
    }
    
    public String getCedula() {
        return cedula;
    }
    
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public String toString() {
        return "ClienteComercializadora{" + "cedula=" + cedula + ", nombre=" + nombre + '}';
    }
}
