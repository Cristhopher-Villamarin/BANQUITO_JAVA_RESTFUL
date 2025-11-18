<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ec.edu.monster.model.LoginResponse"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menú Cliente - Comercializadora Monster</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-menu.css">
</head>
<body>
<%
    LoginResponse usuario = (LoginResponse) request.getAttribute("usuario");
%>
<div class="page page--content">
    <div class="layout">
        <header class="layout__header">
            <div class="layout__header-content">
                <h1 class="layout__title">Comercializadora Monster</h1>
                <p class="layout__subtitle">Consulta catálogo, registra ventas y controla el ciclo del crédito desde un solo lugar.</p>
                <div class="layout__badges">
                    <span class="pill">Usuario <%= usuario != null ? usuario.getUsername() : "" %></span>
                    
                </div>
            </div>
            <div class="layout__header-actions">
                <a href="${pageContext.request.contextPath}/logout" class="btn btn--ghost">Cerrar sesión</a>
            </div>
        </header>

        <section class="section-panel section-panel--accent">
            <div class="section-panel__header">
                <div>
                    <h2 class="section-panel__title">Menú de opciones</h2>
                    <p class="layout__subtitle">Selecciona una opción.</p>
                </div>
            </div>
            <div class="tile-grid">
                <a href="${pageContext.request.contextPath}/user/catalogo" class="tile">
                    <h2>Catálogo de electrodomésticos</h2>
                    <p>Explora precios actualizados y disponibilidad antes de cotizar.</p>
                </a>
                <a href="${pageContext.request.contextPath}/user/venta" class="tile">
                    <h2>Registrar nueva venta</h2>
                    <p>Captura datos del cliente, forma de pago y productos en una sola pantalla.</p>
                </a>
                <a href="${pageContext.request.contextPath}/user/credito/estado" class="tile">
                    <h2>Estado de crédito</h2>
                    <p>Verifica si el cliente es sujeto de crédito directo con Banquito.</p>
                </a>
                <a href="${pageContext.request.contextPath}/user/credito/amortizacion" class="tile">
                    <h2>Tabla de amortización</h2>
                    <p>Consulta cuotas pendientes y montos del financiamiento aprobado.</p>
                </a>
                <a href="${pageContext.request.contextPath}/user/facturas/cliente" class="tile">
                    <h2>Facturas por cliente</h2>
                    <p>Consulta todas las facturas registradas de un cliente y revisa su detalle.</p>
                </a>
            </div>
        </section>
    </div>
</div>
</body>
</html>
