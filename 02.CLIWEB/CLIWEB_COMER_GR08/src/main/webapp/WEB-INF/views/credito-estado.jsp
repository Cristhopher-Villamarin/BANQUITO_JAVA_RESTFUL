<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ec.edu.monster.model.CreditoEvaluacionResponse"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Estado de crédito</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%
    CreditoEvaluacionResponse evaluacion = (CreditoEvaluacionResponse) request.getAttribute("evaluacion");
    String cedula = (String) request.getAttribute("cedula");
    String error = (String) request.getAttribute("error");
%>
<div class="page">
    <div class="card">
        <div class="card__header">
            <div>
                <h1 class="title">Estado de crédito del cliente</h1>
                <p class="subtitle">Consulta con Banquito</p>
            </div>
            <div class="user-badge">
                <a href="${pageContext.request.contextPath}/user/menu" class="link link--muted">Volver al menú</a>
            </div>
        </div>

        <% if (error != null) { %>
            <div class="alert alert--error"><%= error %></div>
        <% } %>

        <form method="post" action="${pageContext.request.contextPath}/user/credito/estado" class="form">
            <div class="form__group">
                <label class="form__label" for="cedula">Cédula del cliente</label>
                <input id="cedula" name="cedula" type="text" class="form__input" required value="<%= cedula != null ? cedula : "" %>">
            </div>
            <div class="form__actions">
                <button type="submit" class="btn btn--primary">Consultar</button>
            </div>
        </form>

        <% if (evaluacion != null) { %>
            <section class="section">
                <h2 class="section__title">Resultado</h2>
                <p><strong>Cédula:</strong> <%= cedula %></p>
                <p><strong>Sujeto de crédito:</strong>
                    <% if (evaluacion.getSujetoCredito() != null && evaluacion.getSujetoCredito()) { %>
                        Sí
                    <% } else { %>
                        No
                    <% } %>
                </p>
                <% if (evaluacion.getMontoMaximoCredito() != null) { %>
                    <p><strong>Monto máximo:</strong> $<%= evaluacion.getMontoMaximoCredito() %></p>
                <% } %>
                <% if (evaluacion.getCreditoAprobado() != null) { %>
                    <p><strong>Crédito actual:</strong>
                        <% if (evaluacion.getCreditoAprobado()) { %>
                            Aprobado
                        <% } else { %>
                            Rechazado
                        <% } %>
                    </p>
                <% } %>
                <% if (evaluacion.getCuotaMensual() != null) { %>
                    <p><strong>Cuota mensual estimada:</strong> $<%= evaluacion.getCuotaMensual() %></p>
                <% } %>
                <% if (evaluacion.getIdCredito() != null) { %>
                    <p><strong>ID Crédito:</strong> <%= evaluacion.getIdCredito() %></p>
                <% } %>
                <% if (evaluacion.getMensaje() != null) { %>
                    <p class="text-muted"><%= evaluacion.getMensaje() %></p>
                <% } %>
            </section>
        <% } %>
    </div>
</div>
</body>
</html>
