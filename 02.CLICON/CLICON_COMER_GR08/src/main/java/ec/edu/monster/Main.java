package ec.edu.monster;

import ec.edu.monster.controller.ElectrodomesticoController;
import ec.edu.monster.controller.LoginController;
import ec.edu.monster.controller.MonsterController;
import ec.edu.monster.model.LoginResponse;
import ec.edu.monster.view.ConsoleUI;

public class Main {
    
    public static void main(String[] args) {
        LoginController loginController = new LoginController();
        ElectrodomesticoController adminController = new ElectrodomesticoController();
        MonsterController monsterController = new MonsterController();
        
        boolean ejecutando = true;
        
        while (ejecutando) {
            // Mostrar pantalla de login
            LoginResponse loginResponse = loginController.mostrarPantallaLogin();
            
            if (loginResponse != null && loginResponse.isAutenticado()) {
                String rol = loginResponse.getRol();
                
                // Redirigir según el rol
                if ("ADMIN".equalsIgnoreCase(rol)) {
                    // Menú CRUD para ADMIN
                    adminController.mostrarMenuCRUD();
                } else if ("USER".equalsIgnoreCase(rol)) {
                    // Menú de facturación para MONSTER
                    monsterController.mostrarMenuPrincipal();
                } else {
                    ConsoleUI.mostrarMensajeError("Rol no reconocido: " + rol);
                    ConsoleUI.pausa();
                }
                
                // Logout después de salir del menú
                loginController.logout();
            } else {
                // Si el login falla, preguntar si desea intentar de nuevo
                ConsoleUI.limpiarPantalla();
                ConsoleUI.mostrarEncabezado("AUTENTICACIÓN FALLIDA");
                System.out.println();
                String respuesta = ConsoleUI.leerLinea("¿Desea intentar nuevamente? (S/N): ");
                
                if (!respuesta.equalsIgnoreCase("S")) {
                    ejecutando = false;
                }
            }
        }
        
        // Mensaje de despedida
        ConsoleUI.limpiarPantalla();
        ConsoleUI.mostrarPantallaInicio();
        System.out.println();
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "  Gracias por usar el Sistema de Comercialización Monster" + ConsoleUI.RESET);
        System.out.println(ConsoleUI.YELLOW + "  ¡Hasta pronto!" + ConsoleUI.RESET);
        System.out.println();
        
        ConsoleUI.cerrarScanner();
    }
}
