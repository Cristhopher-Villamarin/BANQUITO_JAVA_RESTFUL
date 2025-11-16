package ec.edu.monster.controller;

import ec.edu.monster.model.LoginResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "UserMenuServlet", urlPatterns = {"/user/menu"})
public class UserMenuServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        LoginResponse usuario = (LoginResponse) session.getAttribute("usuario");
        if (usuario == null || !usuario.isAutenticado() || !"USER".equalsIgnoreCase(usuario.getRol())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("usuario", usuario);
        request.getRequestDispatcher("/WEB-INF/views/user-menu.jsp").forward(request, response);
    }
}
