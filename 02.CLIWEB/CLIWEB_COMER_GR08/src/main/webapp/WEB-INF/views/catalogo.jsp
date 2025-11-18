<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="ec.edu.monster.model.Electrodomestico"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catálogo de electrodomésticos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/catalogo.css">
</head>
<body class="catalog-body">
<%
    List<Electrodomestico> lista = (List<Electrodomestico>) request.getAttribute("electrodomesticos");
    String error = (String) request.getAttribute("error");
    int totalProductos = lista != null ? lista.size() : 0;
%>
<div class="catalog-shell">
    <header class="hero">
        <div class="hero__content">
            <p class="hero__badge">Comercializadora Monster</p>
            <h1 class="hero__title">Catálogo activo de electrodomésticos</h1>
            <p class="hero__subtitle">Actualiza tu cotización con precios reales y disponibilidad inmediata.</p>
            <div class="hero__stats">
                <article>
                    <h2><%= totalProductos %></h2>
                    <p>Productos vigentes</p>
                </article>
                <article>
                    <h2>USD</h2>
                    <p>Precios expresados en dólares americanos</p>
                </article>
            </div>
        </div>
        <div class="hero__actions">
            <a href="${pageContext.request.contextPath}/user/menu" class="ghost-btn">Regresar al menú</a>
            <p class="hero__note">Datos sincronizados con inventario central.</p>
        </div>
    </header>

    <section class="panel">
        <div class="panel__header">
            <div>
                <p class="panel__eyebrow">Tabla maestra</p>
                <h2>Electrodomésticos disponibles</h2>
            </div>
            <div class="panel__tips">
                <span>Filtra por ID al momento de registrar una venta</span>
            </div>
        </div>

        <% if (error != null) { %>
            <div class="panel__alert"><%= error %></div>
        <% } %>

        <div class="table-wrapper">
            <table class="table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Precio</th>
                    <th>Foto</th>
                </tr>
                </thead>
                <tbody>
                <% if (lista != null && !lista.isEmpty()) { %>
                    <% for (Electrodomestico e : lista) { %>
                        <tr>
                            <td><%= e.getIdElectrodomestico() %></td>
                            <td><%= e.getNombre() %></td>
                            <td>$<%= e.getPrecioVenta() %></td>
                            <td>
                                <% if (e.getFotoUrl() != null && !e.getFotoUrl().isBlank()) { %>
                                    <img src="<%= e.getFotoUrl() %>" alt="Foto" class="table__thumb">
                                <% } %>
                            </td>
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
    </section>
</div>
</body>
</html>
