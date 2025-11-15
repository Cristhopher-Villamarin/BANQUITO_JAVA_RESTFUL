package ec.edu.monster.filter;

import ec.edu.monster.service.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Provider
public class AuthFilter implements ContainerRequestFilter {
    
    private static final Logger LOGGER = Logger.getLogger(AuthFilter.class.getName());
    
    @Inject
    private AuthService authService;
    
    private final List<String> publicPaths = Arrays.asList(
        "/api/auth/login",
        "/api/auth/health",
        "/api/auth/usuarios",
        "/api/ventas/health",
        "/api/ventas/electrodomesticos"
    );
    
    @Override
    public void filter(ContainerRequestContext requestContext) {
        String path = requestContext.getUriInfo().getPath();
        String method = requestContext.getMethod();
        
        LOGGER.info("Request: " + method + " " + path);
        
        // Permitir rutas públicas
        if (isPublicPath(path, method)) {
            return;
        }
        
        // Verificar autenticación
        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            LOGGER.warning("Token no proporcionado para ruta protegida: " + path);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity(createErrorResponse("Token de autenticación requerido"))
                .build());
            return;
        }
        
        String token = authHeader.substring("Bearer ".length());
        String username = extractUsernameFromToken(token);
        
        if (username == null || !authService.esUsuarioValido(username)) {
            LOGGER.warning("Token inválido o usuario no válido: " + username);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity(createErrorResponse("Token inválido"))
                .build());
            return;
        }
        
        // Verificar permisos para CRUD de electrodomésticos
        if (isElectrodomesticoCrudPath(path) && !authService.esAdmin(username)) {
            LOGGER.warning("Usuario sin permisos de admin intentando CRUD: " + username + " en " + path);
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                .entity(createErrorResponse("Se requieren permisos de administrador para esta operación"))
                .build());
            return;
        }
        
        // Agregar información del usuario al contexto
        requestContext.setProperty("username", username);
        LOGGER.info("Usuario autenticado: " + username + " accediendo a: " + path);
    }
    
    private boolean isPublicPath(String path, String method) {
        return publicPaths.stream().anyMatch(publicPath -> 
            path.equals(publicPath) || 
            (path.startsWith("/api/ventas") && method.equals("GET"))
        );
    }
    
    private boolean isElectrodomesticoCrudPath(String path) {
        return (path.startsWith("/api/electrodomesticos") && 
                !path.equals("/api/electrodomesticos/health")) ||
               (path.startsWith("/api/electrodomesticos") && 
                (path.contains("/create") || path.contains("/update") || path.contains("/delete")));
    }
    
    private String extractUsernameFromToken(String token) {
        if (token == null || !token.startsWith("TOKEN_")) {
            return null;
        }
        
        String[] parts = token.split("_");
        if (parts.length >= 2) {
            return parts[1];
        }
        
        return null;
    }
    
    private Object createErrorResponse(String message) {
        return new java.util.HashMap<String, Object>() {{
            put("error", message);
            put("timestamp", java.time.LocalDateTime.now());
        }};
    }
}
