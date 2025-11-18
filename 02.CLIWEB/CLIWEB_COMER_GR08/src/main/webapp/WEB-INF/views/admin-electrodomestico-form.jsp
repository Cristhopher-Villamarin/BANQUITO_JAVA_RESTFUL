<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ec.edu.monster.model.Electrodomestico"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Electrodoméstico - Formulario</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-electrodomestico-form.css">
</head>
<body class="form-body">
<%
    Electrodomestico e = (Electrodomestico) request.getAttribute("electrodomestico");
    boolean esEdicion = e != null && e.getIdElectrodomestico() != null;
%>
<div class="form-shell">
    <header class="hero">
        <div>
            <p class="hero__eyebrow">Catálogo maestro</p>
            <h1><%= esEdicion ? "Editar electrodoméstico" : "Nuevo electrodoméstico" %></h1>
            <p>Mantén actualizada la lista de referencias con precios oficiales.</p>
        </div>
        <div class="hero__actions">
            <a href="${pageContext.request.contextPath}/admin/electrodomesticos" class="ghost-btn">Volver al listado</a>
        </div>
    </header>

    <section class="panel">
        <form method="post" action="${pageContext.request.contextPath}/admin/electrodomesticos" class="edit-form" enctype="multipart/form-data">
            <input type="hidden" name="action" value="<%= esEdicion ? "actualizar" : "crear" %>">
            <% if (esEdicion) { %>
                <input type="hidden" name="id" value="<%= e.getIdElectrodomestico() %>">
            <% } %>

            <label class="field">
                <span>Nombre</span>
                <input id="nombre" name="nombre" type="text" required value="<%= esEdicion ? e.getNombre() : "" %>">
            </label>

            <label class="field">
                <span>Precio de venta (USD)</span>
                <input id="precio" name="precio" type="number" step="0.01" min="0" required value="<%= esEdicion && e.getPrecioVenta() != null ? e.getPrecioVenta() : "" %>">
            </label>

            <label class="field">
                <span>Foto del producto</span>
                <input id="foto" name="foto" type="file" accept="image/*">
            </label>

            <% if (esEdicion && e.getFotoUrl() != null && !e.getFotoUrl().isBlank()) { %>
                <div class="field">
                    <span>Foto actual</span>
                    <img src="<%= e.getFotoUrl() %>" alt="Foto actual" style="max-width: 200px; border-radius: 8px; border: 1px solid #e5e7eb;">
                </div>
            <% } %>

            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/admin/electrodomesticos" class="ghost-btn">Cancelar</a>
                <button type="submit" class="primary-btn">Guardar</button>
            </div>
        </form>
    </section>
</div>
</body>
</html>
