package ec.edu.monster.dto;

import java.math.BigDecimal;

public class CuotaAmortizacionResponse {
    private Integer numeroCuota;
    private BigDecimal capital;
    private BigDecimal interes;
    private BigDecimal cuota;
    private BigDecimal saldoFinal;

    public CuotaAmortizacionResponse() {}

    public CuotaAmortizacionResponse(Integer numeroCuota, BigDecimal capital, BigDecimal interes, 
                                   BigDecimal cuota, BigDecimal saldoFinal) {
        this.numeroCuota = numeroCuota;
        this.capital = capital;
        this.interes = interes;
        this.cuota = cuota;
        this.saldoFinal = saldoFinal;
    }

    public Integer getNumeroCuota() { return numeroCuota; }
    public void setNumeroCuota(Integer numeroCuota) { this.numeroCuota = numeroCuota; }

    public BigDecimal getCapital() { return capital; }
    public void setCapital(BigDecimal capital) { this.capital = capital; }

    public BigDecimal getInteres() { return interes; }
    public void setInteres(BigDecimal interes) { this.interes = interes; }

    public BigDecimal getCuota() { return cuota; }
    public void setCuota(BigDecimal cuota) { this.cuota = cuota; }

    public BigDecimal getSaldoFinal() { return saldoFinal; }
    public void setSaldoFinal(BigDecimal saldoFinal) { this.saldoFinal = saldoFinal; }
}
