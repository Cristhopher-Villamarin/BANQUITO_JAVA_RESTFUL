package ec.edu.monster.controller;

import ec.edu.monster.dto.ElectrodomesticoRequest;
import ec.edu.monster.dto.ElectrodomesticoResponse;
import ec.edu.monster.service.ElectrodomesticoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Path("/electrodomesticos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ElectrodomesticoController {
    
    private static final Logger LOGGER = Logger.getLogger(ElectrodomesticoController.class.getName());
    
    @Inject
    private ElectrodomesticoService electrodomesticoService;
    
    @GET
    @Path("/health")
    public Response health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Electrodomestico Service is running");
        response.put("timestamp", java.time.LocalDateTime.now());
        return Response.ok(response).build();
    }
    
    @GET
    public Response listarTodos() {
        try {
            LOGGER.info("Listando todos los electrodomésticos");
            List<ElectrodomesticoResponse> electrodomesticos = electrodomesticoService.listarTodos();
            return Response.ok(electrodomesticos).build();
        } catch (Exception e) {
            LOGGER.severe("Error listando electrodomésticos: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(createErrorResponse("Error listando electrodomésticos"))
                .build();
        }
    }
    
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Integer id) {
        try {
            LOGGER.info("Buscando electrodoméstico por ID: " + id);
            ElectrodomesticoResponse electrodomestico = electrodomesticoService.buscarPorId(id);
            if (electrodomestico == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(createErrorResponse("Electrodoméstico no encontrado"))
                    .build();
            }
            return Response.ok(electrodomestico).build();
        } catch (Exception e) {
            LOGGER.severe("Error buscando electrodoméstico: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(createErrorResponse("Error buscando electrodoméstico"))
                .build();
        }
    }
    
    @POST
    public Response crear(@Valid ElectrodomesticoRequest request) {
        try {
            LOGGER.info("Creando electrodoméstico: " + request.getNombre());
            ElectrodomesticoResponse creado = electrodomesticoService.crear(request);
            return Response.status(Response.Status.CREATED)
                .entity(creado)
                .build();
        } catch (Exception e) {
            LOGGER.severe("Error creando electrodoméstico: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(createErrorResponse("Error creando electrodoméstico"))
                .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Integer id, @Valid ElectrodomesticoRequest request) {
        try {
            LOGGER.info("Actualizando electrodoméstico ID: " + id);
            ElectrodomesticoResponse actualizado = electrodomesticoService.actualizar(id, request);
            if (actualizado == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(createErrorResponse("Electrodoméstico no encontrado"))
                    .build();
            }
            return Response.ok(actualizado).build();
        } catch (Exception e) {
            LOGGER.severe("Error actualizando electrodoméstico: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(createErrorResponse("Error actualizando electrodoméstico"))
                .build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") Integer id) {
        try {
            LOGGER.info("Eliminando electrodoméstico ID: " + id);
            boolean eliminado = electrodomesticoService.eliminar(id);
            if (!eliminado) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(createErrorResponse("Electrodoméstico no encontrado"))
                    .build();
            }
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Electrodoméstico eliminado exitosamente");
            return Response.ok(response).build();
        } catch (IllegalStateException e) {
            LOGGER.warning("Error eliminando electrodoméstico: " + e.getMessage());
            return Response.status(Response.Status.CONFLICT)
                .entity(createErrorResponse(e.getMessage()))
                .build();
        } catch (Exception e) {
            LOGGER.severe("Error eliminando electrodoméstico: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(createErrorResponse("Error eliminando electrodoméstico"))
                .build();
        }
    }
    
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", message);
        error.put("timestamp", java.time.LocalDateTime.now());
        return error;
    }
}
