package ec.edu.monster.dto;

import java.math.BigDecimal;

public class EvaluacionCreditoRequest {
    
    private String cedula;
    private BigDecimal montoElectrodomestico;
    private Integer plazoMeses;
    
    public EvaluacionCreditoRequest() {}
    
    public EvaluacionCreditoRequest(String cedula, BigDecimal montoElectrodomestico, Integer plazoMeses) {
        this.cedula = cedula;
        this.montoElectrodomestico = montoElectrodomestico;
        this.plazoMeses = plazoMeses;
    }
    
    public String getCedula() {
        return cedula;
    }
    
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    public BigDecimal getMontoElectrodomestico() {
        return montoElectrodomestico;
    }
    
    public void setMontoElectrodomestico(BigDecimal montoElectrodomestico) {
        this.montoElectrodomestico = montoElectrodomestico;
    }
    
    public Integer getPlazoMeses() {
        return plazoMeses;
    }
    
    public void setPlazoMeses(Integer plazoMeses) {
        this.plazoMeses = plazoMeses;
    }
    
    @Override
    public String toString() {
        return "EvaluacionCreditoRequest{" + "cedula=" + cedula + ", montoElectrodomestico=" + montoElectrodomestico + ", plazoMeses=" + plazoMeses + '}';
    }
}
