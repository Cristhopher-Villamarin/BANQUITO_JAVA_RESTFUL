package ec.edu.monster.controller;

import ec.edu.monster.dto.ElectrodomesticoRequest;
import ec.edu.monster.dto.ElectrodomesticoResponse;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.service.ElectrodomesticoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.logging.Level;
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
        return Response.ok("{\"status\":\"OK\",\"message\":\"Electrodomestico Service is running\"}").build();
    }
    
    @GET
    public Response listarTodos() {
        try {
            List<ElectrodomesticoResponse> electrodomesticos = electrodomesticoService.listarTodos();
            return Response.ok(electrodomesticos).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error listando electrodomésticos", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Error interno del servidor\"}")
                    .build();
        }
    }
    
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Integer id) {
        try {
            ElectrodomesticoResponse electrodomestico = electrodomesticoService.buscarPorId(id);
            if (electrodomestico == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\":\"Electrodoméstico no encontrado\"}")
                        .build();
            }
            return Response.ok(electrodomestico).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error buscando electrodoméstico", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Error interno del servidor\"}")
                    .build();
        }
    }
    
    @POST
    public Response crear(@Valid ElectrodomesticoRequest request) {
        try {
            ElectrodomesticoResponse electrodomestico = electrodomesticoService.crear(request);
            return Response.status(Response.Status.CREATED).entity(electrodomestico).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creando electrodoméstico", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Error interno del servidor\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Integer id, @Valid ElectrodomesticoRequest request) {
        try {
            ElectrodomesticoResponse electrodomestico = electrodomesticoService.actualizar(id, request);
            if (electrodomestico == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\":\"Electrodoméstico no encontrado\"}")
                        .build();
            }
            return Response.ok(electrodomestico).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error actualizando electrodoméstico", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Error interno del servidor\"}")
                    .build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") Integer id) {
        try {
            boolean eliminado = electrodomesticoService.eliminar(id);
            if (!eliminado) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\":\"Electrodoméstico no encontrado\"}")
                        .build();
            }
            return Response.ok("{\"mensaje\":\"Electrodoméstico eliminado exitosamente\"}").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error eliminando electrodoméstico", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Error interno del servidor\"}")
                    .build();
        }
    }
}
