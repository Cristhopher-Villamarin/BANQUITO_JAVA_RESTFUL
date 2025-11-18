package ec.edu.monster.config;

import ec.edu.monster.controller.VentaController;
import ec.edu.monster.controller.ElectrodomesticoController;
import ec.edu.monster.controller.AuthController;
import ec.edu.monster.controller.FacturaController;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class JerseyConfig extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(VentaController.class);
        resources.add(ElectrodomesticoController.class);
        resources.add(AuthController.class);
        resources.add(FacturaController.class);
        return resources;
    }
}
