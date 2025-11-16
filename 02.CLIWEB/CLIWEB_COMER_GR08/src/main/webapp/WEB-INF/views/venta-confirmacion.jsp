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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%
    VentaResponse venta = (VentaResponse) request.getAttribute("venta");
    String cedula = (String) request.getAttribute("cedula");
    String nombre = (String) request.getAttribute("nombre");
%>
<div class="page">
    <div class="card">
        <div class="card__header">
            <div>
                <h1 class="title">Venta procesada</h1>
                <p class="subtitle">Factura #<%= venta != null ? venta.getIdFactura() : "" %></p>
            </div>
            <div class="user-badge">
                <a href="${pageContext.request.contextPath}/user/menu" class="link link--muted">Volver al menú</a>
            </div>
        </div>

        <% if (venta != null && venta.isVentaExitosa()) { %>
            <section class="section">
                <h2 class="section__title">Datos del cliente</h2>
                <p><strong>Cliente:</strong> <%= nombre %> (<%= cedula %>)</p>
                <p><strong>Forma de pago:</strong> <%= venta.getFormaPago() %></p>
                <p><strong>Estado:</strong> <%= venta.getEstadoFactura() %></p>
            </section>

            <section class="section">
                <h2 class="section__title">Detalle de productos</h2>
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

            <section class="section section--totals">
                <div class="totals">
                    <div class="totals__row">
                        <span>Subtotal</span>
                        <span>$<%= venta.getSubtotal() %></span>
                    </div>
                    <div class="totals__row">
                        <span>Descuento</span>
                        <span>$<%= venta.getDescuento() %></span>
                    </div>
                    <div class="totals__row totals__row--highlight">
                        <span>Total</span>
                        <span>$<%= venta.getTotal() %></span>
                    </div>
                </div>
            </section>
        <% } else { %>
            <div class="alert alert--error">
                <strong>Venta rechazada.</strong>
                <p><%= venta != null && venta.getMensaje() != null ? venta.getMensaje() : "No se pudo procesar la venta." %></p>
            </div>
        <% } %>

        <div class="form__actions form__actions--full">
            <a href="${pageContext.request.contextPath}/user/venta" class="btn btn--ghost">Registrar otra venta</a>
            <a href="${pageContext.request.contextPath}/user/menu" class="btn btn--primary">Volver al menú</a>
        </div>
    </div>
</div>
</body>
</html>
