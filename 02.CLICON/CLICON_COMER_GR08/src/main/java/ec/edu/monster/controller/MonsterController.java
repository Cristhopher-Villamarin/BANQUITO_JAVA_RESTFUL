package ec.edu.monster.controller;

import ec.edu.monster.model.*;
import ec.edu.monster.service.ElectrodomesticoService;
import ec.edu.monster.service.VentaService;
import ec.edu.monster.service.BanquitoService;
import ec.edu.monster.view.ConsoleUI;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MonsterController {
    private final ElectrodomesticoService electrodomesticoService;
    private final VentaService ventaService;
    private final BanquitoService banquitoService;
    
    public MonsterController() {
        this.electrodomesticoService = new ElectrodomesticoService();
        this.ventaService = new VentaService();
        this.banquitoService = new BanquitoService();
    }
    
    public void mostrarMenuPrincipal() {
        boolean continuar = true;
        
        while (continuar) {
            ConsoleUI.limpiarPantalla();
            ConsoleUI.mostrarEncabezado("SISTEMA DE FACTURACIÓN - MONSTER");
            
            System.out.println();
            System.out.println(ConsoleUI.CYAN + "  ╔══════════════════════════════════════════════════════════════════╗" + ConsoleUI.RESET);
            System.out.println(ConsoleUI.CYAN + "  ║                                                                  ║" + ConsoleUI.RESET);
            System.out.println(ConsoleUI.CYAN + "  ║" + ConsoleUI.YELLOW + "              ¡BIENVENIDO AL SISTEMA DE FACTURACIÓN!              " + ConsoleUI.CYAN + "║" + ConsoleUI.RESET);
            System.out.println(ConsoleUI.CYAN + "  ║                                                                  ║" + ConsoleUI.RESET);
            System.out.println(ConsoleUI.CYAN + "  ║" + ConsoleUI.WHITE + "  Este módulo permite gestionar las ventas y facturación de       " + ConsoleUI.CYAN + "║" + ConsoleUI.RESET);
            System.out.println(ConsoleUI.CYAN + "  ║" + ConsoleUI.WHITE + "  electrodomésticos para la Comercializadora Monster.             " + ConsoleUI.CYAN + "║" + ConsoleUI.RESET);
            System.out.println(ConsoleUI.CYAN + "  ║                                                                  ║" + ConsoleUI.RESET);
            System.out.println(ConsoleUI.CYAN + "  ║" + ConsoleUI.GREEN + "  ✓ Registro de ventas                                            " + ConsoleUI.CYAN + "║" + ConsoleUI.RESET);
            System.out.println(ConsoleUI.CYAN + "  ║" + ConsoleUI.GREEN + "  ✓ Consulta de electrodomésticos                                 " + ConsoleUI.CYAN + "║" + ConsoleUI.RESET);
            System.out.println(ConsoleUI.CYAN + "  ║" + ConsoleUI.GREEN + "  ✓ Evaluación de créditos                                        " + ConsoleUI.CYAN + "║" + ConsoleUI.RESET);
            System.out.println(ConsoleUI.CYAN + "  ║                                                                  ║" + ConsoleUI.RESET);
            System.out.println(ConsoleUI.CYAN + "  ╚══════════════════════════════════════════════════════════════════╝" + ConsoleUI.RESET);
            System.out.println();
            
            System.out.println(ConsoleUI.YELLOW + "  FUNCIONALIDADES DISPONIBLES:" + ConsoleUI.RESET);
            System.out.println();
            System.out.println("  1. Consultar catálogo de electrodomésticos");
            System.out.println("  2. Registrar nueva venta");
            System.out.println("  3. Verificar estado de crédito de cliente");
            System.out.println("  4. Ver tabla de amortización de crédito");
            System.out.println("  0. Cerrar sesión");
            System.out.println();
            
            int opcion = ConsoleUI.leerEntero("Seleccione una opción: ");
            
            switch (opcion) {
                case 1 -> consultarCatalogo();
                case 2 -> registrarVenta();
                case 3 -> verificarEstadoCredito();
                case 4 -> verTablaAmortizacion();
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
    
    private void consultarCatalogo() {
        try {
            ConsoleUI.limpiarPantalla();
            ConsoleUI.mostrarEncabezado("CATÁLOGO DE ELECTRODOMÉSTICOS");
            System.out.println();
            
            List<Electrodomestico> lista = electrodomesticoService.listarElectrodomesticos();
            
            if (lista.isEmpty()) {
                ConsoleUI.mostrarMensajeAdvertencia("No hay electrodomésticos disponibles.");
            } else {
                ConsoleUI.mostrarLinea();
                for (Electrodomestico e : lista) {
                    System.out.println("  " + e.toString());
                }
                ConsoleUI.mostrarLinea();
                ConsoleUI.mostrarMensajeInfo("Total: " + lista.size() + " electrodomésticos disponibles");
            }
            
            ConsoleUI.pausa();
        } catch (Exception e) {
            ConsoleUI.mostrarMensajeError("Error al consultar catálogo: " + e.getMessage());
            ConsoleUI.pausa();
        }
    }
    
    private void registrarVenta() {
        try {
            ConsoleUI.limpiarPantalla();
            ConsoleUI.mostrarEncabezado("REGISTRAR NUEVA VENTA");
            System.out.println();
            
            // Datos del cliente
            ConsoleUI.mostrarMensajeInfo("DATOS DEL CLIENTE:");
            String cedula = ConsoleUI.leerLinea("Cédula del cliente: ");
            
            // Obtener nombre del cliente desde Banquito
            System.out.println();
            ConsoleUI.mostrarMensajeInfo("Consultando datos del cliente...");
            String nombre = "";
            try {
                CreditoEvaluacionRequest consultaCliente = new CreditoEvaluacionRequest(cedula, BigDecimal.ONE, 12);
                CreditoEvaluacionResponse datosCliente = banquitoService.evaluarCredito(consultaCliente);
                if (datosCliente != null && datosCliente.getSujetoCredito() != null) {
                    // El nombre viene en el response del Banquito (aunque no lo mostramos aquí)
                    nombre = ConsoleUI.leerLinea("Nombre completo: ");
                } else {
                    nombre = ConsoleUI.leerLinea("Nombre completo: ");
                }
            } catch (Exception ex) {
                nombre = ConsoleUI.leerLinea("Nombre completo: ");
            }
            
            System.out.println();
            ConsoleUI.mostrarMensajeInfo("FORMA DE PAGO:");
            System.out.println("  1. EFECTIVO (33% descuento)");
            System.out.println("  2. CRÉDITO DIRECTO (validación con Banquito)");
            System.out.println();
            
            int opcionPago = ConsoleUI.leerEntero("Seleccione forma de pago: ");
            String formaPago;
            
            if (opcionPago == 1) {
                formaPago = "EFECTIVO";
            } else if (opcionPago == 2) {
                formaPago = "CREDITO_DIRECTO";
            } else {
                ConsoleUI.mostrarMensajeError("Opción de pago no válida.");
                ConsoleUI.pausa();
                return;
            }
            
            // Selección de productos
            System.out.println();
            ConsoleUI.mostrarMensajeInfo("PRODUCTOS A COMPRAR:");
            List<DetalleVentaRequest> detalles = new ArrayList<>();
            boolean agregarMas = true;
            
            while (agregarMas) {
                System.out.println();
                int idProducto = ConsoleUI.leerEntero("ID del electrodoméstico: ");
                int cantidad = ConsoleUI.leerEntero("Cantidad: ");
                
                detalles.add(new DetalleVentaRequest(idProducto, cantidad));
                
                System.out.println();
                String respuesta = ConsoleUI.leerLinea("¿Agregar otro producto? (S/N): ");
                agregarMas = respuesta.equalsIgnoreCase("S");
            }
            
            // Si es crédito, pedir plazo de meses
            Integer plazoMeses = null;
            if (formaPago.equals("CREDITO_DIRECTO")) {
                System.out.println();
                ConsoleUI.mostrarMensajeInfo("PLAZO DEL CRÉDITO:");
                System.out.println("  Opciones: 6, 12, 18, 24 meses");
                plazoMeses = ConsoleUI.leerEntero("Plazo en meses: ");
            }
            
            // Confirmar venta
            System.out.println();
            ConsoleUI.mostrarLinea();
            ConsoleUI.mostrarMensajeAdvertencia("RESUMEN DE LA VENTA:");
            System.out.println("  Cliente: " + nombre + " (" + cedula + ")");
            System.out.println("  Forma de pago: " + formaPago);
            if (plazoMeses != null) {
                System.out.println("  Plazo: " + plazoMeses + " meses");
            }
            System.out.println("  Productos: " + detalles.size());
            ConsoleUI.mostrarLinea();
            System.out.println();
            
            String confirmar = ConsoleUI.leerLinea("¿Confirmar venta? (S/N): ");
            
            if (!confirmar.equalsIgnoreCase("S")) {
                ConsoleUI.mostrarMensajeInfo("Venta cancelada.");
                ConsoleUI.pausa();
                return;
            }
            
            // Procesar venta
            System.out.println();
            ConsoleUI.mostrarMensajeInfo("Procesando venta...");
            
            VentaRequest ventaRequest = new VentaRequest(cedula, nombre, formaPago, detalles);
            VentaResponse response = ventaService.procesarVenta(ventaRequest);
            
            System.out.println();
            
            if (response.isVentaExitosa()) {
                ConsoleUI.mostrarMensajeExito("¡VENTA PROCESADA EXITOSAMENTE!");
                System.out.println();
                ConsoleUI.mostrarLinea();
                System.out.println("  " + ConsoleUI.BOLD + "FACTURA #" + response.getIdFactura() + ConsoleUI.RESET);
                ConsoleUI.mostrarLinea();
                System.out.println("  Cliente: " + nombre);
                System.out.println("  Cédula: " + cedula);
                System.out.println("  Forma de pago: " + response.getFormaPago());
                System.out.println("  Estado: " + response.getEstadoFactura());
                System.out.println();
                System.out.println("  Subtotal:  $" + response.getSubtotal());
                System.out.println("  Descuento: $" + response.getDescuento());
                System.out.println("  " + ConsoleUI.GREEN + ConsoleUI.BOLD + "TOTAL:     $" + response.getTotal() + ConsoleUI.RESET);
                System.out.println();
                
                if (response.getDetalles() != null && !response.getDetalles().isEmpty()) {
                    ConsoleUI.mostrarMensajeInfo("DETALLE DE PRODUCTOS:");
                    for (DetalleVentaResponse detalle : response.getDetalles()) {
                        System.out.println("  - " + detalle.getNombreElectrodomestico());
                        System.out.println("    Cantidad: " + detalle.getCantidad() + 
                                         " x $" + detalle.getPrecioUnitario() + 
                                         " = $" + detalle.getSubtotalLinea());
                    }
                }
                
                ConsoleUI.mostrarLinea();
            } else {
                ConsoleUI.mostrarMensajeError("VENTA RECHAZADA");
                System.out.println();
                ConsoleUI.mostrarMensajeAdvertencia("Motivo: " + response.getMensaje());
            }
            
            ConsoleUI.pausa();
            
        } catch (Exception e) {
            System.out.println();
            // Extraer mensaje limpio del error
            String mensajeError = e.getMessage();
            if (mensajeError != null && mensajeError.contains("Error HTTP")) {
                // Mostrar solo el mensaje relevante
                ConsoleUI.mostrarMensajeError("VENTA RECHAZADA");
                System.out.println();
                // Extraer el mensaje después de los dos puntos
                int index = mensajeError.lastIndexOf(": ");
                if (index != -1 && index + 2 < mensajeError.length()) {
                    String mensaje = mensajeError.substring(index + 2);
                    ConsoleUI.mostrarMensajeAdvertencia("Motivo: " + mensaje);
                } else {
                    ConsoleUI.mostrarMensajeAdvertencia("Motivo: " + mensajeError);
                }
            } else {
                ConsoleUI.mostrarMensajeError("Error al procesar venta: " + mensajeError);
            }
            ConsoleUI.pausa();
        }
    }
    
    private void verificarEstadoCredito() {
        try {
            ConsoleUI.limpiarPantalla();
            ConsoleUI.mostrarEncabezado("VERIFICAR ESTADO DE CRÉDITO");
            System.out.println();
            
            String cedula = ConsoleUI.leerLinea("Cédula del cliente: ");
            
            System.out.println();
            ConsoleUI.mostrarMensajeInfo("Consultando con Banquito...");
            
            // Consultar con monto mínimo para obtener estado del cliente
            CreditoEvaluacionRequest request = new CreditoEvaluacionRequest(cedula, BigDecimal.ONE, 12);
            CreditoEvaluacionResponse response = banquitoService.evaluarCredito(request);
            
            System.out.println();
            ConsoleUI.mostrarLinea();
            System.out.println("  " + ConsoleUI.BOLD + ConsoleUI.CYAN + "ESTADO DE CRÉDITO DEL CLIENTE" + ConsoleUI.RESET);
            ConsoleUI.mostrarLinea();
            System.out.println();
            System.out.println("  Cédula: " + cedula);
            
            if (response.getSujetoCredito() != null && response.getSujetoCredito()) {
                System.out.println("  Estado: " + ConsoleUI.GREEN + "✓ SUJETO DE CRÉDITO" + ConsoleUI.RESET);
                System.out.println();
                System.out.println("  " + ConsoleUI.BOLD + "Monto máximo de crédito: " + ConsoleUI.GREEN + "$" + response.getMontoMaximoCredito() + ConsoleUI.RESET);
                
                if (response.getCreditoAprobado() != null) {
                    System.out.println();
                    if (response.getCreditoAprobado()) {
                        System.out.println("  Crédito actual: " + ConsoleUI.GREEN + "APROBADO" + ConsoleUI.RESET);
                        if (response.getIdCredito() != null) {
                            System.out.println("  ID Crédito: " + response.getIdCredito());
                        }
                    } else {
                        System.out.println("  Crédito actual: " + ConsoleUI.RED + "RECHAZADO" + ConsoleUI.RESET);
                    }
                }
                
                if (response.getMensaje() != null) {
                    System.out.println();
                    System.out.println("  Mensaje: " + response.getMensaje());
                }
            } else {
                System.out.println("  Estado: " + ConsoleUI.RED + "✗ NO ES SUJETO DE CRÉDITO" + ConsoleUI.RESET);
                System.out.println();
                if (response.getMensaje() != null) {
                    ConsoleUI.mostrarMensajeAdvertencia("Motivo: " + response.getMensaje());
                }
            }
            
            ConsoleUI.mostrarLinea();
            ConsoleUI.pausa();
            
        } catch (Exception e) {
            System.out.println();
            ConsoleUI.mostrarMensajeError("Error al verificar crédito: " + e.getMessage());
            ConsoleUI.pausa();
        }
    }
    
    private void verTablaAmortizacion() {
        try {
            ConsoleUI.limpiarPantalla();
            ConsoleUI.mostrarEncabezado("TABLA DE AMORTIZACIÓN");
            System.out.println();
            
            String cedula = ConsoleUI.leerLinea("Cédula del cliente: ");
            String montoStr = ConsoleUI.leerLinea("Monto del electrodoméstico: $");
            BigDecimal monto = new BigDecimal(montoStr);
            int plazoMeses = ConsoleUI.leerEntero("Plazo en meses (6, 12, 18, 24): ");
            
            System.out.println();
            ConsoleUI.mostrarMensajeInfo("Consultando con Banquito...");
            
            CreditoEvaluacionRequest request = new CreditoEvaluacionRequest(cedula, monto, plazoMeses);
            CreditoEvaluacionResponse response = banquitoService.evaluarCredito(request);
            
            System.out.println();
            
            if (response.getCreditoAprobado() != null && response.getCreditoAprobado()) {
                ConsoleUI.mostrarMensajeExito("¡CRÉDITO APROBADO!");
                System.out.println();
                ConsoleUI.mostrarLinea();
                System.out.println("  " + ConsoleUI.BOLD + ConsoleUI.CYAN + "PLAN DE CRÉDITO" + ConsoleUI.RESET);
                ConsoleUI.mostrarLinea();
                System.out.println();
                System.out.println("  Cédula: " + cedula);
                System.out.println("  Monto financiado: " + ConsoleUI.GREEN + "$" + monto + ConsoleUI.RESET);
                System.out.println("  Plazo: " + plazoMeses + " meses");
                System.out.println("  Cuota mensual: " + ConsoleUI.BOLD + "$" + response.getCuotaMensual() + ConsoleUI.RESET);
                if (response.getIdCredito() != null) {
                    System.out.println("  ID Crédito: " + response.getIdCredito());
                }
                System.out.println();
                
                if (response.getTablaAmortizacion() != null && !response.getTablaAmortizacion().isEmpty()) {
                    ConsoleUI.mostrarLinea();
                    System.out.println("  " + ConsoleUI.BOLD + "TABLA DE AMORTIZACIÓN" + ConsoleUI.RESET);
                    ConsoleUI.mostrarLinea();
                    System.out.println();
                    
                    // Encabezado de la tabla
                    System.out.printf("  %-6s %-13s %-13s %-13s %-13s%n",
                        "Cuota", "Capital", "Interés", "Valor Cuota", "Saldo");
                    ConsoleUI.mostrarLinea();
                    
                    // Mostrar cada cuota
                    for (CuotaAmortizacion cuota : response.getTablaAmortizacion()) {
                        System.out.printf("  %-6d $%-12.2f $%-12.2f $%-12.2f $%-12.2f%n",
                            cuota.getNumeroCuota(),
                            cuota.getCapital(),
                            cuota.getInteres(),
                            cuota.getCuota(),
                            cuota.getSaldoFinal()
                        );
                    }
                    
                    ConsoleUI.mostrarLinea();
                    System.out.println();
                    
                    BigDecimal totalPagar = response.getCuotaMensual().multiply(new BigDecimal(plazoMeses));
                    BigDecimal totalIntereses = totalPagar.subtract(monto);
                    
                    System.out.println("  " + ConsoleUI.CYAN + "Total a pagar: $" + totalPagar + ConsoleUI.RESET);
                    System.out.println("  " + ConsoleUI.YELLOW + "Total intereses: $" + totalIntereses + ConsoleUI.RESET);
                }
                
                ConsoleUI.mostrarLinea();
            } else {
                ConsoleUI.mostrarMensajeError("CRÉDITO RECHAZADO");
                System.out.println();
                ConsoleUI.mostrarMensajeAdvertencia("Motivo: " + response.getMensaje());
            }
            
            ConsoleUI.pausa();
            
        } catch (NumberFormatException e) {
            System.out.println();
            ConsoleUI.mostrarMensajeError("Error: Formato de número inválido");
            ConsoleUI.pausa();
        } catch (Exception e) {
            System.out.println();
            ConsoleUI.mostrarMensajeError("Error al consultar tabla: " + e.getMessage());
            ConsoleUI.pausa();
        }
    }
}
