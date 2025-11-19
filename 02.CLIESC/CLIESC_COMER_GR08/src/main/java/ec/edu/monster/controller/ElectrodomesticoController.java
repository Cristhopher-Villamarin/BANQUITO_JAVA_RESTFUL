package ec.edu.monster.controller;

import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.service.ElectrodomesticoService;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class ElectrodomesticoController {

    private final ElectrodomesticoService electrodomesticoService = new ElectrodomesticoService();

    /** LISTAR */
    public List<Electrodomestico> listar() throws Exception {
        return electrodomesticoService.listarElectrodomesticos();
    }

    /** OBTENER POR ID */
    public Electrodomestico obtenerPorId(int id) throws Exception {
        return electrodomesticoService.obtenerPorId(id);
    }

    /** CREAR */
    public void crear(String nombre, double precio, byte[] fotoBytes, String nombreArchivo) throws Exception {
        String fotoUrl = null;

        if (fotoBytes != null && fotoBytes.length > 0) {
            fotoUrl = electrodomesticoService.subirFoto(fotoBytes, nombreArchivo);
        }

        electrodomesticoService.crear(nombre, precio, fotoUrl);
    }

    /** ACTUALIZAR */
    public void actualizar(int id, String nombre, double precio, byte[] fotoBytes, String nombreArchivo) throws Exception {

        Electrodomestico actual = electrodomesticoService.obtenerPorId(id);

        String fotoUrl = (actual != null) ? actual.getFotoUrl() : null;

        // Si el usuario seleccionó una nueva foto
        if (fotoBytes != null && fotoBytes.length > 0) {
            fotoUrl = electrodomesticoService.subirFoto(fotoBytes, nombreArchivo);
        }

        electrodomesticoService.actualizar(id, nombre, precio, fotoUrl);
    }

    /** ELIMINAR */
    public void eliminar(int id) throws Exception {
        electrodomesticoService.eliminar(id);
    }

    /** LEER BYTES (para cargar imágenes de JFileChooser) */
    public byte[] readAllBytes(InputStream inputStream) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int nRead;

        while ((nRead = inputStream.read(data)) != -1) {
            buffer.write(data, 0, nRead);
        }

        return buffer.toByteArray();
    }
}
