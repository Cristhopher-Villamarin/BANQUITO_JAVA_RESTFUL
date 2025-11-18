package ec.edu.monster.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.db.HttpClientUtil;
import ec.edu.monster.model.DetalleFactura;
import ec.edu.monster.model.Factura;
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

    public List<Factura> listarFacturasPorCedula(String cedula) throws IOException, ParseException {
        String jsonResponse = HttpClientUtil.get("/facturas/cliente/" + cedula, String.class);

        // Mapear manualmente idFactura -> id del modelo Factura del cliente de consola
        com.google.gson.JsonArray array = gson.fromJson(jsonResponse, com.google.gson.JsonArray.class);
        if (array == null) {
            return List.of();
        }

        java.util.List<Factura> resultado = new java.util.ArrayList<>();
        for (JsonElement element : array) {
            if (!element.isJsonObject()) continue;
            JsonObject obj = element.getAsJsonObject();

            Factura factura = gson.fromJson(obj, Factura.class);
            if (obj.has("idFactura") && !obj.get("idFactura").isJsonNull()) {
                factura.setId(obj.get("idFactura").getAsInt());
            }
            resultado.add(factura);
        }
        return resultado;
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

    public List<DetalleFactura> listarDetallesPorFactura(int idFactura) throws IOException, ParseException {
        String jsonResponse = HttpClientUtil.get("/facturas/" + idFactura, String.class);
        JsonObject root = gson.fromJson(jsonResponse, JsonObject.class);
        if (root == null) {
            return List.of();
        }
        JsonElement detallesElement = root.get("detalles");
        if (detallesElement == null || detallesElement.isJsonNull()) {
            return List.of();
        }
        Type listType = new TypeToken<List<DetalleFactura>>(){}.getType();
        return gson.fromJson(detallesElement, listType);
    }
}
