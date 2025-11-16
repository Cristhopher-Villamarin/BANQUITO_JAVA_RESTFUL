package ec.edu.monster.controller;

import ec.edu.monster.model.DetalleVentaRequest;
import ec.edu.monster.model.LoginResponse;
import ec.edu.monster.model.VentaRequest;
import ec.edu.monster.model.VentaResponse;
import ec.edu.monster.service.BanquitoService;
import ec.edu.monster.service.ElectrodomesticoService;
import ec.edu.monster.service.VentaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "VentaServlet", urlPatterns = {"/user/venta"})
public class VentaServlet extends HttpServlet {

    private final ElectrodomesticoService electrodomesticoService = new ElectrodomesticoService();
    private final VentaService ventaService = new VentaService();
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

        try {
            request.setAttribute("electrodomesticos", electrodomesticoService.listarElectrodomesticos());
        } catch (Exception e) {
            request.setAttribute("error", "Error al cargar catálogo: " + e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/views/venta.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoginResponse usuario = requireUser(request, response);
        if (usuario == null) {
            return;
        }

        String cedula = request.getParameter("cedula");
        String nombre = request.getParameter("nombre");
        String formaPago = request.getParameter("formaPago");
        String plazoStr = request.getParameter("plazoMeses");

        Integer plazoMeses = null;
        if (plazoStr != null && !plazoStr.isBlank()) {
            try {
                plazoMeses = Integer.parseInt(plazoStr);
            } catch (NumberFormatException ignored) {
            }
        }

        String[] ids = request.getParameterValues("productoId");
        String[] cantidades = request.getParameterValues("cantidad");

        List<DetalleVentaRequest> detalles = new ArrayList<>();
        if (ids != null && cantidades != null) {
            for (int i = 0; i < ids.length; i++) {
                String idStr = ids[i];
                String cantStr = cantidades[i];
                if (idStr == null || cantStr == null || idStr.isBlank() || cantStr.isBlank()) {
                    continue;
                }
                try {
                    int id = Integer.parseInt(idStr);
                    int cantidad = Integer.parseInt(cantStr);
                    if (cantidad > 0) {
                        detalles.add(new DetalleVentaRequest(id, cantidad));
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        if (detalles.isEmpty()) {
            request.setAttribute("error", "Debe agregar al menos un producto.");
            recargarCatalogo(request);
            request.getRequestDispatcher("/WEB-INF/views/venta.jsp").forward(request, response);
            return;
        }

        if (!"EFECTIVO".equals(formaPago) && !"CREDITO_DIRECTO".equals(formaPago)) {
            request.setAttribute("error", "Forma de pago no válida.");
            recargarCatalogo(request);
            request.getRequestDispatcher("/WEB-INF/views/venta.jsp").forward(request, response);
            return;
        }

        if ("CREDITO_DIRECTO".equals(formaPago)) {
            try {
                var evaluacion = banquitoService.verificarSujetoCredito(cedula);
                if (evaluacion == null || evaluacion.getSujetoCredito() == null || !evaluacion.getSujetoCredito()) {
                    String mensaje = evaluacion != null && evaluacion.getMensaje() != null ? evaluacion.getMensaje() : "El cliente no es sujeto de crédito";
                    request.setAttribute("error", mensaje);
                    recargarCatalogo(request);
                    request.getRequestDispatcher("/WEB-INF/views/venta.jsp").forward(request, response);
                    return;
                }
            } catch (Exception e) {
                request.setAttribute("error", "Error al verificar crédito: " + e.getMessage());
                recargarCatalogo(request);
                request.getRequestDispatcher("/WEB-INF/views/venta.jsp").forward(request, response);
                return;
            }
        }

        try {
            VentaRequest ventaRequest = new VentaRequest();
            ventaRequest.setCedulaCliente(cedula);
            ventaRequest.setNombreCliente(nombre);
            ventaRequest.setFormaPago(formaPago);
            ventaRequest.setPlazoMeses(plazoMeses);
            ventaRequest.setDetalles(detalles);

            VentaResponse ventaResponse = ventaService.procesarVenta(ventaRequest);

            if (ventaResponse.isVentaExitosa()) {
                request.setAttribute("venta", ventaResponse);
                request.setAttribute("cedula", cedula);
                request.setAttribute("nombre", nombre);
                request.getRequestDispatcher("/WEB-INF/views/venta-confirmacion.jsp").forward(request, response);
            } else {
                String mensaje = ventaResponse.getMensaje() != null ? ventaResponse.getMensaje() : "Venta rechazada";
                request.setAttribute("error", mensaje);
                recargarCatalogo(request);
                request.getRequestDispatcher("/WEB-INF/views/venta.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error al procesar venta: " + e.getMessage());
            recargarCatalogo(request);
            request.getRequestDispatcher("/WEB-INF/views/venta.jsp").forward(request, response);
        }
    }

    private void recargarCatalogo(HttpServletRequest request) {
        try {
            request.setAttribute("electrodomesticos", electrodomesticoService.listarElectrodomesticos());
        } catch (Exception ignored) {
        }
    }
}
