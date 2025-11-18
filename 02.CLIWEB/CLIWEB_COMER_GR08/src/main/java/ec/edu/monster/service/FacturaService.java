package ec.edu.monster.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.db.HttpClientUtil;
import ec.edu.monster.model.Factura;
import ec.edu.monster.model.FacturaDetalleResponse;
import ec.edu.monster.model.ReporteVentas;
import ec.edu.monster.model.TablaAmortizacion;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class FacturaService {
    private final Gson gson = new Gson();

    public List<Factura> listarFacturas() throws IOException, ParseException {
        String jsonResponse = HttpClientUtil.get("/facturas", String.class);
        Type listType = new TypeToken<List<Factura>>(){}.getType();
        return gson.fromJson(jsonResponse, listType);
    }

    public Factura obtenerFacturaPorId(int id) throws IOException, ParseException {
        return HttpClientUtil.get("/facturas/" + id, Factura.class);
    }

    public ReporteVentas obtenerReporteVentas() throws IOException, ParseException {
        return HttpClientUtil.get("/ventas/reporte", ReporteVentas.class);
    }

    public TablaAmortizacion obtenerTablaAmortizacion(int idFactura) throws IOException, ParseException {
        return HttpClientUtil.get("/creditos/" + idFactura + "/tabla-amortizacion", TablaAmortizacion.class);
    }

    public List<Factura> listarFacturasPorCedula(String cedula) throws IOException, ParseException {
        String jsonResponse = HttpClientUtil.get("/facturas/cliente/" + cedula, String.class);
        Type listType = new TypeToken<List<Factura>>(){}.getType();
        return gson.fromJson(jsonResponse, listType);
    }

    public FacturaDetalleResponse obtenerFacturaConDetalles(int idFactura) throws IOException, ParseException {
        String jsonResponse = HttpClientUtil.get("/facturas/" + idFactura, String.class);
        return gson.fromJson(jsonResponse, FacturaDetalleResponse.class);
    }
}
