package ec.edu.monster.dto;

public class DetalleVentaRequest {
    
    private String idElectrodomestico;
    private String cantidad;
    
    public DetalleVentaRequest() {}
    
    public DetalleVentaRequest(String idElectrodomestico, String cantidad) {
        this.idElectrodomestico = idElectrodomestico;
        this.cantidad = cantidad;
    }
    
    public String getIdElectrodomestico() {
        return idElectrodomestico;
    }
    
    public void setIdElectrodomestico(String idElectrodomestico) {
        this.idElectrodomestico = idElectrodomestico;
    }
    
    public String getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
    
    @Override
    public String toString() {
        return "DetalleVentaRequest{" + "idElectrodomestico=" + idElectrodomestico + ", cantidad=" + cantidad + '}';
    }
}
