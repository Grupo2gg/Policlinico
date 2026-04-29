<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registro - DERMO; PLASTICA S.R.L.</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/registro.css">
</head>
<body>

<div class="container">
<div class="card">
    <h2>Crear Cuenta</h2>
    <p class="sub">Completa tus datos para registrarte</p>

    <c:if test="${not empty error}">
        <div class="alert-error">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/registro" method="post">
        <div class="form-row">
            <div class="form-group">
                <label>Nombre</label>
                <input type="text" name="nombre" value="${usuario.nombre}" required/>
            </div>
            <div class="form-group">
                <label>Apellido</label>
                <input type="text" name="apellido" value="${usuario.apellido}" required/>
            </div>
        </div>
        <div class="form-row">
            <div class="form-group">
                <label>DNI</label>
                <input type="text" name="dni"
                       value="${usuario.dni}" pattern="[0-9]{8}" maxlength="8" required/>
            </div>
            <div class="form-group">
                <label>Teléfono</label>
                <input type="text" name="telefono" value="${usuario.telefono}" required/>
            </div>
        </div>
        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" value="${usuario.email}" required/>
        </div>
        <div class="form-group">
            <label>Fecha de Nacimiento</label>
            <input type="date" name="fechaNacimiento" value="${usuario.fechaNacimiento}" required/>
        </div>
        <div class="form-row">
            <div class="form-group">
                <label>Contraseña</label>
                <input type="password" name="password" minlength="6" required/>
            </div>
            <div class="form-group">
                <label>Confirmar Contraseña</label>
                <input type="password" name="confirmar" minlength="6" required/>
            </div>
        </div>
        <button type="submit" class="btn-registrar">Crear Cuenta</button>
    </form>

    <div class="link-login">
        ¿Ya tienes cuenta?
        <a href="${pageContext.request.contextPath}/login">Iniciar sesión</a>
    </div>
</div>
</div>

</body>
</html>
