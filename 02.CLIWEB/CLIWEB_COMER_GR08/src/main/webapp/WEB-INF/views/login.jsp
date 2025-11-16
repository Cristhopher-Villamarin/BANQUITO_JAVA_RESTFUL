<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Comercializadora Monster - Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="page">
    <div class="card card--narrow">
        <h1 class="title">Comercializadora Monster</h1>
        <h2 class="subtitle">Iniciar sesión</h2>

        <% String error = (String) request.getAttribute("error"); %>
        <% if (error != null) { %>
            <div class="alert alert--error"><%= error %></div>
        <% } %>

        <form method="post" action="${pageContext.request.contextPath}/login" class="form">
            <div class="form__group">
                <label for="username" class="form__label">Usuario</label>
                <input id="username" name="username" type="text" class="form__input" required>
            </div>
            <div class="form__group">
                <label for="password" class="form__label">Contraseña</label>
                <input id="password" name="password" type="password" class="form__input" required>
            </div>
            <button type="submit" class="btn btn--primary btn--full">Entrar</button>
        </form>
    </div>
</div>
</body>
</html>
