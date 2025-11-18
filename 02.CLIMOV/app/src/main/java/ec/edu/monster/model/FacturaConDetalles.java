package ec.edu.monster.model;

import java.util.List;

public class FacturaConDetalles {

    private Factura factura;
    private List<DetalleFactura> detalles;

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles;
    }
}
