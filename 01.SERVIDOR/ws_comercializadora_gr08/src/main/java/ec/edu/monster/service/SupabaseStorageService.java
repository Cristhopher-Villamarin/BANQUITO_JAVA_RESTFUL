package ec.edu.monster.service;

import ec.edu.monster.config.SupabaseConfig;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SupabaseStorageService {

    private static final Logger LOGGER = Logger.getLogger(SupabaseStorageService.class.getName());

    public String subirImagenBase64(String base64, String originalFileName) throws IOException {
        if (base64 == null || base64.isBlank()) {
            return null;
        }

        String anonKey = SupabaseConfig.getAnonKey();
        if (anonKey == null || anonKey.isBlank()) {
            throw new IllegalStateException("SUPABASE_ANON_KEY no está configurada en las variables de entorno");
        }

        String extension = extraerExtension(originalFileName);
        String objectPath = "electrodomesticos/" + UUID.randomUUID() + (extension != null ? ("." + extension) : "");

        byte[] bytes = Base64.getDecoder().decode(base64);

        String urlStr = SupabaseConfig.SUPABASE_URL + "/storage/v1/object/" + SupabaseConfig.SUPABASE_BUCKET + "/" + objectPath;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("apikey", anonKey);
        conn.setRequestProperty("Authorization", "Bearer " + anonKey);
        conn.setRequestProperty("Content-Type", "application/octet-stream");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(bytes);
        }

        int status = conn.getResponseCode();
        if (status >= 200 && status < 300) {
            return SupabaseConfig.SUPABASE_URL + "/storage/v1/object/public/" + SupabaseConfig.SUPABASE_BUCKET + "/" + objectPath;
        } else {
            LOGGER.log(Level.SEVERE, "Error subiendo imagen a Supabase. HTTP status: {0}", status);
            throw new IOException("Error subiendo imagen a Supabase, código HTTP: " + status);
        }
    }

    private String extraerExtension(String nombreArchivo) {
        if (nombreArchivo == null) {
            return null;
        }
        int idx = nombreArchivo.lastIndexOf('.')
                ;
        if (idx == -1 || idx == nombreArchivo.length() - 1) {
            return null;
        }
        return nombreArchivo.substring(idx + 1);
    }
}
