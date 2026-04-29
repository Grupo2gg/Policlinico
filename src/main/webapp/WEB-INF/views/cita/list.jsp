<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis Citas - DERMO; PLASTICA S.R.L.</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/cita-list.css">
</head>
<body>

<%@ include file="../_header.jsp" %>

<div class="container">

    <div class="page-header">
        <h2>Mis Citas</h2>
        <a href="${pageContext.request.contextPath}/cita/nueva" class="btn btn-primary">
            + Nueva Cita
        </a>
    </div>

    <div class="filtro">
        <label>Filtrar por estado:</label>
        <form method="get">
            <select name="estado" onchange="this.form.submit()">
                <option value="" ${estadoSeleccionado == 'TODAS' ? 'selected' : ''}>Todas</option>
                <option value="PENDIENTE" ${estadoSeleccionado == 'PENDIENTE' ? 'selected' : ''}>Pendiente</option>
                <option value="CONFIRMADA" ${estadoSeleccionado == 'CONFIRMADA' ? 'selected' : ''}>Confirmada</option>
                <option value="CANCELADA" ${estadoSeleccionado == 'CANCELADA' ? 'selected' : ''}>Cancelada</option>
            </select>
        </form>
    </div>

    <div class="tabla-card">
        <table>
            <tr>
                <th>#</th>
                <th>Especialidad</th>
                <th>Médico</th>
                <th>Fecha</th>
                <th>Hora</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
            <c:choose>
                <c:when test="${empty citas}">
                    <tr>
                        <td colspan="7">
                            <div class="empty-msg">
                                No tienes citas registradas
                            </div>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="cita" items="${citas}" varStatus="i">
                        <tr>
                            <td>${i.count}</td>
                            <td>${cita.especialidad}</td>
                            <td>${cita.medico}</td>
                            <td>${cita.fecha}</td>
                            <td>${cita.hora}</td>
                            <td>
                                <span class="badge badge-${fn:toLowerCase(cita.estado)}">
                                    ${cita.estado}
                                </span>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/cita/ver/${cita.id}"
                                   class="btn btn-secondary">Ver</a>
                                <c:if test="${cita.estado == 'PENDIENTE'}">
                                    <a href="${pageContext.request.contextPath}/cita/editar/${cita.id}"
                                       class="btn btn-primary">Editar</a>
                                    <a href="${pageContext.request.contextPath}/cita/cancelar/${cita.id}"
                                       class="btn btn-danger">Cancelar</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </table>
    </div>

    <div class="resumen">
        <div class="resumen-item">
            <strong>${totalCitas}</strong>
            Total
        </div>
        <div class="resumen-item">
            <strong>${totalPendientes}</strong>
            Pendientes
        </div>
        <div class="resumen-item">
            <strong>${totalConfirmadas}</strong>
            Confirmadas
        </div>
        <div class="resumen-item">
            <strong>${totalCanceladas}</strong>
            Canceladas
        </div>
    </div>

</div>

<%@ include file="../_footer.jsp" %>

</body>
</html>
