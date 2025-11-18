<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="ec.edu.monster.model.Factura"%>
<%@page import="ec.edu.monster.model.DetalleFactura"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Facturas por cliente</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/facturas-cliente.css">
</head>
<body class="facturas-body">
<%
    String cedula = (String) request.getAttribute("cedula");
    String error = (String) request.getAttribute("error");
    List<Factura> facturas = (List<Factura>) request.getAttribute("facturas");
    Factura facturaSeleccionada = (Factura) request.getAttribute("facturaSeleccionada");
%>
<div class="facturas-shell">
    <header class="hero">
        <div class="hero__content">
            <p class="hero__eyebrow">Consultas comerciales</p>
            <h1>Facturas por cliente</h1>
            <p>Ingresa la cédula del cliente para ver todas sus facturas y revisa el detalle de cada una.</p>
        </div>
        <div class="hero__actions">
            <a href="${pageContext.request.contextPath}/user/menu" class="ghost-btn">Volver al menú</a>
        </div>
    </header>

    <% if (error != null) { %>
        <div class="alert"><%= error %></div>
    <% } %>

    <section class="panel">
        <form method="get" action="${pageContext.request.contextPath}/user/facturas/cliente" class="search-form">
            <label class="field">
                <span>Cédula del cliente</span>
                <input id="cedula" name="cedula" type="text" required value="<%= cedula != null ? cedula : "" %>">
            </label>
            <button type="submit" class="primary-btn">Buscar facturas</button>
        </form>
    </section>

    <% if (facturas != null && !facturas.isEmpty()) { %>
        <section class="panel panel--table">
            <header class="panel__header">
                <div>
                    <p class="panel__eyebrow">Listado</p>
                    <h2>Facturas encontradas</h2>
                </div>
            </header>
            <div class="table-wrapper">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Fecha</th>
                        <th>Forma de pago</th>
                        <th>Subtotal</th>
                        <th>Descuento</th>
                        <th>Total</th>
                        <th>Estado</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (Factura f : facturas) { %>
                        <tr>
                            <td><%= f.getFecha() %></td>
                            <td><%= f.getFormaPago() %></td>
                            <td>$<%= f.getSubtotal() %></td>
                            <td>$<%= f.getDescuento() %></td>
                            <td>$<%= f.getTotal() %></td>
                            <td><%= f.getEstado() %></td>
                            <td class="table__actions">
                                <form method="get" action="${pageContext.request.contextPath}/user/facturas/cliente" class="inline-form">
                                    <input type="hidden" name="cedula" value="<%= cedula %>">
                                    <input type="hidden" name="idFactura" value="<%= f.getId() %>">
                                    <button type="submit" class="ghost-btn ghost-btn--inline">Ver detalle</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </section>
    <% } else if (cedula != null && !cedula.isBlank()) { %>
        <section class="panel panel--empty">
            <p>No se encontraron facturas para la cédula <strong><%= cedula %></strong>.</p>
        </section>
    <% } %>

    <% if (facturaSeleccionada != null) { %>
        <section class="panel panel--summary">
            <header>
                <p class="panel__eyebrow">Detalle de factura</p>
                <h2>Factura #<%= facturaSeleccionada.getId() %></h2>
            </header>
            <div class="summary-grid">
                <article>
                    <p class="label">Cliente</p>
                    <strong><%= facturaSeleccionada.getNombreCliente() != null ? facturaSeleccionada.getNombreCliente() : "" %></strong>
                    <span class="muted"><%= facturaSeleccionada.getCedulaCliente() != null ? facturaSeleccionada.getCedulaCliente() : (cedula != null ? cedula : "") %></span>
                </article>
                <article>
                    <p class="label">Fecha</p>
                    <strong><%= facturaSeleccionada.getFecha() %></strong>
                </article>
                <article>
                    <p class="label">Forma de pago</p>
                    <strong><%= facturaSeleccionada.getFormaPago() %></strong>
                </article>
                <article>
                    <p class="label">Subtotal</p>
                    <strong>$<%= facturaSeleccionada.getSubtotal() %></strong>
                </article>
                <article>
                    <p class="label">Descuento</p>
                    <strong>
                        <%
                            if ("CREDITO_DIRECTO".equalsIgnoreCase(facturaSeleccionada.getFormaPago()) && facturaSeleccionada.getDescuento() == null) {
                        %>
                            No aplica
                        <%
                            } else {
                        %>
                            $<%= facturaSeleccionada.getDescuento() %>
                        <%
                            }
                        %>
                    </strong>
                </article>
                <article>
                    <p class="label">Total</p>
                    <strong>$<%= facturaSeleccionada.getTotal() %></strong>
                </article>
            </div>
        </section>

        <section class="panel panel--table">
            <header class="panel__header">
                <div>
                    <p class="panel__eyebrow">Detalle de productos</p>
                    <h2>Electrodomésticos</h2>
                </div>
            </header>
            <div class="table-wrapper">
                <table class="table table--compact">
                    <thead>
                    <tr>
                        <th>Producto</th>
                        <th>Cantidad</th>
                        <th>Precio unitario</th>
                        <th>Subtotal</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% 
                       List<DetalleFactura> detalles = facturaSeleccionada.getDetalles();
                       if (detalles != null && !detalles.isEmpty()) {
                           for (DetalleFactura d : detalles) { %>
                        <tr>
                            <td><%= d.getNombreElectrodomestico() != null ? d.getNombreElectrodomestico() : (d.getIdElectrodomestico() != null ? d.getIdElectrodomestico() : "") %></td>
                            <td><%= d.getCantidad() %></td>
                            <td>$<%= d.getPrecioUnitario() %></td>
                            <td>$<%= d.getSubtotal() %></td>
                        </tr>
                    <%   }
                       } else { %>
                        <tr>
                            <td colspan="4" class="table__empty">Sin detalles registrados para esta factura.</td>
                        </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </section>
    <% } %>
</div>
</body>
</html>
