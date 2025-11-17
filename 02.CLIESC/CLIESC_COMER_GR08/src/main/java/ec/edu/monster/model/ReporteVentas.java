package ec.edu.monster.model;

import java.math.BigDecimal;

public class ReporteVentas {
    private Integer totalFacturas;
    private BigDecimal totalVentas;
    private BigDecimal totalDescuentos;
    private Integer ventasEfectivo;
    private Integer ventasCredito;
    private BigDecimal promedioVenta;
    private String productoMasVendido;
    private Integer cantidadProductoMasVendido;

    public ReporteVentas() {}

    public Integer getTotalFacturas() {
        return totalFacturas;
    }

    public void setTotalFacturas(Integer totalFacturas) {
        this.totalFacturas = totalFacturas;
    }

    public BigDecimal getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(BigDecimal totalVentas) {
        this.totalVentas = totalVentas;
    }

    public BigDecimal getTotalDescuentos() {
        return totalDescuentos;
    }

    public void setTotalDescuentos(BigDecimal totalDescuentos) {
        this.totalDescuentos = totalDescuentos;
    }

    public Integer getVentasEfectivo() {
        return ventasEfectivo;
    }

    public void setVentasEfectivo(Integer ventasEfectivo) {
        this.ventasEfectivo = ventasEfectivo;
    }

    public Integer getVentasCredito() {
        return ventasCredito;
    }

    public void setVentasCredito(Integer ventasCredito) {
        this.ventasCredito = ventasCredito;
    }

    public BigDecimal getPromedioVenta() {
        return promedioVenta;
    }

    public void setPromedioVenta(BigDecimal promedioVenta) {
        this.promedioVenta = promedioVenta;
    }

    public String getProductoMasVendido() {
        return productoMasVendido;
    }

    public void setProductoMasVendido(String productoMasVendido) {
        this.productoMasVendido = productoMasVendido;
    }

    public Integer getCantidadProductoMasVendido() {
        return cantidadProductoMasVendido;
    }

    public void setCantidadProductoMasVendido(Integer cantidadProductoMasVendido) {
        this.cantidadProductoMasVendido = cantidadProductoMasVendido;
    }
}
