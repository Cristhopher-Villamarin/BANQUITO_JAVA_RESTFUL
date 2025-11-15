package ec.edu.monster.dto;

import java.math.BigDecimal;
import java.util.List;

public class EvaluacionCreditoResponse {
    
    private boolean sujetoCredito;
    private boolean creditoAprobado;
    private BigDecimal montoMaximoCredito;
    private String mensaje;
    private Integer idCredito;
    private BigDecimal cuotaMensual;
    private List<AmortizacionDTO> tablaAmortizacion;
    
    public EvaluacionCreditoResponse() {}
    
    public boolean isSujetoCredito() {
        return sujetoCredito;
    }
    
    public void setSujetoCredito(boolean sujetoCredito) {
        this.sujetoCredito = sujetoCredito;
    }
    
    public boolean isCreditoAprobado() {
        return creditoAprobado;
    }
    
    public void setCreditoAprobado(boolean creditoAprobado) {
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
