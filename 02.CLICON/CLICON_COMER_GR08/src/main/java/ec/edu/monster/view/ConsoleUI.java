package ec.edu.monster.view;

import java.util.Scanner;

public class ConsoleUI {
    private static final Scanner scanner = new Scanner(System.in);
    
    // Colores ANSI
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BOLD = "\u001B[1m";
    
    public static void limpiarPantalla() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Si falla, imprimir líneas en blanco
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    public static void mostrarPantallaInicio() {
        limpiarPantalla();
        System.out.println(CYAN + BOLD);
        System.out.println("╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                      ║");
        System.out.println("║              ███████╗██╗     ███████╗ ██████╗████████╗██████╗       ║");
        System.out.println("║              ██╔════╝██║     ██╔════╝██╔════╝╚══██╔══╝██╔══██╗      ║");
        System.out.println("║              █████╗  ██║     █████╗  ██║        ██║   ██████╔╝      ║");
        System.out.println("║              ██╔══╝  ██║     ██╔══╝  ██║        ██║   ██╔══██╗      ║");
        System.out.println("║              ███████╗███████╗███████╗╚██████╗   ██║   ██║  ██║      ║");
        System.out.println("║              ╚══════╝╚══════╝╚══════╝ ╚═════╝   ╚═╝   ╚═╝  ╚═╝      ║");
        System.out.println("║                                                                      ║");
        System.out.println("║                    " + YELLOW + "COMERCIALIZADORA MONSTER" + CYAN + "                         ║");
        System.out.println("║                  " + WHITE + "Sistema de Gestión de Ventas" + CYAN + "                      ║");
        System.out.println("║                                                                      ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
        System.out.println(RESET);
        System.out.println();
    }
    
    public static void mostrarEncabezado(String titulo) {
        System.out.println(CYAN + BOLD);
        System.out.println("╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║  " + centrarTexto(titulo, 66) + "  ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
        System.out.println(RESET);
    }
    
    public static void mostrarMensajeExito(String mensaje) {
        System.out.println(GREEN + "✓ " + mensaje + RESET);
    }
    
    public static void mostrarMensajeError(String mensaje) {
        System.out.println(RED + "✗ " + mensaje + RESET);
    }
    
    public static void mostrarMensajeInfo(String mensaje) {
        System.out.println(BLUE + "ℹ " + mensaje + RESET);
    }
    
    public static void mostrarMensajeAdvertencia(String mensaje) {
        System.out.println(YELLOW + "⚠ " + mensaje + RESET);
    }
    
    public static String leerLinea(String prompt) {
        System.out.print(WHITE + prompt + RESET);
        return scanner.nextLine().trim();
    }
    
    public static int leerEntero(String prompt) {
        while (true) {
            try {
                System.out.print(WHITE + prompt + RESET);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                mostrarMensajeError("Por favor ingrese un número válido.");
            }
        }
    }
    
    public static double leerDouble(String prompt) {
        while (true) {
            try {
                System.out.print(WHITE + prompt + RESET);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                mostrarMensajeError("Por favor ingrese un número válido.");
            }
        }
    }
    
    public static void pausa() {
        System.out.println();
        System.out.print(YELLOW + "Presione ENTER para continuar..." + RESET);
        scanner.nextLine();
    }
    
    public static void mostrarLinea() {
        System.out.println(CYAN + "──────────────────────────────────────────────────────────────────────" + RESET);
    }
    
    private static String centrarTexto(String texto, int ancho) {
        int espacios = (ancho - texto.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < espacios; i++) {
            sb.append(" ");
        }
        sb.append(texto);
        while (sb.length() < ancho) {
            sb.append(" ");
        }
        return sb.toString();
    }
    
    public static void cerrarScanner() {
        scanner.close();
    }
}
