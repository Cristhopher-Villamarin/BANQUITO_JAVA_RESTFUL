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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%
    List<Credito> creditos = (List<Credito>) request.getAttribute("creditos");
    TablaAmortizacion tabla = (TablaAmortizacion) request.getAttribute("tabla");
    String cedula = (String) request.getAttribute("cedula");
    String error = (String) request.getAttribute("error");
%>
<div class="page">
    <div class="card">
        <div class="card__header">
            <div>
                <h1 class="title">Tabla de amortización</h1>
                <p class="subtitle">Créditos activos del cliente</p>
            </div>
            <div class="user-badge">
                <a href="${pageContext.request.contextPath}/user/menu" class="link link--muted">Volver al menú</a>
            </div>
        </div>

        <% if (error != null) { %>
            <div class="alert alert--error"><%= error %></div>
        <% } %>

        <form method="get" action="${pageContext.request.contextPath}/user/credito/amortizacion" class="form">
            <div class="form__group">
                <label class="form__label" for="cedula">Cédula del cliente</label>
                <input id="cedula" name="cedula" type="text" class="form__input" required value="<%= cedula != null ? cedula : "" %>">
            </div>
            <div class="form__actions">
                <button type="submit" class="btn btn--primary">Buscar créditos activos</button>
            </div>
        </form>

        <% if (creditos != null && !creditos.isEmpty()) { %>
            <section class="section">
                <h2 class="section__title">Créditos activos</h2>
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
                                        <button type="submit" class="btn btn--ghost">Ver tabla</button>
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
            <section class="section">
                <h2 class="section__title">Detalle del crédito</h2>
                <p><strong>ID Factura:</strong> <%= tabla.getIdFactura() %></p>
                <p><strong>Cliente:</strong> <%= tabla.getNombreCliente() %> (<%= tabla.getCedulaCliente() %>)</p>
                <p><strong>Monto financiado:</strong> $<%= tabla.getMontoTotal() %></p>
                <p><strong>Cuota mensual:</strong> $<%= tabla.getCuotaMensual() %></p>
            </section>

            <section class="section">
                <h2 class="section__title">Tabla de amortización</h2>
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
</div>
</body>
</html>
