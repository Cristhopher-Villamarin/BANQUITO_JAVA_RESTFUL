package ec.edu.monster.util;

import android.content.Context;
import android.content.SharedPreferences;
import ec.edu.monster.model.LoginResponse;

public class SessionManager {
    private static final String PREF_NAME = "ComercializadoraSession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROL = "rol";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_AUTENTICADO = "autenticado";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createSession(LoginResponse loginResponse) {
        editor.putBoolean(KEY_AUTENTICADO, loginResponse.isAutenticado());
        editor.putString(KEY_USERNAME, loginResponse.getUsername());
        editor.putString(KEY_ROL, loginResponse.getRol());
        editor.putString(KEY_TOKEN, loginResponse.getToken());
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_AUTENTICADO, false);
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public String getRol() {
        return pref.getString(KEY_ROL, null);
    }

    public String getToken() {
        return pref.getString(KEY_TOKEN, null);
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(getRol());
    }

    public boolean isUser() {
        return "USER".equalsIgnoreCase(getRol());
    }
}

