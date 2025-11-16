package ec.edu.monster.controller;

import ec.edu.monster.dto.AmortizacionDTO;
import ec.edu.monster.dto.CuotaAmortizacionResponse;
import ec.edu.monster.dto.CreditoResponse;
import ec.edu.monster.dto.EvaluacionCreditoRequest;
import ec.edu.monster.dto.EvaluacionCreditoResponse;
import ec.edu.monster.dto.TablaAmortizacionResponse;
import ec.edu.monster.model.Credito;
import ec.edu.monster.model.Cliente;
import ec.edu.monster.service.CreditoService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
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
    @Path("/cliente/{cedula}/creditos-activos")
    public Response obtenerCreditosActivos(@PathParam("cedula") String cedula) {
        try {
            List<CreditoResponse> creditos = creditoService.obtenerCreditosActivosPorCliente(cedula);
            
            return Response.ok(creditos).build();
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error obteniendo créditos activos del cliente", e);
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
    
    @GET
    @Path("/{idCredito}/tabla-amortizacion")
    public Response obtenerTablaAmortizacion(@PathParam("idCredito") Integer idCredito) {
        try {
            List<AmortizacionDTO> tabla = creditoService.obtenerTablaAmortizacion(idCredito);
            
            if (tabla.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\": \"No se encontró tabla de amortización para el crédito ID: " + idCredito + "\"}")
                        .build();
            }
            
            return Response.ok(tabla).build();
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error obteniendo tabla de amortización", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Error interno del servidor\"}")
                    .build();
        }
    }
    
    @GET
    @Path("/amortizacion/{idCredito}")
    public Response obtenerTablaAmortizacionComercializadora(@PathParam("idCredito") Integer idCredito) {
        try {
            TablaAmortizacionResponse tabla = creditoService.obtenerTablaAmortizacionParaComercializadora(idCredito);
            
            if (tabla == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\": \"No se encontró tabla de amortización para el crédito ID: " + idCredito + "\"}")
                        .build();
            }
            
            return Response.ok(tabla).build();
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error obteniendo tabla de amortización para comercializadora", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Error interno del servidor\"}")
                    .build();
        }
    }
}
