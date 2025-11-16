<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ec.edu.monster.model.LoginResponse"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menú Cliente - Comercializadora Monster</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%
    LoginResponse usuario = (LoginResponse) request.getAttribute("usuario");
%>
<div class="page">
    <div class="card">
        <div class="card__header">
            <div>
                <h1 class="title">Comercializadora Monster</h1>
                <p class="subtitle">Menú principal cliente</p>
            </div>
            <div class="user-badge">
                <span class="user-badge__name"><%= usuario != null ? usuario.getUsername() : "" %></span>
                <span class="user-badge__role">ROL: USER</span>
                <a href="${pageContext.request.contextPath}/logout" class="link link--muted">Cerrar sesión</a>
            </div>
        </div>
        <div class="grid grid--2">
            <a href="${pageContext.request.contextPath}/user/catalogo" class="menu-tile">
                <h2>Catálogo de electrodomésticos</h2>
                <p>Consulta los productos disponibles y sus precios.</p>
            </a>
            <a href="${pageContext.request.contextPath}/user/venta" class="menu-tile">
                <h2>Registrar nueva venta</h2>
                <p>Registra una compra en efectivo o crédito directo.</p>
            </a>
            <a href="${pageContext.request.contextPath}/user/credito/estado" class="menu-tile">
                <h2>Estado de crédito</h2>
                <p>Verifica si el cliente es sujeto de crédito.</p>
            </a>
            <a href="${pageContext.request.contextPath}/user/credito/amortizacion" class="menu-tile">
                <h2>Tabla de amortización</h2>
                <p>Consulta el detalle de pagos de un crédito aprobado.</p>
            </a>
        </div>
    </div>
</div>
</body>
</html>
