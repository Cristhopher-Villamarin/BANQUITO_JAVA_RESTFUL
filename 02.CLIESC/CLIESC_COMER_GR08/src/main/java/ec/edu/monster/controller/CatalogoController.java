package ec.edu.monster.controller;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.service.ElectrodomesticoService;
import java.util.List;

public class CatalogoController {

    private final ElectrodomesticoService electrodomesticoService = new ElectrodomesticoService();

   
    public List<Electrodomestico> obtenerCatalogo() throws Exception {
        return electrodomesticoService.listarElectrodomesticos();
    }
}