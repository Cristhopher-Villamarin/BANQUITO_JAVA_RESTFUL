package ec.edu.monster.controller;

import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.service.ElectrodomesticoService;
import ec.edu.monster.view.ConsoleUI;

import java.util.List;

public class ElectrodomesticoController {
    private final ElectrodomesticoService electrodomesticoService;
    
    public ElectrodomesticoController() {
        this.electrodomesticoService = new ElectrodomesticoService();
    }
    
    public void mostrarMenuCRUD() {
        boolean continuar = true;
        
        while (continuar) {
            ConsoleUI.limpiarPantalla();
            ConsoleUI.mostrarEncabezado("GESTIÓN DE ELECTRODOMÉSTICOS - ADMIN");
            
            System.out.println();
            System.out.println("  1. Listar todos los electrodomésticos");
            System.out.println("  2. Buscar electrodoméstico por ID");
            System.out.println("  3. Crear nuevo electrodoméstico");
            System.out.println("  4. Actualizar electrodoméstico");
            System.out.println("  5. Eliminar electrodoméstico");
            System.out.println("  0. Cerrar sesión");
            System.out.println();
            
            int opcion = ConsoleUI.leerEntero("Seleccione una opción: ");
            
            switch (opcion) {
                case 1 -> listarElectrodomesticos();
                case 2 -> buscarPorId();
                case 3 -> crearElectrodomestico();
                case 4 -> actualizarElectrodomestico();
                case 5 -> eliminarElectrodomestico();
                case 0 -> {
                    ConsoleUI.mostrarMensajeInfo("Cerrando sesión...");
                    continuar = false;
                }
                default -> {
                    ConsoleUI.mostrarMensajeError("Opción no válida");
                    ConsoleUI.pausa();
                }
            }
        }
    }
    
    private void listarElectrodomesticos() {
        try {
            ConsoleUI.limpiarPantalla();
            ConsoleUI.mostrarEncabezado("LISTADO DE ELECTRODOMÉSTICOS");
            System.out.println();
            
            List<Electrodomestico> lista = electrodomesticoService.listarElectrodomesticos();
            
            if (lista.isEmpty()) {
                ConsoleUI.mostrarMensajeAdvertencia("No hay electrodomésticos registrados.");
            } else {
                ConsoleUI.mostrarLinea();
                for (Electrodomestico e : lista) {
                    System.out.println("  " + e.toString());
                }
                ConsoleUI.mostrarLinea();
                ConsoleUI.mostrarMensajeInfo("Total: " + lista.size() + " electrodomésticos");
            }
            
            ConsoleUI.pausa();
        } catch (Exception e) {
            ConsoleUI.mostrarMensajeError("Error al listar: " + e.getMessage());
            ConsoleUI.pausa();
        }
    }
    
    private void buscarPorId() {
        try {
            ConsoleUI.limpiarPantalla();
            ConsoleUI.mostrarEncabezado("BUSCAR ELECTRODOMÉSTICO");
            System.out.println();
            
            int id = ConsoleUI.leerEntero("Ingrese el ID del electrodoméstico: ");
            Electrodomestico e = electrodomesticoService.obtenerPorId(id);
            
            System.out.println();
            ConsoleUI.mostrarLinea();
            System.out.println("  " + e.toString());
            ConsoleUI.mostrarLinea();
            
            ConsoleUI.pausa();
        } catch (Exception e) {
            ConsoleUI.mostrarMensajeError("Error: " + e.getMessage());
            ConsoleUI.pausa();
        }
    }
    
    private void crearElectrodomestico() {
        try {
            ConsoleUI.limpiarPantalla();
            ConsoleUI.mostrarEncabezado("CREAR NUEVO ELECTRODOMÉSTICO");
            System.out.println();
            
            String nombre = ConsoleUI.leerLinea("Nombre del electrodoméstico: ");
            double precio = ConsoleUI.leerDouble("Precio de venta: $");
            
            Electrodomestico nuevo = electrodomesticoService.crear(nombre, precio);
            
            System.out.println();
            ConsoleUI.mostrarMensajeExito("Electrodoméstico creado exitosamente!");
            ConsoleUI.mostrarLinea();
            System.out.println("  " + nuevo.toString());
            ConsoleUI.mostrarLinea();
            
            ConsoleUI.pausa();
        } catch (Exception e) {
            ConsoleUI.mostrarMensajeError("Error al crear: " + e.getMessage());
            ConsoleUI.pausa();
        }
    }
    
    private void actualizarElectrodomestico() {
        try {
            ConsoleUI.limpiarPantalla();
            ConsoleUI.mostrarEncabezado("ACTUALIZAR ELECTRODOMÉSTICO");
            System.out.println();
            
            int id = ConsoleUI.leerEntero("ID del electrodoméstico a actualizar: ");
            
            // Mostrar datos actuales
            Electrodomestico actual = electrodomesticoService.obtenerPorId(id);
            System.out.println();
            ConsoleUI.mostrarMensajeInfo("Datos actuales:");
            System.out.println("  " + actual.toString());
            System.out.println();
            
            String nombre = ConsoleUI.leerLinea("Nuevo nombre: ");
            double precio = ConsoleUI.leerDouble("Nuevo precio: $");
            
            Electrodomestico actualizado = electrodomesticoService.actualizar(id, nombre, precio);
            
            System.out.println();
            ConsoleUI.mostrarMensajeExito("Electrodoméstico actualizado exitosamente!");
            ConsoleUI.mostrarLinea();
            System.out.println("  " + actualizado.toString());
            ConsoleUI.mostrarLinea();
            
            ConsoleUI.pausa();
        } catch (Exception e) {
            ConsoleUI.mostrarMensajeError("Error al actualizar: " + e.getMessage());
            ConsoleUI.pausa();
        }
    }
    
    private void eliminarElectrodomestico() {
        try {
            ConsoleUI.limpiarPantalla();
            ConsoleUI.mostrarEncabezado("ELIMINAR ELECTRODOMÉSTICO");
            System.out.println();
            
            int id = ConsoleUI.leerEntero("ID del electrodoméstico a eliminar: ");
            
            // Mostrar datos antes de eliminar
            Electrodomestico e = electrodomesticoService.obtenerPorId(id);
            System.out.println();
            ConsoleUI.mostrarMensajeAdvertencia("Se eliminará el siguiente electrodoméstico:");
            System.out.println("  " + e.toString());
            System.out.println();
            
            String confirmacion = ConsoleUI.leerLinea("¿Está seguro? (S/N): ");
            
            if (confirmacion.equalsIgnoreCase("S")) {
                electrodomesticoService.eliminar(id);
                System.out.println();
                ConsoleUI.mostrarMensajeExito("Electrodoméstico eliminado exitosamente!");
            } else {
                ConsoleUI.mostrarMensajeInfo("Operación cancelada.");
            }
            
            ConsoleUI.pausa();
        } catch (Exception e) {
            ConsoleUI.mostrarMensajeError("Error al eliminar: " + e.getMessage());
            ConsoleUI.pausa();
        }
    }
}
