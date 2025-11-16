<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="ec.edu.monster.model.Electrodomestico"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Electrodomésticos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-electrodomesticos.css">
</head>
<body class="admin-body">
<%
    List<Electrodomestico> lista = (List<Electrodomestico>) request.getAttribute("electrodomesticos");
    String error = (String) request.getAttribute("error");
    int total = lista != null ? lista.size() : 0;
%>
<div class="admin-shell">
    <header class="hero">
        <div class="hero__content">
            <p class="hero__eyebrow">Panel administrativo</p>
            <h1>Gestión de electrodomésticos</h1>
            <p>Administra el catálogo corporativo, asegura precios vigentes y ordena el inventario disponible.</p>
            <div class="hero__stats">
                <article>
                    <h2><%= total %></h2>
                    <p>Registros activos</p>
                </article>
              
            </div>
        </div>
        <div class="hero__actions">
            <a href="${pageContext.request.contextPath}/logout" class="ghost-btn">Cerrar sesión</a>
            <a href="${pageContext.request.contextPath}/admin/electrodomesticos?action=nuevo" class="primary-btn">Nuevo electrodoméstico</a>
        </div>
    </header>

    <% if (error != null) { %>
        <div class="alert"><%= error %></div>
    <% } %>

    <section class="panel">
        <div class="panel__header">
            <div>
                <p class="panel__eyebrow">Catálogo maestro</p>
                <h2>Listado y acciones</h2>
            </div>
            <p class="panel__hint">Selecciona editar para ajustar precios o eliminar para depurar el inventario.</p>
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
                                <a href="${pageContext.request.contextPath}/admin/electrodomesticos?action=editar&id=<%= e.getIdElectrodomestico() %>" class="link">Editar</a>
                                <form method="post" action="${pageContext.request.contextPath}/admin/electrodomesticos" class="inline-form" onsubmit="return confirm('¿Eliminar este electrodoméstico?');">
                                    <input type="hidden" name="action" value="eliminar">
                                    <input type="hidden" name="id" value="<%= e.getIdElectrodomestico() %>">
                                    <button type="submit" class="danger-btn">Eliminar</button>
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
    </section>
</div>
</body>
</html>
