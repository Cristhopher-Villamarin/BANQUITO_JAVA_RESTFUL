package ec.edu.monster.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.db.HttpClientUtil;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.model.ElectrodomesticoRequest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

public class ElectrodomesticoService {
    private final Gson gson = new Gson();

    public List<Electrodomestico> listarElectrodomesticos() throws IOException {
        String jsonResponse = HttpClientUtil.get("/electrodomesticos", String.class);
        Type listType = new TypeToken<List<Electrodomestico>>(){}.getType();
        return gson.fromJson(jsonResponse, listType);
    }

    public Electrodomestico obtenerPorId(int id) throws IOException {
        return HttpClientUtil.get("/electrodomesticos/" + id, Electrodomestico.class);
    }

    public Electrodomestico crear(String nombre, double precio, String fotoUrl) throws IOException {
        ElectrodomesticoRequest request = new ElectrodomesticoRequest(
                nombre,
                BigDecimal.valueOf(precio),
                fotoUrl
        );
        return HttpClientUtil.post("/electrodomesticos", request, Electrodomestico.class);
    }

    public Electrodomestico actualizar(int id, String nombre, double precio, String fotoUrl) throws IOException {
        ElectrodomesticoRequest request = new ElectrodomesticoRequest(
                nombre,
                BigDecimal.valueOf(precio),
                fotoUrl
        );
        return HttpClientUtil.put("/electrodomesticos/" + id, request, Electrodomestico.class);
    }

    public boolean eliminar(int id) throws IOException {
        try {
            String response = HttpClientUtil.delete("/electrodomesticos/" + id);
            Log.d("ElectrodomesticoService", "Respuesta de eliminación: " + response);
            return response != null && (response.contains("eliminado") || response.contains("success") || response.trim().isEmpty());
        } catch (Exception e) {
            Log.e("ElectrodomesticoService", "Error al eliminar electrodoméstico", e);
            throw e;
        }
    }
}

