package ec.edu.monster.controller;

import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.model.LoginResponse;
import ec.edu.monster.service.ElectrodomesticoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminElectrodomesticoServlet", urlPatterns = {"/admin/electrodomesticos"})
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
                electrodomesticoService.crear(nombre, precio);
            } else if ("actualizar".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String nombre = request.getParameter("nombre");
                double precio = Double.parseDouble(request.getParameter("precio"));
                electrodomesticoService.actualizar(id, nombre, precio);
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
}
