<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ec.edu.monster.model.CreditoEvaluacionResponse"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Estado de crédito</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/credito-estado.css">
</head>
<body class="credito-body">
<%
    CreditoEvaluacionResponse evaluacion = (CreditoEvaluacionResponse) request.getAttribute("evaluacion");
    String cedula = (String) request.getAttribute("cedula");
    String error = (String) request.getAttribute("error");
%>
<div class="credito-shell">
    <header class="hero">
        <div class="hero__content">
            <p class="hero__eyebrow">Conexión Banquito</p>
            <h1>Estado de crédito del cliente</h1>
            <p>Consulta si el cliente puede acceder a crédito directo y visualiza el detalle en tarjetas.</p>
        </div>
        <div class="hero__actions">
            <a href="${pageContext.request.contextPath}/user/menu" class="ghost-btn">Volver al menú</a>
        </div>
    </header>

    <% if (error != null) { %>
        <div class="alert"><%= error %></div>
    <% } %>

    <section class="panel">
        <form method="post" action="${pageContext.request.contextPath}/user/credito/estado" class="consulta-form">
            <label class="field">
                <span>Cédula del cliente</span>
                <input id="cedula" name="cedula" type="text" required value="<%= cedula != null ? cedula : "" %>">
            </label>
            <button type="submit" class="primary-btn">Consultar</button>
        </form>
    </section>

    <% if (evaluacion != null) {
           boolean sujeto = evaluacion.getSujetoCredito() != null && evaluacion.getSujetoCredito();
    %>
        <section class="results">
            <article class="result-card">
                <p class="result-card__label">Resultado</p>
                <h2><%= sujeto ? "Sujeto de crédito" : "No sujeto" %></h2>
                <p class="result-card__value">Cédula: <strong><%= cedula %></strong></p>
            </article>
            <% if (sujeto) { %>
            <article class="result-card">
                <p class="result-card__label">Monto máximo</p>
                <h2><%= evaluacion.getMontoMaximoCredito() != null ? "$" + evaluacion.getMontoMaximoCredito() : "--" %></h2>
            </article>
            <% } %>
        </section>

        <% if (evaluacion.getMensaje() != null) { %>
            <section class="panel panel--message">
                <p class="panel__eyebrow">Mensaje</p>
                <p><%= evaluacion.getMensaje() %></p>
            </section>
        <% } %>
    <% } %>
</div>
</body>
</html>
