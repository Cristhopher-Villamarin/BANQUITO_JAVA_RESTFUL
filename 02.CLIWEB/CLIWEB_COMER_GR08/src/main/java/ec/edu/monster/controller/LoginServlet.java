package ec.edu.monster.controller;

import ec.edu.monster.model.LoginResponse;
import ec.edu.monster.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            LoginResponse loginResponse = authService.login(username, password);

            if (loginResponse != null && loginResponse.isAutenticado()) {
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario", loginResponse);

                String rol = loginResponse.getRol();
                if ("ADMIN".equalsIgnoreCase(rol)) {
                    response.sendRedirect(request.getContextPath() + "/admin/electrodomesticos");
                } else if ("USER".equalsIgnoreCase(rol)) {
                    response.sendRedirect(request.getContextPath() + "/user/menu");
                } else {
                    request.setAttribute("error", "Rol no reconocido: " + rol);
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                }
            } else {
                String mensaje = (loginResponse != null) ? loginResponse.getMensaje() : "Credenciales inválidas";
                request.setAttribute("error", mensaje);
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error de conexión: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
