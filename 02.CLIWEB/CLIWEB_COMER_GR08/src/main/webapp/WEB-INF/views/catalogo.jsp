<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="ec.edu.monster.model.Electrodomestico"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catálogo de electrodomésticos</title>
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
                <h1 class="title">Catálogo de electrodomésticos</h1>
                <p class="subtitle">Comercializadora Monster</p>
            </div>
            <div class="user-badge">
                <a href="${pageContext.request.contextPath}/user/menu" class="link link--muted">Volver al menú</a>
            </div>
        </div>

        <% if (error != null) { %>
            <div class="alert alert--error"><%= error %></div>
        <% } %>

        <div class="table-wrapper">
            <table class="table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Precio</th>
                </tr>
                </thead>
                <tbody>
                <% if (lista != null && !lista.isEmpty()) { %>
                    <% for (Electrodomestico e : lista) { %>
                        <tr>
                            <td><%= e.getIdElectrodomestico() %></td>
                            <td><%= e.getNombre() %></td>
                            <td>$<%= e.getPrecioVenta() %></td>
                        </tr>
                    <% } %>
                <% } else { %>
                    <tr>
                        <td colspan="3" class="table__empty">No hay productos disponibles.</td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
