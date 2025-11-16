package ec.edu.monster.dto;

import java.math.BigDecimal;

public class CreditoResponse {
    private Integer idCredito;
    private String cedula;
    private BigDecimal montoSolicitado;
    private BigDecimal montoAprobado;
    private Integer plazoMeses;
    private BigDecimal tasaAnual;
    private BigDecimal cuotaFija;
    private String estado;
    private String fechaSolicitud;
    private String fechaAprobacion;

    public CreditoResponse() {}

    public CreditoResponse(Integer idCredito, String cedula, BigDecimal montoSolicitado, 
                           BigDecimal montoAprobado, Integer plazoMeses, BigDecimal tasaAnual, 
                           BigDecimal cuotaFija, String estado, String fechaSolicitud, String fechaAprobacion) {
        this.idCredito = idCredito;
        this.cedula = cedula;
        this.montoSolicitado = montoSolicitado;
        this.montoAprobado = montoAprobado;
        this.plazoMeses = plazoMeses;
        this.tasaAnual = tasaAnual;
        this.cuotaFija = cuotaFija;
        this.estado = estado;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaAprobacion = fechaAprobacion;
    }

    // Getters and Setters
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

    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(String fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }
}
