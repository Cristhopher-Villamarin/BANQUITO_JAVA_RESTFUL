package ec.edu.monster.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.db.HttpClientUtil;
import ec.edu.monster.model.Credito;
import ec.edu.monster.model.CreditoEvaluacionRequest;
import ec.edu.monster.model.CreditoEvaluacionResponse;
import ec.edu.monster.model.TablaAmortizacion;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class BanquitoService {

    private static final String BANQUITO_BASE_URL = "http://localhost:8080/ws_banquito_gr08/api";
    private static final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .setDateFormat("yyyy-MM-dd")
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .setLenient()
            .create();

    public CreditoEvaluacionResponse evaluarCredito(CreditoEvaluacionRequest request) throws IOException, ParseException {
        String originalBaseUrl = HttpClientUtil.getBaseUrl();
        String originalToken = HttpClientUtil.getAuthToken();

        try {
            HttpClientUtil.setBaseUrl(BANQUITO_BASE_URL);
            HttpClientUtil.clearAuthToken();

            return HttpClientUtil.post("/creditos/evaluar", request, CreditoEvaluacionResponse.class);

        } finally {
            HttpClientUtil.setBaseUrl(originalBaseUrl);
            if (originalToken != null) {
                HttpClientUtil.setAuthToken(originalToken);
            }
        }
    }

    public List<Credito> obtenerCreditosActivos(String cedula) throws IOException, ParseException {
        String originalBaseUrl = HttpClientUtil.getBaseUrl();
        String originalToken = HttpClientUtil.getAuthToken();

        try {
            HttpClientUtil.setBaseUrl(BANQUITO_BASE_URL);
            HttpClientUtil.clearAuthToken();

            String jsonResponse = HttpClientUtil.get("/creditos/cliente/" + cedula + "/creditos-activos", String.class);

            Type listType = new TypeToken<List<Credito>>(){}.getType();
            return gson.fromJson(jsonResponse, listType);

        } finally {
            HttpClientUtil.setBaseUrl(originalBaseUrl);
            if (originalToken != null) {
                HttpClientUtil.setAuthToken(originalToken);
            }
        }
    }

    public CreditoEvaluacionResponse verificarSujetoCredito(String cedula) throws IOException, ParseException {
        String originalBaseUrl = HttpClientUtil.getBaseUrl();
        String originalToken = HttpClientUtil.getAuthToken();

        try {
            HttpClientUtil.setBaseUrl(BANQUITO_BASE_URL);
            HttpClientUtil.clearAuthToken();

            return HttpClientUtil.get("/creditos/cliente/" + cedula + "/sujeto-credito", CreditoEvaluacionResponse.class);

        } finally {
            HttpClientUtil.setBaseUrl(originalBaseUrl);
            if (originalToken != null) {
                HttpClientUtil.setAuthToken(originalToken);
            }
        }
    }

    public TablaAmortizacion obtenerTablaAmortizacion(Integer idCredito) throws IOException, ParseException {
        String originalBaseUrl = HttpClientUtil.getBaseUrl();
        String originalToken = HttpClientUtil.getAuthToken();

        try {
            HttpClientUtil.setBaseUrl(BANQUITO_BASE_URL);
            HttpClientUtil.clearAuthToken();

            return HttpClientUtil.get("/creditos/amortizacion/" + idCredito, TablaAmortizacion.class);

        } finally {
            HttpClientUtil.setBaseUrl(originalBaseUrl);
            if (originalToken != null) {
                HttpClientUtil.setAuthToken(originalToken);
            }
        }
    }
}
