<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty sessionScope.usuario}">
    <c:redirect url="/login"/>
</c:if>
<c:if test="${sessionScope.usuario.rol != 'ADMIN'}">
    <c:redirect url="/login"/>
</c:if>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Usuarios Admin - DERMO; PLASTICA S.R.L.</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<div class="navbar">
    <div class="logo">DERMO; PLASTICA S.R.L. - Admin</div>
    <div class="navbar-menu">
        <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/usuarios">Usuarios</a>
        <a href="${pageContext.request.contextPath}/especialidad/list">Especialidades</a>
        <a href="${pageContext.request.contextPath}/admin/medicos">Medicos</a>
        <a href="${pageContext.request.contextPath}/admin/horarios">Horarios</a>
        <a href="${pageContext.request.contextPath}/admin/citas">Citas</a>
    </div>
</div>

<div class="container">
    <h2>Usuarios registrados</h2>
    <c:if test="${not empty mensaje}">
        <div class="alert-error">${mensaje}</div>
    </c:if>

    <div class="card">
        <h3>
            <c:choose>
                <c:when test="${modoEdicion}">Editar usuario</c:when>
                <c:otherwise>Crear usuario</c:otherwise>
            </c:choose>
        </h3>

        <c:choose>
            <c:when test="${modoEdicion}">
                <form action="${pageContext.request.contextPath}/admin/usuarios/actualizar" method="post">
                <input type="hidden" name="id" value="${usuarioForm.id}">
            </c:when>
            <c:otherwise>
                <form action="${pageContext.request.contextPath}/admin/usuarios/guardar" method="post">
            </c:otherwise>
        </c:choose>

            <div class="form-group">
                <label>Nombre</label>
                <input type="text" name="nombre" value="${usuarioForm.nombre}" required>
            </div>
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" value="${usuarioForm.email}" required>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="text" name="password" value="${usuarioForm.password}" required>
            </div>
            <div class="form-group">
                <label>Rol</label>
                <select name="rol" required>
                    <option value="PACIENTE" <c:if test="${usuarioForm.rol == 'PACIENTE'}">selected</c:if>>PACIENTE</option>
                    <option value="ADMIN" <c:if test="${usuarioForm.rol == 'ADMIN'}">selected</c:if>>ADMIN</option>
                    <option value="MEDICO" <c:if test="${usuarioForm.rol == 'MEDICO'}">selected</c:if>>MEDICO</option>
                </select>
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Guardar</button>
                <c:if test="${modoEdicion}">
                    <a href="${pageContext.request.contextPath}/admin/usuarios" class="btn btn-secondary">Cancelar</a>
                </c:if>
            </div>
        </form>
    </div>

    <table class="table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Email</th>
            <th>Rol</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${usuarios}">
            <tr>
                <td>${item.id}</td>
                <td>${item.nombre}</td>
                <td>${item.email}</td>
                <td>${item.rol}</td>
                <td>
                    <a class="btn btn-secondary"
                       href="${pageContext.request.contextPath}/admin/usuarios/editar/${item.id}">Editar</a>
                    <a class="btn btn-secondary"
                       href="${pageContext.request.contextPath}/admin/usuarios/eliminar/${item.id}"
                       onclick="return confirm('Eliminar usuario?');">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
