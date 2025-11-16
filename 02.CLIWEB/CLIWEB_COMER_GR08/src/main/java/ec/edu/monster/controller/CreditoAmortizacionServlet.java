package ec.edu.monster.controller;

import ec.edu.monster.model.Credito;
import ec.edu.monster.model.LoginResponse;
import ec.edu.monster.model.TablaAmortizacion;
import ec.edu.monster.service.BanquitoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CreditoAmortizacionServlet", urlPatterns = {"/user/credito/amortizacion"})
public class CreditoAmortizacionServlet extends HttpServlet {

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

        String cedula = request.getParameter("cedula");
        String idCreditoStr = request.getParameter("idCredito");

        if (idCreditoStr != null && !idCreditoStr.isBlank()) {
            try {
                Integer idCredito = Integer.valueOf(idCreditoStr);
                TablaAmortizacion tabla = banquitoService.obtenerTablaAmortizacion(idCredito);
                request.setAttribute("tabla", tabla);
                request.setAttribute("idCredito", idCredito);
            } catch (Exception e) {
                request.setAttribute("error", "Error al obtener tabla de amortización: " + e.getMessage());
            }
        } else if (cedula != null && !cedula.isBlank()) {
            try {
                List<Credito> creditos = banquitoService.obtenerCreditosActivos(cedula);
                request.setAttribute("creditos", creditos);
                request.setAttribute("cedula", cedula);
            } catch (Exception e) {
                request.setAttribute("error", "Error al obtener créditos activos: " + e.getMessage());
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/credito-amortizacion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
