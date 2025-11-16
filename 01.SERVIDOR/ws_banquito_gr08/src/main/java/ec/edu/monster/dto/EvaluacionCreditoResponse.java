package ec.edu.monster.dto;

import java.math.BigDecimal;
import java.util.List;

public class EvaluacionCreditoResponse {
    
    private Boolean sujetoCredito;
    private Boolean creditoAprobado;
    private BigDecimal montoMaximoCredito;
    private String mensaje;
    private Integer idCredito;
    private BigDecimal cuotaMensual;
    private List<AmortizacionDTO> tablaAmortizacion;
    
    public EvaluacionCreditoResponse() {}
    
    public Boolean isSujetoCredito() {
        return sujetoCredito;
    }
    
    public void setSujetoCredito(Boolean sujetoCredito) {
        this.sujetoCredito = sujetoCredito;
    }
    
    public Boolean isCreditoAprobado() {
        return creditoAprobado;
    }
    
    public void setCreditoAprobado(Boolean creditoAprobado) {
        this.creditoAprobado = creditoAprobado;
    }
    
    public BigDecimal getMontoMaximoCredito() {
        return montoMaximoCredito;
    }
    
    public void setMontoMaximoCredito(BigDecimal montoMaximoCredito) {
        this.montoMaximoCredito = montoMaximoCredito;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public Integer getIdCredito() {
        return idCredito;
    }
    
    public void setIdCredito(Integer idCredito) {
        this.idCredito = idCredito;
    }
    
    public BigDecimal getCuotaMensual() {
        return cuotaMensual;
    }
    
    public void setCuotaMensual(BigDecimal cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
    }
    
    public List<AmortizacionDTO> getTablaAmortizacion() {
        return tablaAmortizacion;
    }
    
    public void setTablaAmortizacion(List<AmortizacionDTO> tablaAmortizacion) {
        this.tablaAmortizacion = tablaAmortizacion;
    }
    
    @Override
    public String toString() {
        return "EvaluacionCreditoResponse{" + "sujetoCredito=" + sujetoCredito + ", creditoAprobado=" + creditoAprobado + ", montoMaximoCredito=" + montoMaximoCredito + ", mensaje=" + mensaje + ", idCredito=" + idCredito + ", cuotaMensual=" + cuotaMensual + '}';
    }
}
