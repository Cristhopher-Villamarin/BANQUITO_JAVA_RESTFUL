package ec.edu.monster.model;

import java.math.BigDecimal;
import java.util.List;

public class CreditoEvaluacionResponse {
    private Boolean creditoAprobado;
    private Boolean sujetoCredito;
    private String mensaje;
    private BigDecimal montoMaximoCredito;
    private BigDecimal cuotaMensual;
    private Integer idCredito;
    private List<CuotaAmortizacion> tablaAmortizacion;

    public CreditoEvaluacionResponse() {}

    public Boolean getCreditoAprobado() {
        return creditoAprobado;
    }

    public void setCreditoAprobado(Boolean creditoAprobado) {
        this.creditoAprobado = creditoAprobado;
    }

    public Boolean getSujetoCredito() {
        return sujetoCredito;
    }

    public void setSujetoCredito(Boolean sujetoCredito) {
        this.sujetoCredito = sujetoCredito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public BigDecimal getMontoMaximoCredito() {
        return montoMaximoCredito;
    }

    public void setMontoMaximoCredito(BigDecimal montoMaximoCredito) {
        this.montoMaximoCredito = montoMaximoCredito;
    }

    public BigDecimal getCuotaMensual() {
        return cuotaMensual;
    }

    public void setCuotaMensual(BigDecimal cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
    }

    public Integer getIdCredito() {
        return idCredito;
    }

    public void setIdCredito(Integer idCredito) {
        this.idCredito = idCredito;
    }

    public List<CuotaAmortizacion> getTablaAmortizacion() {
        return tablaAmortizacion;
    }

    public void setTablaAmortizacion(List<CuotaAmortizacion> tablaAmortizacion) {
        this.tablaAmortizacion = tablaAmortizacion;
    }
}
