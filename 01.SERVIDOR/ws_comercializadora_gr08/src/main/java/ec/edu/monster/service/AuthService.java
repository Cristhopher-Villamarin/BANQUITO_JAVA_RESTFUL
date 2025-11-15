package ec.edu.monster.service;

import ec.edu.monster.dto.LoginRequest;
import ec.edu.monster.dto.LoginResponse;
import ec.edu.monster.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class AuthService {
    
    private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());
    
    private final Map<String, Usuario> usuarios;
    
    public AuthService() {
        usuarios = new HashMap<>();
        inicializarUsuarios();
    }
    
    private void inicializarUsuarios() {
        // Usuario MONSTER - Solo puede hacer operaciones de venta, no CRUD
        Usuario monster = new Usuario("MONSTER", "MONSTER9", "USER");
        usuarios.put("MONSTER", monster);
        
        // Usuario admin - Puede hacer CRUD completo
        Usuario admin = new Usuario("admin", "admin", "ADMIN");
        usuarios.put("admin", admin);
        
        LOGGER.info("Usuarios inicializados: MONSTER (USER), admin (ADMIN)");
    }
    
    public LoginResponse autenticar(LoginRequest request) {
        try {
            Usuario usuario = usuarios.get(request.getUsername());
            
            if (usuario == null) {
                LOGGER.warning("Usuario no encontrado: " + request.getUsername());
                return LoginResponse.fallido("Usuario no encontrado");
            }
            
            if (!usuario.isActivo()) {
                LOGGER.warning("Usuario inactivo: " + request.getUsername());
                return LoginResponse.fallido("Usuario inactivo");
            }
            
            if (!usuario.getPassword().equals(request.getPassword())) {
                LOGGER.warning("Contraseña incorrecta para usuario: " + request.getUsername());
                return LoginResponse.fallido("Contraseña incorrecta");
            }
            
            LOGGER.info("Autenticación exitosa para: " + request.getUsername() + " con rol: " + usuario.getRol());
            return LoginResponse.exitoso(usuario.getUsername(), usuario.getRol());
            
        } catch (Exception e) {
            LOGGER.severe("Error en autenticación: " + e.getMessage());
            return LoginResponse.fallido("Error en el proceso de autenticación");
        }
    }
    
    public boolean tieneRol(String username, String rolRequerido) {
        Usuario usuario = usuarios.get(username);
        return usuario != null && usuario.tieneRol(rolRequerido);
    }
    
    public boolean esAdmin(String username) {
        return tieneRol(username, "ADMIN");
    }
    
    public boolean esUsuarioValido(String username) {
        Usuario usuario = usuarios.get(username);
        return usuario != null && usuario.isActivo();
    }
}
