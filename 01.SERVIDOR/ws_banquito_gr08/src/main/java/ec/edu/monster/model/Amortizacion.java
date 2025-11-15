package ec.edu.monster.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "AMORTIZACION")
public class Amortizacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAmortizacion")
    private Integer idAmortizacion;
    
    @Column(name = "idCredito", nullable = false)
    private Integer idCredito;
    
    @Column(name = "numeroCuota", nullable = false)
    private Integer numeroCuota;
    
    @Column(name = "valorCuota", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorCuota;
    
    @Column(name = "interesPagado", nullable = false, precision = 12, scale = 2)
    private BigDecimal interesPagado;
    
    @Column(name = "capitalPagado", nullable = false, precision = 12, scale = 2)
    private BigDecimal capitalPagado;
    
    @Column(name = "saldo", nullable = false, precision = 12, scale = 2)
    private BigDecimal saldo;
    
    public Amortizacion() {}
    
    public Amortizacion(Integer idCredito, Integer numeroCuota, BigDecimal valorCuota, 
                       BigDecimal interesPagado, BigDecimal capitalPagado, BigDecimal saldo) {
        this.idCredito = idCredito;
        this.numeroCuota = numeroCuota;
        this.valorCuota = valorCuota;
        this.interesPagado = interesPagado;
        this.capitalPagado = capitalPagado;
        this.saldo = saldo;
    }
    
    public Integer getIdAmortizacion() {
        return idAmortizacion;
    }
    
    public void setIdAmortizacion(Integer idAmortizacion) {
        this.idAmortizacion = idAmortizacion;
    }
    
    public Integer getIdCredito() {
        return idCredito;
    }
    
    public void setIdCredito(Integer idCredito) {
        this.idCredito = idCredito;
    }
    
    public Integer getNumeroCuota() {
        return numeroCuota;
    }
    
    public void setNumeroCuota(Integer numeroCuota) {
        this.numeroCuota = numeroCuota;
    }
    
    public BigDecimal getValorCuota() {
        return valorCuota;
    }
    
    public void setValorCuota(BigDecimal valorCuota) {
        this.valorCuota = valorCuota;
    }
    
    public BigDecimal getInteresPagado() {
        return interesPagado;
    }
    
    public void setInteresPagado(BigDecimal interesPagado) {
        this.interesPagado = interesPagado;
    }
    
    public BigDecimal getCapitalPagado() {
        return capitalPagado;
    }
    
    public void setCapitalPagado(BigDecimal capitalPagado) {
        this.capitalPagado = capitalPagado;
    }
    
    public BigDecimal getSaldo() {
        return saldo;
    }
    
    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
    
    @Override
    public String toString() {
        return "Amortizacion{" + "idAmortizacion=" + idAmortizacion + ", idCredito=" + idCredito + ", numeroCuota=" + numeroCuota + ", valorCuota=" + valorCuota + ", interesPagado=" + interesPagado + ", capitalPagado=" + capitalPagado + ", saldo=" + saldo + '}';
    }
}
