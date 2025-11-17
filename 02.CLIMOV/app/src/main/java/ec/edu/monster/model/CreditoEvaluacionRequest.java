package ec.edu.monster.model;

import java.math.BigDecimal;

public class CreditoEvaluacionRequest {
    private String cedula;
    private BigDecimal montoElectrodomestico;
    private Integer plazoMeses;

    public CreditoEvaluacionRequest() {}

    public CreditoEvaluacionRequest(String cedula, BigDecimal montoElectrodomestico, Integer plazoMeses) {
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
}

