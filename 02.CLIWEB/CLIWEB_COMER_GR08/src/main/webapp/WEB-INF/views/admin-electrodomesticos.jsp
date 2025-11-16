<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="ec.edu.monster.model.Electrodomestico"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Electrodomésticos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%
    List<Electrodomestico> lista = (List<Electrodomestico>) request.getAttribute("electrodomesticos");
    String error = (String) request.getAttribute("error");
%>
<div class="page">
    <div class="card">
        <div class="card__header">
            <div>
                <h1 class="title">Gestión de electrodomésticos</h1>
                <p class="subtitle">Panel de administración</p>
            </div>
            <div class="user-badge">
                <a href="${pageContext.request.contextPath}/logout" class="link link--muted">Cerrar sesión</a>
            </div>
        </div>

        <% if (error != null) { %>
            <div class="alert alert--error"><%= error %></div>
        <% } %>

        <div class="toolbar">
            <a href="${pageContext.request.contextPath}/admin/electrodomesticos?action=nuevo" class="btn btn--primary">Nuevo electrodoméstico</a>
        </div>

        <div class="table-wrapper">
            <table class="table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Precio</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <% if (lista != null && !lista.isEmpty()) { %>
                    <% for (Electrodomestico e : lista) { %>
                        <tr>
                            <td><%= e.getIdElectrodomestico() %></td>
                            <td><%= e.getNombre() %></td>
                            <td>$<%= e.getPrecioVenta() %></td>
                            <td class="table__actions">
                                <a href="${pageContext.request.contextPath}/admin/electrodomesticos?action=editar&id=<%= e.getIdElectrodomestico() %>" class="btn btn--ghost">Editar</a>
                                <form method="post" action="${pageContext.request.contextPath}/admin/electrodomesticos" class="inline-form" onsubmit="return confirm('¿Eliminar este electrodoméstico?');">
                                    <input type="hidden" name="action" value="eliminar">
                                    <input type="hidden" name="id" value="<%= e.getIdElectrodomestico() %>">
                                    <button type="submit" class="btn btn--danger">Eliminar</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                <% } else { %>
                    <tr>
                        <td colspan="4" class="table__empty">No hay electrodomésticos registrados.</td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
