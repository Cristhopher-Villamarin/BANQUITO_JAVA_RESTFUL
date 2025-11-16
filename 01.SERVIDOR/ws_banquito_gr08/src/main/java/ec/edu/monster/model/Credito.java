package ec.edu.monster.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "CREDITO")
public class Credito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCredito")
    private Integer idCredito;
    
    @Column(name = "cedula", nullable = false, length = 10)
    private String cedula;
    
    @Column(name = "montoSolicitado", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoSolicitado;
    
    @Column(name = "montoAprobado", precision = 12, scale = 2)
    private BigDecimal montoAprobado;
    
    @Column(name = "plazoMeses", nullable = false)
    private Integer plazoMeses;
    
    @Column(name = "tasaAnual", nullable = false, precision = 5, scale = 2)
    private BigDecimal tasaAnual;
    
    @Column(name = "cuotaFija", nullable = false, precision = 12, scale = 2)
    private BigDecimal cuotaFija;
    
    @Column(name = "estado", nullable = false, length = 15)
    private String estado;
    
    @Column(name = "fechaSolicitud", nullable = false)
    private LocalDate fechaSolicitud;
    
    @Column(name = "fechaAprobacion")
    private LocalDate fechaAprobacion;
    
    public Credito() {}
    
    public Credito(String cedula, BigDecimal montoSolicitado, Integer plazoMeses) {
        this.cedula = cedula;
        this.montoSolicitado = montoSolicitado;
        this.plazoMeses = plazoMeses;
        this.tasaAnual = new BigDecimal("16.00");
        this.estado = "ACTIVO";
        this.fechaSolicitud = LocalDate.now();
    }
    
    public Integer getIdCredito() {
        return idCredito;
    }
    
    public void setIdCredito(Integer idCredito) {
        this.idCredito = idCredito;
    }
    
    public String getCedula() {
        return cedula;
    }
    
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    public BigDecimal getMontoSolicitado() {
        return montoSolicitado;
    }
    
    public void setMontoSolicitado(BigDecimal montoSolicitado) {
        this.montoSolicitado = montoSolicitado;
    }
    
    public BigDecimal getMontoAprobado() {
        return montoAprobado;
    }
    
    public void setMontoAprobado(BigDecimal montoAprobado) {
        this.montoAprobado = montoAprobado;
    }
    
    public Integer getPlazoMeses() {
        return plazoMeses;
    }
    
    public void setPlazoMeses(Integer plazoMeses) {
        this.plazoMeses = plazoMeses;
    }
    
    public BigDecimal getTasaAnual() {
        return tasaAnual;
    }
    
    public void setTasaAnual(BigDecimal tasaAnual) {
        this.tasaAnual = tasaAnual;
    }
    
    public BigDecimal getCuotaFija() {
        return cuotaFija;
    }
    
    public void setCuotaFija(BigDecimal cuotaFija) {
        this.cuotaFija = cuotaFija;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }
    
    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }
    
    public LocalDate getFechaAprobacion() {
        return fechaAprobacion;
    }
    
    public void setFechaAprobacion(LocalDate fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }
    
    public boolean estaActivo() {
        return "ACTIVO".equals(estado);
    }
    
    public void activar() {
        this.estado = "ACTIVO";
    }
    
    
    @Override
    public String toString() {
        return "Credito{" + "idCredito=" + idCredito + ", cedula=" + cedula + ", montoSolicitado=" + montoSolicitado + ", montoAprobado=" + montoAprobado + ", plazoMeses=" + plazoMeses + ", tasaAnual=" + tasaAnual + ", cuotaFija=" + cuotaFija + ", estado=" + estado + ", fechaSolicitud=" + fechaSolicitud + ", fechaAprobacion=" + fechaAprobacion + '}';
    }
}
