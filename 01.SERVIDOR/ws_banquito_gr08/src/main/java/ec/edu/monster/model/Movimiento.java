package ec.edu.monster.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "MOVIMIENTO")
public class Movimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigoMovimiento")
    private Integer codigoMovimiento;
    
    @Column(name = "numCuenta", nullable = false, length = 8)
    private String numCuenta;
    
    @Column(name = "tipo", nullable = false, length = 3)
    private String tipo;
    
    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;
    
    public Movimiento() {}
    
    public Movimiento(String numCuenta, String tipo, BigDecimal valor, LocalDate fecha) {
        this.numCuenta = numCuenta;
        this.tipo = tipo;
        this.valor = valor;
        this.fecha = fecha;
    }
    
    public Integer getCodigoMovimiento() {
        return codigoMovimiento;
    }
    
    public void setCodigoMovimiento(Integer codigoMovimiento) {
        this.codigoMovimiento = codigoMovimiento;
    }
    
    public String getNumCuenta() {
        return numCuenta;
    }
    
    public void setNumCuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public LocalDate getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    
    public boolean esDeposito() {
        return "DEP".equals(tipo);
    }
    
    public boolean esRetiro() {
        return "RET".equals(tipo);
    }
    
    @Override
    public String toString() {
        return "Movimiento{" + "codigoMovimiento=" + codigoMovimiento + ", numCuenta=" + numCuenta + ", tipo=" + tipo + ", valor=" + valor + ", fecha=" + fecha + '}';
    }
}
