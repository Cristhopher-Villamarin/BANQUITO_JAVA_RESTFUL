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

@WebServlet(name = "CatalogoServlet", urlPatterns = {"/user/catalogo"})
public class CatalogoServlet extends HttpServlet {

    private final ElectrodomesticoService electrodomesticoService = new ElectrodomesticoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        LoginResponse usuario = (LoginResponse) session.getAttribute("usuario");
        if (usuario == null || !usuario.isAutenticado()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            List<Electrodomestico> lista = electrodomesticoService.listarElectrodomesticos();
            request.setAttribute("electrodomesticos", lista);
        } catch (Exception e) {
            request.setAttribute("error", "Error al consultar catálogo: " + e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/views/catalogo.jsp").forward(request, response);
    }
}
