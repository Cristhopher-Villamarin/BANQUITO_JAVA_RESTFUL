package ec.edu.monster.controller;

import ec.edu.monster.model.DetalleFactura;
import ec.edu.monster.model.Factura;
import ec.edu.monster.service.VentaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/facturas")
@Produces(MediaType.APPLICATION_JSON)
public class FacturaController {

    private static final Logger LOGGER = Logger.getLogger(FacturaController.class.getName());

    @Inject
    private VentaService ventaService;

    @GET
    @Path("/cliente/{cedula}")
    public Response listarFacturasPorCedula(@PathParam("cedula") String cedula) {
        try {
            List<Factura> facturas = ventaService.listarFacturasPorCedula(cedula);
            if (facturas == null || facturas.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\":\"No se encontraron facturas para la cédula proporcionada\"}")
                        .build();
            }
            return Response.ok(facturas).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error listando facturas por cédula", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Error interno del servidor\"}")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerFacturaConDetalles(@PathParam("id") Integer id) {
        try {
            Factura factura = ventaService.obtenerFacturaPorId(id);
            if (factura == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\":\"Factura no encontrada\"}")
                        .build();
            }
            List<DetalleFactura> detalles = ventaService.listarDetallesPorFactura(id);
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("factura", factura);
            resultado.put("detalles", detalles);
            return Response.ok(resultado).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error obteniendo factura con detalles", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Error interno del servidor\"}")
                    .build();
        }
    }
}
