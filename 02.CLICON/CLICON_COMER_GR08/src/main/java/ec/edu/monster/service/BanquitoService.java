package ec.edu.monster.service;

import ec.edu.monster.db.HttpClientUtil;
import ec.edu.monster.model.Credito;
import ec.edu.monster.model.CreditoEvaluacionRequest;
import ec.edu.monster.model.CreditoEvaluacionResponse;
import ec.edu.monster.model.TablaAmortizacion;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.reflect.TypeToken;

public class BanquitoService {
    
    private static final String BANQUITO_BASE_URL = "http://localhost:8080/ws_banquito_gr08/api";
    private static final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .setDateFormat("yyyy-MM-dd")
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .setLenient()
            .create();
    
    public CreditoEvaluacionResponse evaluarCredito(CreditoEvaluacionRequest request) throws IOException, ParseException {
        // Guardar la URL base actual
        String originalBaseUrl = HttpClientUtil.getBaseUrl();
        String originalToken = HttpClientUtil.getAuthToken();
        
        try {
            // Cambiar temporalmente a la URL del Banquito y limpiar token
            HttpClientUtil.setBaseUrl(BANQUITO_BASE_URL);
            HttpClientUtil.clearAuthToken();
            
            // Hacer la petición
            return HttpClientUtil.post("/creditos/evaluar", request, CreditoEvaluacionResponse.class);
            
        } finally {
            // Restaurar la URL base y token originales
            HttpClientUtil.setBaseUrl(originalBaseUrl);
            if (originalToken != null) {
                HttpClientUtil.setAuthToken(originalToken);
            }
        }
    }
    
    public List<Credito> obtenerCreditosActivos(String cedula) throws IOException, ParseException {
        // Guardar la URL base actual
        String originalBaseUrl = HttpClientUtil.getBaseUrl();
        String originalToken = HttpClientUtil.getAuthToken();
        
        try {
            // Cambiar temporalmente a la URL del Banquito y limpiar token
            HttpClientUtil.setBaseUrl(BANQUITO_BASE_URL);
            HttpClientUtil.clearAuthToken();
            
            // Hacer la petición GET como String
            String jsonResponse = HttpClientUtil.get("/creditos/cliente/" + cedula + "/creditos-activos", String.class);
            
            // Deserializar manualmente usando Gson
            Type listType = new TypeToken<List<Credito>>(){}.getType();
            return gson.fromJson(jsonResponse, listType);
            
        } finally {
            // Restaurar la URL base y token originales
            HttpClientUtil.setBaseUrl(originalBaseUrl);
            if (originalToken != null) {
                HttpClientUtil.setAuthToken(originalToken);
            }
        }
    }
    
    public CreditoEvaluacionResponse verificarSujetoCredito(String cedula) throws IOException, ParseException {
        // Guardar la URL base actual
        String originalBaseUrl = HttpClientUtil.getBaseUrl();
        String originalToken = HttpClientUtil.getAuthToken();
        
        try {
            // Cambiar temporalmente a la URL del Banquito y limpiar token
            HttpClientUtil.setBaseUrl(BANQUITO_BASE_URL);
            HttpClientUtil.clearAuthToken();
            
            // Hacer la petición
            return HttpClientUtil.get("/creditos/cliente/" + cedula + "/sujeto-credito", CreditoEvaluacionResponse.class);
            
        } finally {
            // Restaurar la URL base y token originales
            HttpClientUtil.setBaseUrl(originalBaseUrl);
            if (originalToken != null) {
                HttpClientUtil.setAuthToken(originalToken);
            }
        }
    }
    
    public TablaAmortizacion obtenerTablaAmortizacion(Integer idCredito) throws IOException, ParseException {
        // Guardar la URL base actual
        String originalBaseUrl = HttpClientUtil.getBaseUrl();
        String originalToken = HttpClientUtil.getAuthToken();
        
        try {
            // Cambiar temporalmente a la URL del Banquito y limpiar token
            HttpClientUtil.setBaseUrl(BANQUITO_BASE_URL);
            HttpClientUtil.clearAuthToken();
            
            // Hacer la petición
            return HttpClientUtil.get("/creditos/amortizacion/" + idCredito, TablaAmortizacion.class);
            
        } finally {
            // Restaurar la URL base y token originales
            HttpClientUtil.setBaseUrl(originalBaseUrl);
            if (originalToken != null) {
                HttpClientUtil.setAuthToken(originalToken);
            }
        }
    }
}
