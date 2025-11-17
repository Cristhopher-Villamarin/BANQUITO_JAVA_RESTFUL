/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.controller;

import ec.edu.monster.model.DetalleVentaRequest;
import ec.edu.monster.model.VentaRequest;
import ec.edu.monster.model.VentaResponse;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.service.BanquitoService;
import ec.edu.monster.service.ElectrodomesticoService;
import ec.edu.monster.service.VentaService;

import java.util.List;

public class VentaController {

    private final ElectrodomesticoService electrodomesticoService = new ElectrodomesticoService();
    private final VentaService ventaService = new VentaService();
    private final BanquitoService banquitoService = new BanquitoService();

    /**
     * Devuelve el catálogo de electrodomésticos disponible para la venta.
     */
    public List<Electrodomestico> obtenerCatalogo() throws Exception {
        return electrodomesticoService.listarElectrodomesticos();
    }

    /**
     * Procesa una venta validando forma de pago, productos y, si aplica,
     * la evaluación de crédito en Banquito.
     *
     * @param ventaRequest datos de la venta (cliente, forma de pago, plazo, detalles)
     * @return respuesta con el resultado de la venta
     * @throws Exception si ocurre un error de negocio o de conexión
     */
    public VentaResponse procesarVenta(VentaRequest ventaRequest) throws Exception {
        if (ventaRequest == null) {
            throw new IllegalArgumentException("Los datos de la venta no pueden ser nulos.");
        }

        // Validar detalles
        List<DetalleVentaRequest> detalles = ventaRequest.getDetalles();
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("Debe agregar al menos un producto a la venta.");
        }

        // Validar forma de pago
        String formaPago = ventaRequest.getFormaPago();
        if (!"EFECTIVO".equals(formaPago) && !"CREDITO_DIRECTO".equals(formaPago)) {
            throw new IllegalArgumentException("Forma de pago no válida.");
        }

        // Si es crédito directo, validar sujeto de crédito en Banquito
        if ("CREDITO_DIRECTO".equals(formaPago)) {
            String cedula = ventaRequest.getCedulaCliente();
            var evaluacion = banquitoService.verificarSujetoCredito(cedula);

            if (evaluacion == null || evaluacion.getSujetoCredito() == null || !evaluacion.getSujetoCredito()) {
                String mensaje = (evaluacion != null && evaluacion.getMensaje() != null)
                        ? evaluacion.getMensaje()
                        : "El cliente no es sujeto de crédito";
                throw new IllegalStateException(mensaje);
            }
        }

        // Si pasa las validaciones, delega al servicio de venta
        return ventaService.procesarVenta(ventaRequest);
    }
}
