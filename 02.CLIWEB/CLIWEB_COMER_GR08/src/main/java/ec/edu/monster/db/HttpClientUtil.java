package ec.edu.monster.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;

public class HttpClientUtil {
    private static String BASE_URL = "http://localhost:8080/ws_comercializadora_gr08/api";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static String authToken = null;

    public static void setAuthToken(String token) {
        authToken = token;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static void clearAuthToken() {
        authToken = null;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void setBaseUrl(String newBaseUrl) {
        BASE_URL = newBaseUrl;
    }

    private static String extractErrorMessage(String responseBody) {
        try {
            JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
            if (jsonObject.has("mensaje")) {
                return jsonObject.get("mensaje").getAsString();
            }
            if (jsonObject.has("message")) {
                return jsonObject.get("message").getAsString();
            }
            return responseBody;
        } catch (Exception e) {
            return responseBody;
        }
    }

    public static <T> T post(String endpoint, Object requestBody, Class<T> responseType) throws IOException, ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(BASE_URL + endpoint);
            httpPost.setHeader("Content-Type", "application/json");

            if (authToken != null && !endpoint.contains("/auth/")) {
                httpPost.setHeader("Authorization", "Bearer " + authToken);
            }

            String jsonBody = gson.toJson(requestBody);
            httpPost.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String responseBody = EntityUtils.toString(response.getEntity());

                if (response.getCode() >= 200 && response.getCode() < 300) {
                    return gson.fromJson(responseBody, responseType);
                } else {
                    String errorMsg = extractErrorMessage(responseBody);
                    throw new IOException("Error HTTP " + response.getCode() + ": " + errorMsg);
                }
            }
        }
    }

    public static <T> T get(String endpoint, Class<T> responseType) throws IOException, ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(BASE_URL + endpoint);
            httpGet.setHeader("Accept", "application/json");

            if (authToken != null) {
                httpGet.setHeader("Authorization", "Bearer " + authToken);
            }

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                String responseBody = EntityUtils.toString(response.getEntity());

                if (response.getCode() >= 200 && response.getCode() < 300) {
                    if (responseType == String.class) {
                        return responseType.cast(responseBody);
                    }
                    return gson.fromJson(responseBody, responseType);
                } else {
                    String errorMsg = extractErrorMessage(responseBody);
                    throw new IOException("Error HTTP " + response.getCode() + ": " + errorMsg);
                }
            }
        }
    }

    public static <T> T put(String endpoint, Object requestBody, Class<T> responseType) throws IOException, ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut httpPut = new HttpPut(BASE_URL + endpoint);
            httpPut.setHeader("Content-Type", "application/json");

            if (authToken != null) {
                httpPut.setHeader("Authorization", "Bearer " + authToken);
            }

            String jsonBody = gson.toJson(requestBody);
            httpPut.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));

            try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
                String responseBody = EntityUtils.toString(response.getEntity());

                if (response.getCode() >= 200 && response.getCode() < 300) {
                    return gson.fromJson(responseBody, responseType);
                } else {
                    String errorMsg = extractErrorMessage(responseBody);
                    throw new IOException("Error HTTP " + response.getCode() + ": " + errorMsg);
                }
            }
        }
    }

    public static String delete(String endpoint) throws IOException, ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete httpDelete = new HttpDelete(BASE_URL + endpoint);

            if (authToken != null) {
                httpDelete.setHeader("Authorization", "Bearer " + authToken);
            }

            try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
                String responseBody = EntityUtils.toString(response.getEntity());

                if (response.getCode() >= 200 && response.getCode() < 300) {
                    return responseBody;
                } else {
                    String errorMsg = extractErrorMessage(responseBody);
                    throw new IOException("Error HTTP " + response.getCode() + ": " + errorMsg);
                }
            }
        }
    }
}
