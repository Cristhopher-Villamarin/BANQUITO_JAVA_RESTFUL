package ec.edu.monster.controller;

import ec.edu.monster.model.LoginResponse;
import ec.edu.monster.service.AuthService;
import ec.edu.monster.view.ConsoleUI;

public class LoginController {
    private final AuthService authService;
    
    public LoginController() {
        this.authService = new AuthService();
    }
    
    public LoginResponse mostrarPantallaLogin() {
        ConsoleUI.limpiarPantalla();
        ConsoleUI.mostrarPantallaInicio();
        ConsoleUI.mostrarEncabezado("INICIO DE SESIÓN");
        
        System.out.println();
        String username = ConsoleUI.leerLinea("Usuario: ");
        String password = ConsoleUI.leerLinea("Contraseña: ");
        
        try {
            System.out.println();
            ConsoleUI.mostrarMensajeInfo("Autenticando...");
            LoginResponse response = authService.login(username, password);
            
            if (response.isAutenticado()) {
                ConsoleUI.mostrarMensajeExito("¡Bienvenido " + response.getUsername() + "!");
                ConsoleUI.mostrarMensajeInfo("Rol: " + response.getRol());
                Thread.sleep(1500);
                return response;
            } else {
                ConsoleUI.mostrarMensajeError("Error: " + response.getMensaje());
                ConsoleUI.pausa();
                return null;
            }
        } catch (Exception e) {
            ConsoleUI.mostrarMensajeError("Error de conexión: " + e.getMessage());
            ConsoleUI.pausa();
            return null;
        }
    }
    
    public void logout() {
        authService.logout();
    }
}
