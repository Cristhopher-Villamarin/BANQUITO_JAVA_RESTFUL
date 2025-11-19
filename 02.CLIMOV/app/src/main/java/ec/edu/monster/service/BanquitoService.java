package ec.edu.monster.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.db.HttpClientUtil;
import ec.edu.monster.model.Credito;
import ec.edu.monster.model.CreditoEvaluacionRequest;
import ec.edu.monster.model.CreditoEvaluacionResponse;
import ec.edu.monster.model.TablaAmortizacion;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class BanquitoService {

    // Para emulador Android usar: http://10.0.2.2:8080
    // Para dispositivo físico usar la IP de tu máquina: http://192.168.x.x:8080
    private static final String BANQUITO_BASE_URL = "http://10.95.194.42:8080/ws_banquito_gr08/api";
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();

    public CreditoEvaluacionResponse evaluarCredito(CreditoEvaluacionRequest request) throws IOException {
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

    public List<Credito> obtenerCreditosActivos(String cedula) throws IOException {
        String originalBaseUrl = HttpClientUtil.getBaseUrl();
        String originalToken = HttpClientUtil.getAuthToken();

        try {
            HttpClientUtil.setBaseUrl(BANQUITO_BASE_URL);
            HttpClientUtil.clearAuthToken();

            Type listType = new TypeToken<List<Credito>>(){}.getType();
            return HttpClientUtil.get("/creditos/cliente/" + cedula + "/creditos-activos", listType);

        } finally {
            HttpClientUtil.setBaseUrl(originalBaseUrl);
            if (originalToken != null) {
                HttpClientUtil.setAuthToken(originalToken);
            }
        }
    }

    public CreditoEvaluacionResponse verificarSujetoCredito(String cedula) throws IOException {
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

    public TablaAmortizacion obtenerTablaAmortizacion(Integer idCredito) throws IOException {
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

