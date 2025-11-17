<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="ec.edu.monster.model.Credito"%>
<%@page import="ec.edu.monster.model.TablaAmortizacion"%>
<%@page import="ec.edu.monster.model.CuotaAmortizacion"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tabla de amortización</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/credito-amortizacion.css">
</head>
<body class="amort-body">
<%
    List<Credito> creditos = (List<Credito>) request.getAttribute("creditos");
    TablaAmortizacion tabla = (TablaAmortizacion) request.getAttribute("tabla");
    String cedula = (String) request.getAttribute("cedula");
    String error = (String) request.getAttribute("error");
%>
<div class="amort-shell">
    <header class="hero">
        <div class="hero__content">
            <p class="hero__eyebrow">Ciclo de crédito</p>
            <h1>Tabla de amortización</h1>
            <p>Consulta los créditos activos del cliente y revisa la tabla de cuotas aprobada por Banquito.</p>
        </div>
        <div class="hero__actions">
            <a href="${pageContext.request.contextPath}/user/menu" class="ghost-btn">Volver al menú</a>
        </div>
    </header>

    <% if (error != null) { %>
        <div class="alert"><%= error %></div>
    <% } %>

    <section class="panel">
        <form method="get" action="${pageContext.request.contextPath}/user/credito/amortizacion" class="search-form">
            <label class="field">
                <span>Cédula del cliente</span>
                <input id="cedula" name="cedula" type="text" required value="<%= cedula != null ? cedula : "" %>">
            </label>
            <button type="submit" class="primary-btn">Buscar créditos activos</button>
        </form>
    </section>

    <% if (creditos != null && !creditos.isEmpty()) { %>
        <section class="panel panel--table">
            <header class="panel__header">
                <div>
                    <p class="panel__eyebrow">Créditos activos</p>
                    <h2>Selecciona un crédito</h2>
                </div>
            </header>
            <div class="table-wrapper">
                <table class="table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Monto aprobado</th>
                        <th>Plazo</th>
                        <th>Cuota</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (Credito c : creditos) { %>
                        <tr>
                            <td><%= c.getIdCredito() %></td>
                            <td>$<%= c.getMontoAprobado() %></td>
                            <td><%= c.getPlazoMeses() %> meses</td>
                            <td>$<%= c.getCuotaFija() %></td>
                            <td class="table__actions">
                                <form method="get" action="${pageContext.request.contextPath}/user/credito/amortizacion" class="inline-form">
                                    <input type="hidden" name="idCredito" value="<%= c.getIdCredito() %>">
                                    <button type="submit" class="ghost-btn ghost-btn--inline">Ver tabla</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </section>
    <% } %>

    <% if (tabla != null && tabla.getCuotas() != null && !tabla.getCuotas().isEmpty()) { %>
        <section class="panel panel--summary">
            <header>
                <p class="panel__eyebrow">Detalle del crédito</p>
                <h2>ID Factura <%= tabla.getIdFactura() %></h2>
            </header>
            <div class="summary-grid">
                <article>
                    <p class="label">Cliente</p>
                    <strong><%= tabla.getNombreCliente() %></strong>
                    <span class="muted"><%= tabla.getCedulaCliente() %></span>
                </article>
                <article>
                    <p class="label">Monto financiado</p>
                    <strong>$<%= tabla.getMontoTotal() %></strong>
                </article>
                <article>
                    <p class="label">Cuota mensual</p>
                    <strong>$<%= tabla.getCuotaMensual() %></strong>
                </article>
            </div>
        </section>

        <section class="panel panel--table">
            <header class="panel__header">
                <div>
                    <p class="panel__eyebrow">Tabla de amortización</p>
                    <h2>Detalle de cuotas</h2>
                </div>
            </header>
            <div class="table-wrapper">
                <table class="table table--compact">
                    <thead>
                    <tr>
                        <th>Cuota</th>
                        <th>Capital</th>
                        <th>Interés</th>
                        <th>Valor cuota</th>
                        <th>Saldo</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (CuotaAmortizacion cuota : tabla.getCuotas()) { %>
                        <tr>
                            <td><%= cuota.getNumeroCuota() %></td>
                            <td>$<%= cuota.getCapital() %></td>
                            <td>$<%= cuota.getInteres() %></td>
                            <td>$<%= cuota.getCuota() %></td>
                            <td>$<%= cuota.getSaldoFinal() %></td>
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
