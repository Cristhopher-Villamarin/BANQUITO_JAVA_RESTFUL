package ec.edu.monster.controller;

import ec.edu.monster.dto.VentaRequest;
import ec.edu.monster.dto.VentaResponse;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.service.VentaService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/ventas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VentaController {
    
    private static final Logger LOGGER = Logger.getLogger(VentaController.class.getName());
    
    @Inject
    private VentaService ventaService;
    
    @GET
    @Path("/health")
    public Response health() {
        return Response.ok("{\"status\":\"OK\",\"message\":\"Comercializadora Service is running\"}").build();
    }
    
    @GET
    @Path("/electrodomesticos")
    public Response listarElectrodomesticos() {
        try {
            List<Electrodomestico> electrodomesticos = ventaService.listarElectrodomesticos();
            return Response.ok(electrodomesticos).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error listando electrodomésticos", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Error interno del servidor\"}")
                    .build();
        }
    }
    
    @POST
    @Path("/procesar")
    public Response procesarVenta(@Valid VentaRequest request) {
        if (request == null || request.getCedulaCliente() == null || request.getCedulaCliente().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Datos de venta inválidos\"}")
                    .build();
        }
        
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Debe incluir al menos un electrodoméstico\"}")
                    .build();
        }
        
        if (request.getFormaPago() == null || request.getFormaPago().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Debe especificar la forma de pago\"}")
                    .build();
        }
        
        try {
            VentaResponse response = ventaService.procesarVenta(request);
            
            if (response.isVentaExitosa()) {
                return Response.ok(response).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(response)
                        .build();
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error procesando venta", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Error interno del servidor\"}")
                    .build();
        }
    }
}
