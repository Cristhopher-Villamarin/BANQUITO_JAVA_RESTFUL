package ec.edu.monster.controller;

import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.service.ElectrodomesticoService;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.List;

public class ElectrodomesticoController {
    private final ElectrodomesticoService electrodomesticoService;

    public ElectrodomesticoController() {
        this.electrodomesticoService = new ElectrodomesticoService();
    }

    public List<Electrodomestico> listarElectrodomesticos() throws IOException, ParseException {
        return electrodomesticoService.listarElectrodomesticos();
    }

    public Electrodomestico obtenerPorId(int id) throws IOException, ParseException {
        return electrodomesticoService.obtenerPorId(id);
    }

    public Electrodomestico crearElectrodomestico(String nombre, double precio) throws IOException, ParseException {
        return electrodomesticoService.crear(nombre, precio);
    }

    public Electrodomestico actualizarElectrodomestico(int id, String nombre, double precio) throws IOException, ParseException {
        return electrodomesticoService.actualizar(id, nombre, precio);
    }

    public String eliminarElectrodomestico(int id) throws IOException, ParseException {
        return electrodomesticoService.eliminar(id);
    }
}
