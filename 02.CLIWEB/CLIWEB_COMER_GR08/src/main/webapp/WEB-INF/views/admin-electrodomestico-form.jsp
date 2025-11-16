<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ec.edu.monster.model.Electrodomestico"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Electrodoméstico - Formulario</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%
    Electrodomestico e = (Electrodomestico) request.getAttribute("electrodomestico");
    boolean esEdicion = e != null && e.getIdElectrodomestico() != null;
%>
<div class="page">
    <div class="card card--narrow">
        <h1 class="title"><%= esEdicion ? "Editar electrodoméstico" : "Nuevo electrodoméstico" %></h1>

        <form method="post" action="${pageContext.request.contextPath}/admin/electrodomesticos" class="form">
            <input type="hidden" name="action" value="<%= esEdicion ? "actualizar" : "crear" %>">
            <% if (esEdicion) { %>
                <input type="hidden" name="id" value="<%= e.getIdElectrodomestico() %>">
            <% } %>

            <div class="form__group">
                <label class="form__label" for="nombre">Nombre</label>
                <input id="nombre" name="nombre" type="text" class="form__input" required value="<%= esEdicion ? e.getNombre() : "" %>">
            </div>

            <div class="form__group">
                <label class="form__label" for="precio">Precio de venta</label>
                <input id="precio" name="precio" type="number" step="0.01" min="0" class="form__input" required value="<%= esEdicion && e.getPrecioVenta() != null ? e.getPrecioVenta() : "" %>">
            </div>

            <div class="form__actions">
                <a href="${pageContext.request.contextPath}/admin/electrodomesticos" class="btn btn--ghost">Cancelar</a>
                <button type="submit" class="btn btn--primary">Guardar</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
