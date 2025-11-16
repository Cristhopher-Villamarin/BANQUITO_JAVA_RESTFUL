<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ec.edu.monster.model.VentaResponse"%>
<%@page import="ec.edu.monster.model.DetalleVentaResponse"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Venta confirmada</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/venta-confirmacion.css">
</head>
<body class="confirm-body">
<%
    VentaResponse venta = (VentaResponse) request.getAttribute("venta");
    String cedula = (String) request.getAttribute("cedula");
    String nombre = (String) request.getAttribute("nombre");
%>
<div class="confirm-shell">
    <header class="hero">
        <div>
            <p class="hero__eyebrow">Resultado del flujo</p>
            <h1>Venta procesada</h1>
            <p>Factura #<%= venta != null ? venta.getIdFactura() : "" %> · Estado: <%= venta != null ? venta.getEstadoFactura() : "--" %></p>
        </div>
        <div class="hero__actions">
            <a href="${pageContext.request.contextPath}/user/menu" class="ghost-btn">Volver al menú</a>
        </div>
    </header>

    <% if (venta != null && venta.isVentaExitosa()) { %>
        <section class="panel panel--grid">
            <article class="card">
                <h2>Datos del cliente</h2>
                <ul>
                    <li><span>Cliente</span><strong><%= nombre %></strong></li>
                    <li><span>Cédula</span><strong><%= cedula %></strong></li>
                    <li><span>Forma de pago</span><strong><%= venta.getFormaPago() %></strong></li>
                </ul>
            </article>
            <article class="card">
                <h2>Resumen de la factura</h2>
                <div class="totals">
                    <div><span>Subtotal</span><strong>$<%= venta.getSubtotal() %></strong></div>
                    <div><span>Descuento</span><strong>$<%= venta.getDescuento() %></strong></div>
                    <div class="totals__highlight"><span>Total</span><strong>$<%= venta.getTotal() %></strong></div>
                </div>
            </article>
        </section>

        <section class="panel">
            <header class="panel__header">
                <div>
                    <p class="panel__eyebrow">Productos</p>
                    <h2>Detalle de la venta</h2>
                </div>
                <a href="${pageContext.request.contextPath}/user/venta" class="ghost-btn">Registrar otra</a>
            </header>
            <div class="table-wrapper">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Producto</th>
                        <th>Cantidad</th>
                        <th>Precio unitario</th>
                        <th>Subtotal</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% List<DetalleVentaResponse> detalles = venta.getDetalles(); %>
                    <% if (detalles != null && !detalles.isEmpty()) { %>
                        <% for (DetalleVentaResponse d : detalles) { %>
                            <tr>
                                <td><%= d.getNombreElectrodomestico() %></td>
                                <td><%= d.getCantidad() %></td>
                                <td>$<%= d.getPrecioUnitario() %></td>
                                <td>$<%= d.getSubtotalLinea() %></td>
                            </tr>
                        <% } %>
                    <% } else { %>
                        <tr>
                            <td colspan="4" class="table__empty">Sin detalles.</td>
                        </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </section>
    <% } else { %>
        <section class="panel panel--error">
            <h2>Venta rechazada</h2>
            <p><%= venta != null && venta.getMensaje() != null ? venta.getMensaje() : "No se pudo procesar la venta." %></p>
            <div class="panel__actions">
                <a href="${pageContext.request.contextPath}/user/venta" class="primary-btn">Intentar nuevamente</a>
                <a href="${pageContext.request.contextPath}/user/menu" class="ghost-btn">Volver al menú</a>
            </div>
        </section>
    <% } %>

    <footer class="actions">
        <a href="${pageContext.request.contextPath}/user/venta" class="ghost-btn">Registrar otra venta</a>
        <a href="${pageContext.request.contextPath}/user/menu" class="primary-btn">Ir al menú</a>
    </footer>
</div>
</body>
</html>
