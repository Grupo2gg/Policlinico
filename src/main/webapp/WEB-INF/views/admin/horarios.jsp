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
    <title>Horarios Admin - DERMO; PLASTICA S.R.L.</title>
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
    <h2>Horarios configurados</h2>

    <c:if test="${not empty mensaje}">
        <div class="alert-error">${mensaje}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert-error">${error}</div>
    </c:if>

    <div class="card">
        <h3>
            <c:choose>
                <c:when test="${modoEdicion}">Editar horario</c:when>
                <c:otherwise>Crear horario</c:otherwise>
            </c:choose>
        </h3>

        <c:choose>
            <c:when test="${modoEdicion}">
                <form action="${pageContext.request.contextPath}/admin/horarios/actualizar" method="post">
                <input type="hidden" name="id" value="${horario.id}">
            </c:when>
            <c:otherwise>
                <form action="${pageContext.request.contextPath}/admin/horarios/guardar" method="post">
            </c:otherwise>
        </c:choose>

            <div class="form-group">
                <label>Hora</label>
                <input type="time" name="hora" value="${horario.hora}" required>
            </div>

            <div class="form-group">
                <label>Disponible</label>
                <select name="disponible" required>
                    <option value="true" <c:if test="${horario.disponible}">selected</c:if>>Si</option>
                    <option value="false" <c:if test="${not horario.disponible}">selected</c:if>>No</option>
                </select>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Guardar</button>
                <c:if test="${modoEdicion}">
                    <a href="${pageContext.request.contextPath}/admin/horarios" class="btn btn-secondary">Cancelar</a>
                </c:if>
            </div>
        </form>
    </div>

    <h3>Medicos activos para horarios</h3>
    <table class="table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Medico</th>
            <th>Especialidad</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="medico" items="${medicos}">
            <tr>
                <td>${medico.id}</td>
                <td>${medico.nombre}</td>
                <td>${medico.especialidad}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <h3>Lista de horarios</h3>
    <table class="table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Hora</th>
            <th>Disponible</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${horarios}">
            <tr>
                <td>${item.id}</td>
                <td>${item.hora}</td>
                <td>
                    <c:choose>
                        <c:when test="${item.disponible}">Si</c:when>
                        <c:otherwise>No</c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <a class="btn btn-secondary"
                       href="${pageContext.request.contextPath}/admin/horarios/editar/${item.id}">Editar</a>
                    <a class="btn btn-secondary"
                       href="${pageContext.request.contextPath}/admin/horarios/eliminar/${item.id}"
                       onclick="return confirm('Eliminar horario?');">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
