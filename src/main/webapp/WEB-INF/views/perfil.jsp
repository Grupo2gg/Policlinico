<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mi Perfil - DERMO; PLASTICA S.R.L.</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/perfil.css">
</head>
<body>

<%@ include file="_header.jsp" %>

<div class="container">

    <div class="card">
        <div class="avatar">👤</div>
        <h2>Mis Datos</h2>

        <c:if test="${not empty exito}">
            <div class="alert-success">${exito}</div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="alert-error">${error}</div>
        </c:if>

        <div class="dato-item">
            <label>Nombre:</label>
            <span>${usuario.nombre} ${usuario.apellido}</span>
        </div>
        <div class="dato-item">
            <label>Email:</label>
            <span>${usuario.email}</span>
        </div>
        <div class="dato-item">
            <label>DNI:</label>
            <span>${usuario.dni}</span>
        </div>
        <div class="dato-item">
            <label>Teléfono:</label>
            <span>${usuario.telefono}</span>
        </div>
        <div class="dato-item">
            <label>Fecha de nacimiento:</label>
            <span>${usuario.fechaNacimiento}</span>
        </div>
    </div>

    <div class="card">
        <h2>Actualizar Datos</h2>
        <form action="${pageContext.request.contextPath}/perfil/actualizar"
              method="post">
            <input type="hidden" name="id" value="${usuario.id}"/>

            <div class="form-group">
                <label>Nombre</label>
                <input type="text" name="nombre" value="${usuario.nombre}" required/>
            </div>
            <div class="form-group">
                <label>Apellido</label>
                <input type="text" name="apellido" value="${usuario.apellido}" required/>
            </div>
            <div class="form-group">
                <label>Teléfono</label>
                <input type="text" name="telefono" value="${usuario.telefono}"/>
            </div>
            <div class="form-group">
                <label>Email (no editable)</label>
                <input type="email" value="${usuario.email}" readonly/>
            </div>
            <div class="form-group">
                <label>DNI (no editable)</label>
                <input type="text" value="${usuario.dni}" readonly/>
            </div>

            <button type="submit" class="btn-actualizar">Actualizar Datos</button>
        </form>
    </div>

</div>

<%@ include file="_footer.jsp" %>

</body>
</html>
