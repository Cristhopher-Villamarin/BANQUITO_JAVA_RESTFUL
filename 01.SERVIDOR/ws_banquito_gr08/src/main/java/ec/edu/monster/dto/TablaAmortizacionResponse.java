package ec.edu.monster.dto;

import java.math.BigDecimal;
import java.util.List;

public class TablaAmortizacionResponse {
    private Integer idFactura;
    private String nombreCliente;
    private String cedulaCliente;
    private BigDecimal montoTotal;
    private BigDecimal tasaInteres;
    private Integer numeroCuotas;
    private BigDecimal cuotaMensual;
    private List<CuotaAmortizacionResponse> cuotas;

    public TablaAmortizacionResponse() {}

    public TablaAmortizacionResponse(Integer idFactura, String nombreCliente, String cedulaCliente, 
                                   BigDecimal montoTotal, BigDecimal tasaInteres, Integer numeroCuotas, 
                                   BigDecimal cuotaMensual, List<CuotaAmortizacionResponse> cuotas) {
        this.idFactura = idFactura;
        this.nombreCliente = nombreCliente;
        this.cedulaCliente = cedulaCliente;
        this.montoTotal = montoTotal;
        this.tasaInteres = tasaInteres;
        this.numeroCuotas = numeroCuotas;
        this.cuotaMensual = cuotaMensual;
        this.cuotas = cuotas;
    }

    public Integer getIdFactura() { return idFactura; }
    public void setIdFactura(Integer idFactura) { this.idFactura = idFactura; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getCedulaCliente() { return cedulaCliente; }
    public void setCedulaCliente(String cedulaCliente) { this.cedulaCliente = cedulaCliente; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }

    public BigDecimal getTasaInteres() { return tasaInteres; }
    public void setTasaInteres(BigDecimal tasaInteres) { this.tasaInteres = tasaInteres; }

    public Integer getNumeroCuotas() { return numeroCuotas; }
    public void setNumeroCuotas(Integer numeroCuotas) { this.numeroCuotas = numeroCuotas; }

    public BigDecimal getCuotaMensual() { return cuotaMensual; }
    public void setCuotaMensual(BigDecimal cuotaMensual) { this.cuotaMensual = cuotaMensual; }

    public List<CuotaAmortizacionResponse> getCuotas() { return cuotas; }
    public void setCuotas(List<CuotaAmortizacionResponse> cuotas) { this.cuotas = cuotas; }
}
