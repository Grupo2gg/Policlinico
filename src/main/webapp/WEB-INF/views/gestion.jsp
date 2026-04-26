<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión - Dermo &amp; Plástica</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/gestion.css">
</head>
<body>

<%@ include file="_header.jsp" %>

<div class="container">
    <h2 class="page-title">Panel de Gestión</h2>

    <div class="accesos">
        <a href="${pageContext.request.contextPath}/cita/list" class="acceso-card">
            <h3>Mis Citas</h3>
            <p>Ver y gestionar citas</p>
        </a>
        <a href="${pageContext.request.contextPath}/cita/nueva" class="acceso-card">
            <h3>Nueva Cita</h3>
            <p>Reservar una cita</p>
        </a>
        <a href="${pageContext.request.contextPath}/perfil" class="acceso-card">
            <h3>Mi Perfil</h3>
            <p>Ver mis datos</p>
        </a>
        <a href="${pageContext.request.contextPath}/especialidad/list" class="acceso-card">
            <h3>Especialidades</h3>
            <p>Ver disponibles</p>
        </a>
    </div>

    <div class="card">
        <h2>Últimas Citas</h2>
        <table>
            <tr>
                <th>Especialidad</th>
                <th>Fecha</th>
                <th>Estado</th>
            </tr>
            <c:choose>
                <c:when test="${empty ultimasCitas}">
                    <tr>
                        <td colspan="3">No tienes citas registradas</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="cita" items="${ultimasCitas}">
                        <tr>
                            <td>${cita.especialidad}</td>
                            <td>${cita.fecha}</td>
                            <td>
                                <span class="badge badge-${fn:toLowerCase(cita.estado)}">
                                    ${cita.estado}
                                </span>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </table>
    </div>
</div>

<%@ include file="_footer.jsp" %>

</body>
</html>
