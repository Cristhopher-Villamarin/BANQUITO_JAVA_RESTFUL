<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Comercializadora Monster - Acceso</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body class="login-body">
<main class="login-wrapper">
    <section class="brand-panel">
        <div class="brand-panel__badge">
            <span></span>
            Plataforma comercial &amp; financiera
        </div>
        <h1 class="brand-panel__title">Comercializadora</h1>
        <h1 class="brand-panel__title">MONSTER</h1>
        <p class="brand-panel__subtitle">Ventas, crédito directo y monitoreo de créditos.</p>

        <div class="brand-panel__grid">
    
        </div>

    
    </section>

    <section class="auth-panel">
        <header class="auth-panel__header">
            <p class="auth-panel__eyebrow">Bienvenido</p>
            <h2>Inicia sesión</h2>
            <p>Autoriza tu ingreso para continuar con los procesos comerciales.</p>
        </header>

        <% String error = (String) request.getAttribute("error"); %>
        <% if (error != null) { %>
            <div class="auth-panel__alert"><%= error %></div>
        <% } %>

        <form method="post" action="${pageContext.request.contextPath}/login" class="auth-form">
            <label class="form-field">
                <span class="form-field__label">Usuario</span>
                <span class="form-field__input">
                    <span class="form-field__icon">👤</span>
                    <input id="username" name="username" type="text" required>
                </span>
            </label>
            <label class="form-field">
                <span class="form-field__label">Contraseña</span>
                <span class="form-field__input">
                    <span class="form-field__icon">🔐</span>
                    <input id="password" name="password" type="password" required>
                </span>
            </label>

            <button type="submit" class="auth-submit">Ingresar</button>
        </form>
    </section>
</main>
</body>
</html>
