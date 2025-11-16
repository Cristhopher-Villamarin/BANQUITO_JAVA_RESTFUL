package ec.edu.monster.controller;

import ec.edu.monster.model.CreditoEvaluacionResponse;
import ec.edu.monster.model.LoginResponse;
import ec.edu.monster.service.BanquitoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "CreditoEstadoServlet", urlPatterns = {"/user/credito/estado"})
public class CreditoEstadoServlet extends HttpServlet {

    private final BanquitoService banquitoService = new BanquitoService();

    private LoginResponse requireUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        LoginResponse usuario = (LoginResponse) session.getAttribute("usuario");
        if (usuario == null || !usuario.isAutenticado() || !"USER".equalsIgnoreCase(usuario.getRol())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        return usuario;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoginResponse usuario = requireUser(request, response);
        if (usuario == null) {
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/credito-estado.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoginResponse usuario = requireUser(request, response);
        if (usuario == null) {
            return;
        }

        String cedula = request.getParameter("cedula");
        request.setAttribute("cedula", cedula);

        try {
            CreditoEvaluacionResponse evaluacion = banquitoService.verificarSujetoCredito(cedula);
            request.setAttribute("evaluacion", evaluacion);
        } catch (Exception e) {
            request.setAttribute("error", "Error al verificar crédito: " + e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/views/credito-estado.jsp").forward(request, response);
    }
}
