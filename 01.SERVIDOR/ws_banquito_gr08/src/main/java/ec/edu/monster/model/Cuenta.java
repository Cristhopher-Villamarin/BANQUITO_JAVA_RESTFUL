package ec.edu.monster.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "CUENTA")
public class Cuenta {
    
    @Id
    @Column(name = "numCuenta", length = 8)
    private String numCuenta;
    
    @Column(name = "cedula", nullable = false, length = 10)
    private String cedula;
    
    @Column(name = "saldo", nullable = false, precision = 12, scale = 2)
    private BigDecimal saldo;
    
    public Cuenta() {}
    
    public Cuenta(String numCuenta, String cedula, BigDecimal saldo) {
        this.numCuenta = numCuenta;
        this.cedula = cedula;
        this.saldo = saldo;
    }
    
    public String getNumCuenta() {
        return numCuenta;
    }
    
    public void setNumCuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }
    
    public String getCedula() {
        return cedula;
    }
    
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    public BigDecimal getSaldo() {
        return saldo;
    }
    
    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
    
    @Override
    public String toString() {
        return "Cuenta{" + "numCuenta=" + numCuenta + ", cedula=" + cedula + ", saldo=" + saldo + '}';
    }
}
