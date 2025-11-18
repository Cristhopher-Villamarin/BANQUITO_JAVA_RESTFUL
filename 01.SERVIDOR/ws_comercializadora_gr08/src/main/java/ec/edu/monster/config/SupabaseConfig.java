package ec.edu.monster.config;

public class SupabaseConfig {

    public static final String SUPABASE_URL = "https://jddqafafuyoibjglwhlj.supabase.co";
    public static final String SUPABASE_BUCKET = "fotos";

    public static String getAnonKey() {
        // Leer desde variable de entorno para no hardcodear la key en el código
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImpkZHFhZmFmdXlvaWJqZ2x3aGxqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM0MjAxNTUsImV4cCI6MjA3ODk5NjE1NX0.Wjl9oNROrfCoR8v8UZpF6Wu_T93ulKsZb-4cmGpdztQ";
    }
}
