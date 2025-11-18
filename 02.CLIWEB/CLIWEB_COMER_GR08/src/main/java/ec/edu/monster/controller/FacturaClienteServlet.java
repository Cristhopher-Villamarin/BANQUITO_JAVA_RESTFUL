package ec.edu.monster.controller;

import ec.edu.monster.model.Factura;
import ec.edu.monster.model.FacturaDetalleResponse;
import ec.edu.monster.model.LoginResponse;
import ec.edu.monster.service.FacturaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "FacturaClienteServlet", urlPatterns = {"/user/facturas/cliente"})
public class FacturaClienteServlet extends HttpServlet {

    private final FacturaService facturaService = new FacturaService();

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
        String idFacturaStr = request.getParameter("idFactura");
        request.setAttribute("cedula", cedula);

        try {
            if (cedula != null && !cedula.isBlank()) {
                List<Factura> facturas = facturaService.listarFacturasPorCedula(cedula);
                request.setAttribute("facturas", facturas);
            }

            if (idFacturaStr != null && !idFacturaStr.isBlank() && idFacturaStr.matches("\\d+")) {
                int idFactura = Integer.parseInt(idFacturaStr);
                FacturaDetalleResponse detalleResponse = facturaService.obtenerFacturaConDetalles(idFactura);
                if (detalleResponse != null && detalleResponse.getFactura() != null) {
                    Factura facturaSeleccionada = detalleResponse.getFactura();
                    facturaSeleccionada.setDetalles(detalleResponse.getDetalles());
                    request.setAttribute("facturaSeleccionada", facturaSeleccionada);
                }
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error al consultar facturas: " + e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/views/facturas-cliente.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
