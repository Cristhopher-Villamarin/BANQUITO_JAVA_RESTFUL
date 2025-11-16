package ec.edu.monster.service;

import ec.edu.monster.db.HttpClientUtil;
import ec.edu.monster.model.CreditoEvaluacionRequest;
import ec.edu.monster.model.CreditoEvaluacionResponse;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class BanquitoService {
    
    private static final String BANQUITO_BASE_URL = "http://localhost:8080/ws_banquito_gr08/api";
    
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
}
