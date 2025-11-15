package ec.edu.monster.dto;

import java.time.LocalDateTime;

public class LoginResponse {
    
    private boolean autenticado;
    private String mensaje;
    private String username;
    private String rol;
    private String token;
    private LocalDateTime timestamp;

    public LoginResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public LoginResponse(boolean autenticado, String mensaje, String username, String rol) {
        this();
        this.autenticado = autenticado;
        this.mensaje = mensaje;
        this.username = username;
        this.rol = rol;
        if (autenticado) {
            this.token = generarToken();
        }
    }

    public static LoginResponse exitoso(String username, String rol) {
        return new LoginResponse(true, "Autenticación exitosa", username, rol);
    }

    public static LoginResponse fallido(String mensaje) {
        return new LoginResponse(false, mensaje, null, null);
    }

    private String generarToken() {
        return "TOKEN_" + username + "_" + System.currentTimeMillis();
    }

    public boolean isAutenticado() {
        return autenticado;
    }

    public void setAutenticado(boolean autenticado) {
        this.autenticado = autenticado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
