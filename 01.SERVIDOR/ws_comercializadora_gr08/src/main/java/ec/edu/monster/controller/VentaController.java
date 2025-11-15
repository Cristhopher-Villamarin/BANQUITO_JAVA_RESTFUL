package ec.edu.monster.controller;

import ec.edu.monster.dto.VentaRequest;
import ec.edu.monster.dto.VentaResponse;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.service.VentaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/ventas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VentaController {
    
    private final VentaService ventaService = new VentaService();
    
    @POST
    @Path("/procesar")
    public Response procesarVenta(VentaRequest request) {
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
        
        VentaResponse response = ventaService.procesarVenta(request);
        
        if (response.isVentaExitosa()) {
            return Response.status(Response.Status.CREATED).entity(response).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }
    
    @GET
    @Path("/electrodomesticos")
    public Response listarElectrodomesticos() {
        List<Electrodomestico> electrodomesticos = ventaService.listarElectrodomesticos();
        return Response.ok(electrodomesticos).build();
    }
    
    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    public Response healthCheck() {
        return Response.ok("Comercializadora service is running").build();
    }
}
