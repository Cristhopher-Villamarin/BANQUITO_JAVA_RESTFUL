package ec.edu.monster.model;

public class DetalleVentaRequest {
    private Integer idElectrodomestico;
    private Integer cantidad;

    public DetalleVentaRequest() {}

    public DetalleVentaRequest(Integer idElectrodomestico, Integer cantidad) {
        this.idElectrodomestico = idElectrodomestico;
        this.cantidad = cantidad;
    }

    public Integer getIdElectrodomestico() {
        return idElectrodomestico;
    }

    public void setIdElectrodomestico(Integer idElectrodomestico) {
        this.idElectrodomestico = idElectrodomestico;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}

