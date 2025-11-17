package ec.edu.monster.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

public class HttpClientUtil {
    // Para emulador Android usar: http://10.0.2.2:8080
    // Para dispositivo físico usar la IP de tu máquina: http://192.168.x.x:8080
    private static String BASE_URL = "http://192.168.1.7:8080/ws_comercializadora_gr08/api";
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();
    private static String authToken = null;
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

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

    public static <T> T post(String endpoint, Object requestBody, Class<T> responseType) throws IOException {
        String jsonBody = gson.toJson(requestBody);
        RequestBody body = RequestBody.create(jsonBody, JSON);

        Request.Builder requestBuilder = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(body)
                .addHeader("Content-Type", "application/json");

        if (authToken != null && !endpoint.contains("/auth/")) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            if (response.isSuccessful()) {
                if (responseType == String.class) {
                    return responseType.cast(responseBody);
                }
                return gson.fromJson(responseBody, responseType);
            } else {
                String errorMsg = extractErrorMessage(responseBody);
                throw new IOException("Error HTTP " + response.code() + ": " + errorMsg);
            }
        }
    }

    public static <T> T get(String endpoint, Class<T> responseType) throws IOException {
        Request.Builder requestBuilder = new Request.Builder()
                .url(BASE_URL + endpoint)
                .get()
                .addHeader("Accept", "application/json");

        if (authToken != null) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            if (response.isSuccessful()) {
                if (responseType == String.class) {
                    return responseType.cast(responseBody);
                }
                return gson.fromJson(responseBody, responseType);
            } else {
                String errorMsg = extractErrorMessage(responseBody);
                throw new IOException("Error HTTP " + response.code() + ": " + errorMsg);
            }
        }
    }

    public static <T> T get(String endpoint, Type responseType) throws IOException {
        Request.Builder requestBuilder = new Request.Builder()
                .url(BASE_URL + endpoint)
                .get()
                .addHeader("Accept", "application/json");

        if (authToken != null) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            if (response.isSuccessful()) {
                return gson.fromJson(responseBody, responseType);
            } else {
                String errorMsg = extractErrorMessage(responseBody);
                throw new IOException("Error HTTP " + response.code() + ": " + errorMsg);
            }
        }
    }

    public static <T> T put(String endpoint, Object requestBody, Class<T> responseType) throws IOException {
        String jsonBody = gson.toJson(requestBody);
        RequestBody body = RequestBody.create(jsonBody, JSON);

        Request.Builder requestBuilder = new Request.Builder()
                .url(BASE_URL + endpoint)
                .put(body)
                .addHeader("Content-Type", "application/json");

        if (authToken != null) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            if (response.isSuccessful()) {
                return gson.fromJson(responseBody, responseType);
            } else {
                String errorMsg = extractErrorMessage(responseBody);
                throw new IOException("Error HTTP " + response.code() + ": " + errorMsg);
            }
        }
    }

    public static String delete(String endpoint) throws IOException {
        Request.Builder requestBuilder = new Request.Builder()
                .url(BASE_URL + endpoint)
                .delete();

        if (authToken != null) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            if (response.isSuccessful()) {
                return responseBody;
            } else {
                String errorMsg = extractErrorMessage(responseBody);
                throw new IOException("Error HTTP " + response.code() + ": " + errorMsg);
            }
        }
    }
}

