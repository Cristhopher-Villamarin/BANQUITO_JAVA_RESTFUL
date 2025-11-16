package ec.edu.monster.model;

import java.math.BigDecimal;
import java.util.List;

public class TablaAmortizacion {
    private Integer idFactura;
    private String nombreCliente;
    private String cedulaCliente;
    private BigDecimal montoTotal;
    private BigDecimal tasaInteres;
    private Integer numeroCuotas;
    private BigDecimal cuotaMensual;
    private List<CuotaAmortizacion> cuotas;

    public TablaAmortizacion() {}

    public Integer getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public BigDecimal getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(BigDecimal tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public Integer getNumeroCuotas() {
        return numeroCuotas;
    }

    public void setNumeroCuotas(Integer numeroCuotas) {
        this.numeroCuotas = numeroCuotas;
    }

    public BigDecimal getCuotaMensual() {
        return cuotaMensual;
    }

    public void setCuotaMensual(BigDecimal cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
    }

    public List<CuotaAmortizacion> getCuotas() {
        return cuotas;
    }

    public void setCuotas(List<CuotaAmortizacion> cuotas) {
        this.cuotas = cuotas;
    }
}
