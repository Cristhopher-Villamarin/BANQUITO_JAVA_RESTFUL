package ec.edu.monster.controller;

import ec.edu.monster.dto.LoginRequest;
import ec.edu.monster.dto.LoginResponse;
import ec.edu.monster.service.AuthService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {
    
    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());
    
    @Inject
    private AuthService authService;
    
    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest loginRequest) {
        try {
            LOGGER.info("Intento de login para usuario: " + loginRequest.getUsername());
            
            LoginResponse response = authService.autenticar(loginRequest);
            
            if (response.isAutenticado()) {
                LOGGER.info("Login exitoso para: " + loginRequest.getUsername());
                return Response.status(Response.Status.OK)
                    .entity(response)
                    .build();
            } else {
                LOGGER.warning("Login fallido para: " + loginRequest.getUsername());
                return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(response)
                    .build();
            }
            
        } catch (Exception e) {
            LOGGER.severe("Error en login: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(LoginResponse.fallido("Error interno del servidor"))
                .build();
        }
    }
    
    @GET
    @Path("/health")
    public Response health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Auth Service is running");
        response.put("timestamp", java.time.LocalDateTime.now());
        
        return Response.ok(response).build();
    }
    
    @GET
    @Path("/usuarios")
    public Response listarUsuarios() {
        Map<String, Object> response = new HashMap<>();
        response.put("usuarios", Map.of(
            "MONSTER", "USER (Solo ventas, sin CRUD)",
            "admin", "ADMIN (Acceso completo including CRUD)"
        ));
        response.put("timestamp", java.time.LocalDateTime.now());
        
        return Response.ok(response).build();
    }
}
