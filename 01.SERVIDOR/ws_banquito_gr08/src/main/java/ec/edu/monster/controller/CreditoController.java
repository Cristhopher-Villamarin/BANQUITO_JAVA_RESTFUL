package ec.edu.monster.controller;

import ec.edu.monster.dto.EvaluacionCreditoRequest;
import ec.edu.monster.dto.EvaluacionCreditoResponse;
import ec.edu.monster.service.CreditoService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/creditos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CreditoController {
    
    private static final Logger LOGGER = Logger.getLogger(CreditoController.class.getName());
    private final CreditoService creditoService = new CreditoService();
    
    @POST
    @Path("/evaluar")
    public Response evaluarCredito(EvaluacionCreditoRequest request) {
        try {
            // Validar plazos
            if (request.getPlazoMeses() < 3 || request.getPlazoMeses() > 24) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"mensaje\":\"El plazo debe estar entre 3 y 24 meses\"}")
                        .build();
            }
            
            // Validar monto
            if (request.getMontoElectrodomestico().doubleValue() <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"mensaje\":\"El monto del electrodoméstico debe ser mayor a 0\"}")
                        .build();
            }
            
            EvaluacionCreditoResponse response = creditoService.evaluarCredito(request);
            
            return Response.ok(response).build();
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error en evaluación de crédito", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Error interno del servidor\"}")
                    .build();
        }
    }
    
    @GET
    @Path("/cliente/{cedula}/sujeto-credito")
    public Response verificarSujetoCredito(@PathParam("cedula") String cedula) {
        try {
            EvaluacionCreditoResponse response = creditoService.verificarSujetoCredito(cedula);
            
            return Response.ok(response).build();
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error verificando sujeto de crédito", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Error interno del servidor\"}")
                    .build();
        }
    }
    
    @GET
    @Path("/cliente/{cedula}/monto-maximo")
    public Response obtenerMontoMaximo(@PathParam("cedula") String cedula) {
        try {
            EvaluacionCreditoRequest request = new EvaluacionCreditoRequest();
            request.setCedula(cedula);
            request.setMontoElectrodomestico(java.math.BigDecimal.ZERO);
            request.setPlazoMeses(3);
            
            EvaluacionCreditoResponse response = creditoService.evaluarCredito(request);
            
            if (response.isSujetoCredito()) {
                return Response.ok(response.getMontoMaximoCredito()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\":\"El cliente no es sujeto de crédito\"}")
                        .build();
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error obteniendo monto máximo", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Error interno del servidor\"}")
                    .build();
        }
    }
}
