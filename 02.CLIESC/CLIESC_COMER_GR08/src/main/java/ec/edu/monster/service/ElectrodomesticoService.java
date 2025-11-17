package ec.edu.monster.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.db.HttpClientUtil;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.model.ElectrodomesticoRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ElectrodomesticoService {
    private final Gson gson = new Gson();
    
    public List<Electrodomestico> listarElectrodomesticos() throws IOException, ParseException {
        String jsonResponse = HttpClientUtil.get("/electrodomesticos", String.class);
        Type listType = new TypeToken<List<Electrodomestico>>(){}.getType();
        return gson.fromJson(jsonResponse, listType);
    }
    
    public Electrodomestico obtenerPorId(int id) throws IOException, ParseException {
        return HttpClientUtil.get("/electrodomesticos/" + id, Electrodomestico.class);
    }
    
    public Electrodomestico crear(String nombre, double precio) throws IOException, ParseException {
        ElectrodomesticoRequest request = new ElectrodomesticoRequest(
            nombre, 
            java.math.BigDecimal.valueOf(precio)
        );
        return HttpClientUtil.post("/electrodomesticos", request, Electrodomestico.class);
    }
    
    public Electrodomestico actualizar(int id, String nombre, double precio) throws IOException, ParseException {
        ElectrodomesticoRequest request = new ElectrodomesticoRequest(
            nombre, 
            java.math.BigDecimal.valueOf(precio)
        );
        return HttpClientUtil.put("/electrodomesticos/" + id, request, Electrodomestico.class);
    }
    
    public String eliminar(int id) throws IOException, ParseException {
        return HttpClientUtil.delete("/electrodomesticos/" + id);
    }
}
