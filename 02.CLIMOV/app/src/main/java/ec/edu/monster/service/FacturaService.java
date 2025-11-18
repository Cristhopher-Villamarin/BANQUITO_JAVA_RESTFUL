package ec.edu.monster.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import ec.edu.monster.db.HttpClientUtil;
import ec.edu.monster.model.Factura;
import ec.edu.monster.model.FacturaConDetalles;

public class FacturaService {

    private final Gson gson = new Gson();

    public List<Factura> listarFacturasPorCedula(String cedula) throws IOException {
        String jsonResponse = HttpClientUtil.get("/facturas/cliente/" + cedula, String.class);
        Type listType = new TypeToken<List<Factura>>(){}.getType();
        return gson.fromJson(jsonResponse, listType);
    }

    public FacturaConDetalles obtenerFacturaConDetalles(Integer idFactura) throws IOException {
        return HttpClientUtil.get("/facturas/" + idFactura, FacturaConDetalles.class);
    }
}
