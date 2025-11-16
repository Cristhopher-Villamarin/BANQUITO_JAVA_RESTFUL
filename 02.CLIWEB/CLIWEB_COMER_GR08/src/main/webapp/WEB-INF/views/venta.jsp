<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="ec.edu.monster.model.Electrodomestico"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrar venta</title>
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
                <h1 class="title">Registrar nueva venta</h1>
                <p class="subtitle">Cliente - Comercializadora Monster</p>
            </div>
            <div class="user-badge">
                <a href="${pageContext.request.contextPath}/user/menu" class="link link--muted">Volver al menú</a>
            </div>
        </div>

        <% if (error != null) { %>
            <div class="alert alert--error"><%= error %></div>
        <% } %>

        <form method="post" action="${pageContext.request.contextPath}/user/venta" class="form form--two-cols">
            <section class="section">
                <h2 class="section__title">Datos del cliente</h2>
                <div class="form__group">
                    <label class="form__label" for="cedula">Cédula</label>
                    <input id="cedula" name="cedula" type="text" class="form__input" required>
                </div>
                <div class="form__group">
                    <label class="form__label" for="nombre">Nombre completo</label>
                    <input id="nombre" name="nombre" type="text" class="form__input" required>
                </div>
                <div class="form__group">
                    <label class="form__label">Forma de pago</label>
                    <div class="radio-group">
                        <label class="radio">
                            <input type="radio" name="formaPago" value="EFECTIVO" checked>
                            <span>EFECTIVO (33% descuento)</span>
                        </label>
                        <label class="radio">
                            <input type="radio" name="formaPago" value="CREDITO_DIRECTO">
                            <span>CRÉDITO DIRECTO (validación con Banquito)</span>
                        </label>
                    </div>
                </div>
                <div class="form__group">
                    <label class="form__label" for="plazoMeses">Plazo (meses, solo crédito)</label>
                    <input id="plazoMeses" name="plazoMeses" type="number" min="3" max="24" class="form__input" placeholder="Ej: 12">
                </div>
            </section>

            <section class="section section--full">
                <h2 class="section__title">Productos</h2>
                <p class="section__hint">Indique la cantidad para los productos que desea vender.</p>
                <div class="table-wrapper">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Precio</th>
                            <th>Cantidad</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% if (lista != null && !lista.isEmpty()) { %>
                            <% for (Electrodomestico e : lista) { %>
                                <tr>
                                    <td>
                                        <%= e.getIdElectrodomestico() %>
                                        <input type="hidden" name="productoId" value="<%= e.getIdElectrodomestico() %>">
                                    </td>
                                    <td><%= e.getNombre() %></td>
                                    <td>$<%= e.getPrecioVenta() %></td>
                                    <td>
                                        <input name="cantidad" type="number" min="0" class="form__input form__input--sm" value="0">
                                    </td>
                                </tr>
                            <% } %>
                        <% } else { %>
                            <tr>
                                <td colspan="4" class="table__empty">No hay productos disponibles.</td>
                            </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
            </section>

            <div class="form__actions form__actions--full">
                <a href="${pageContext.request.contextPath}/user/menu" class="btn btn--ghost">Cancelar</a>
                <button type="submit" class="btn btn--primary">Procesar venta</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
