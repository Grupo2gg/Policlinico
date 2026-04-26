<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Login - Dermo &amp; Plástica</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>

<div class="login-box">
    <h2>Iniciar Sesión</h2>
    <p class="subtitulo">Ingresa tus credenciales</p>

    <c:if test="${not empty error}">
        <div class="alert-error">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" required placeholder="correo@ejemplo.com"/>
        </div>
        <div class="form-group">
            <label>Contraseña</label>
            <input type="password" name="password" required placeholder="••••••"/>
        </div>
        <button type="submit" class="btn-login">Ingresar</button>
    </form>

    <div class="link-registro">
        ¿No tienes cuenta?
        <a href="${pageContext.request.contextPath}/registro">Regístrate</a>
    </div>
</div>

</body>
</html>
