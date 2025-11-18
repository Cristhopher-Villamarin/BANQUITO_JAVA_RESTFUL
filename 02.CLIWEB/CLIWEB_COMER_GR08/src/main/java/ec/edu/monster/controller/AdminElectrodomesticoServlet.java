package ec.edu.monster.controller;

import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.model.LoginResponse;
import ec.edu.monster.service.ElectrodomesticoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet(name = "AdminElectrodomesticoServlet", urlPatterns = {"/admin/electrodomesticos"})
@MultipartConfig
public class AdminElectrodomesticoServlet extends HttpServlet {

    private final ElectrodomesticoService electrodomesticoService = new ElectrodomesticoService();

    private boolean isAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        LoginResponse usuario = (LoginResponse) session.getAttribute("usuario");
        if (usuario == null || !usuario.isAutenticado() || !"ADMIN".equalsIgnoreCase(usuario.getRol())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdmin(request, response)) {
            return;
        }

        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            try {
                List<Electrodomestico> lista = electrodomesticoService.listarElectrodomesticos();
                request.setAttribute("electrodomesticos", lista);
            } catch (Exception e) {
                request.setAttribute("error", "Error al listar electrodomésticos: " + e.getMessage());
            }
            request.getRequestDispatcher("/WEB-INF/views/admin-electrodomesticos.jsp").forward(request, response);
        } else if ("nuevo".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/views/admin-electrodomestico-form.jsp").forward(request, response);
        } else if ("editar".equals(action)) {
            String idStr = request.getParameter("id");
            try {
                int id = Integer.parseInt(idStr);
                Electrodomestico e = electrodomesticoService.obtenerPorId(id);
                request.setAttribute("electrodomestico", e);
            } catch (Exception e) {
                request.setAttribute("error", "Error al cargar electrodoméstico: " + e.getMessage());
            }
            request.getRequestDispatcher("/WEB-INF/views/admin-electrodomestico-form.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/electrodomesticos");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdmin(request, response)) {
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("crear".equals(action)) {
                String nombre = request.getParameter("nombre");
                double precio = Double.parseDouble(request.getParameter("precio"));

                String fotoUrl = null;
                Part fotoPart = request.getPart("foto");
                if (fotoPart != null && fotoPart.getSize() > 0) {
                    byte[] bytes = readAllBytes(fotoPart.getInputStream());
                    fotoUrl = electrodomesticoService.subirFoto(bytes, fotoPart.getSubmittedFileName());
                }

                electrodomesticoService.crear(nombre, precio, fotoUrl);
            } else if ("actualizar".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String nombre = request.getParameter("nombre");
                double precio = Double.parseDouble(request.getParameter("precio"));

                Electrodomestico existente = electrodomesticoService.obtenerPorId(id);
                String fotoUrlActual = existente != null ? existente.getFotoUrl() : null;

                String fotoUrl = fotoUrlActual;
                Part fotoPart = request.getPart("foto");
                if (fotoPart != null && fotoPart.getSize() > 0) {
                    byte[] bytes = readAllBytes(fotoPart.getInputStream());
                    fotoUrl = electrodomesticoService.subirFoto(bytes, fotoPart.getSubmittedFileName());
                }

                electrodomesticoService.actualizar(id, nombre, precio, fotoUrl);
            } else if ("eliminar".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                electrodomesticoService.eliminar(id);
            }
            response.sendRedirect(request.getContextPath() + "/admin/electrodomesticos");
        } catch (Exception e) {
            request.setAttribute("error", "Error al procesar la operación: " + e.getMessage());
            doGet(request, response);
        }
    }

    private byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int nRead;
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }
}
