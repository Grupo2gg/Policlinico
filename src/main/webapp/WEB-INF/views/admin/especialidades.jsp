<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
    <title>Especialidades Admin - DERMO; PLASTICA S.R.L.</title>
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
    <h2>Especialidades</h2>

    <c:if test="${not empty mensaje}">
        <div class="alert-error">${mensaje}</div>
    </c:if>

    <div class="card">
        <h3>
            <c:choose>
                <c:when test="${modoEdicion}">Editar especialidad</c:when>
                <c:otherwise>Crear especialidad</c:otherwise>
            </c:choose>
        </h3>

        <c:choose>
            <c:when test="${modoEdicion}">
                <form action="${pageContext.request.contextPath}/especialidad/actualizar" method="post">
                <input type="hidden" name="id" value="${especialidad.id}">
            </c:when>
            <c:otherwise>
                <form action="${pageContext.request.contextPath}/especialidad/guardar" method="post">
            </c:otherwise>
        </c:choose>

            <div class="form-group">
                <label>Nombre</label>
                <input type="text" name="nombre" value="${especialidad.nombre}" required>
            </div>
            <div class="form-group">
                <label>Descripción</label>
                <input type="text" name="descripcion" value="${especialidad.descripcion}" required>
            </div>
            <div class="form-group">
                <label>Médico</label>
                <input type="text" name="medico" value="${especialidad.medico}">
            </div>

            <!-- Seleccionar Días de la Semana -->
            <div class="form-group">
                <label>Días Disponibles</label>
                <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px;">
                    <label style="display: flex; align-items: center; font-weight: normal;">
                        <input type="checkbox" name="dias" value="Lunes" 
                            <c:if test="${fn:contains(especialidad.horarioDisponible, 'Lunes')}">checked</c:if>>
                        <span style="margin-left: 5px;">Lunes</span>
                    </label>
                    <label style="display: flex; align-items: center; font-weight: normal;">
                        <input type="checkbox" name="dias" value="Martes"
                            <c:if test="${fn:contains(especialidad.horarioDisponible, 'Martes')}">checked</c:if>>
                        <span style="margin-left: 5px;">Martes</span>
                    </label>
                    <label style="display: flex; align-items: center; font-weight: normal;">
                        <input type="checkbox" name="dias" value="Miércoles"
                            <c:if test="${fn:contains(especialidad.horarioDisponible, 'Miércoles')}">checked</c:if>>
                        <span style="margin-left: 5px;">Miércoles</span>
                    </label>
                    <label style="display: flex; align-items: center; font-weight: normal;">
                        <input type="checkbox" name="dias" value="Jueves"
                            <c:if test="${fn:contains(especialidad.horarioDisponible, 'Jueves')}">checked</c:if>>
                        <span style="margin-left: 5px;">Jueves</span>
                    </label>
                    <label style="display: flex; align-items: center; font-weight: normal;">
                        <input type="checkbox" name="dias" value="Viernes"
                            <c:if test="${fn:contains(especialidad.horarioDisponible, 'Viernes')}">checked</c:if>>
                        <span style="margin-left: 5px;">Viernes</span>
                    </label>
                    <label style="display: flex; align-items: center; font-weight: normal;">
                        <input type="checkbox" name="dias" value="Sábado"
                            <c:if test="${fn:contains(especialidad.horarioDisponible, 'Sábado')}">checked</c:if>>
                        <span style="margin-left: 5px;">Sábado</span>
                    </label>
                    <label style="display: flex; align-items: center; font-weight: normal;">
                        <input type="checkbox" name="dias" value="Domingo"
                            <c:if test="${fn:contains(especialidad.horarioDisponible, 'Domingo')}">checked</c:if>>
                        <span style="margin-left: 5px;">Domingo</span>
                    </label>
                </div>
            </div>

            <!-- Rango de Horas -->
            <div class="form-group">
                <label>Hora de Inicio</label>
                <input type="time" name="horaInicio" required>
            </div>
            <div class="form-group">
                <label>Hora de Fin</label>
                <input type="time" name="horaFin" required>
            </div>

            <div class="form-group">
                <label>Activa</label>
                <select name="activa" required>
                    <option value="true" <c:if test="${especialidad.activa}">selected</c:if>>Si</option>
                    <option value="false" <c:if test="${not especialidad.activa}">selected</c:if>>No</option>
                </select>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Guardar</button>
                <c:if test="${modoEdicion}">
                    <a href="${pageContext.request.contextPath}/especialidad/list" class="btn btn-secondary">Cancelar</a>
                </c:if>
            </div>
        </form>
    </div>

    <h3>Lista de especialidades</h3>
    <table class="table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Horario</th>
            <th>Activa</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${especialidades}">
            <tr>
                <td>${item.id}</td>
                <td>${item.nombre}</td>
                <td>${item.horarioDisponible}</td>
                <td>
                    <c:choose>
                        <c:when test="${item.activa}">Si</c:when>
                        <c:otherwise>No</c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <a class="btn btn-secondary"
                       href="${pageContext.request.contextPath}/especialidad/editar/${item.id}">Editar</a>
                    <a class="btn btn-secondary"
                       href="${pageContext.request.contextPath}/especialidad/eliminar/${item.id}"
                       onclick="return confirm('Eliminar especialidad?');">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
