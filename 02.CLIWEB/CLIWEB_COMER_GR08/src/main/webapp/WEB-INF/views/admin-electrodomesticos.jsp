<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="ec.edu.monster.model.Electrodomestico"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Electrodomésticos</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-electrodomesticos.css"><link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-electrodomesticos.css">
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

        <div class="cards-wrapper">
            <% if (lista != null && !lista.isEmpty()) { %>
                <div class="cards-grid">
                    <% for (Electrodomestico e : lista) { %>
                        <article class="product-card">
                            <div class="product-card__media">
                                <% if (e.getFotoUrl() != null && !e.getFotoUrl().isBlank()) { %>
                                    <img src="<%= e.getFotoUrl() %>" alt="Foto de <%= e.getNombre() %>" class="product-card__image">
                                <% } else { %>
                                    <div class="product-card__placeholder">Sin imagen</div>
                                <% } %>
                            </div>
                            <div class="product-card__body">
                                <header class="product-card__header">
                                    <span class="product-card__id">ID #<%= e.getIdElectrodomestico() %></span>
                                    <h3 class="product-card__title"><%= e.getNombre() %></h3>
                                </header>
                                <p class="product-card__price">$<%= e.getPrecioVenta() %></p>
                                <div class="product-card__actions">
                                    <a href="${pageContext.request.contextPath}/admin/electrodomesticos?action=editar&id=<%= e.getIdElectrodomestico() %>" class="product-card__link">Editar</a>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/electrodomesticos" class="inline-form" onsubmit="return confirm('¿Eliminar este electrodoméstico?');">
                                        <input type="hidden" name="action" value="eliminar">
                                        <input type="hidden" name="id" value="<%= e.getIdElectrodomestico() %>">
                                        <button type="submit" class="product-card__danger">Eliminar</button>
                                    </form>
                                </div>
                            </div>
                        </article>
                    <% } %>
                </div>
            <% } else { %>
                <div class="table__empty">No hay electrodomésticos registrados.</div>
            <% } %>
        </div>
    </section>
</div>
</body>
</html>
