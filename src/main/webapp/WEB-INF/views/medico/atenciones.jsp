<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty sessionScope.usuario}">
    <c:redirect url="/login"/>
</c:if>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Citas del Médico - DERMO; PLASTICA S.R.L.</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>

<%@ include file="../_header.jsp" %>

<div class="container">
    <h2>Citas para Atención Médica</h2>

    <table class="table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Paciente</th>
            <th>Fecha</th>
            <th>Hora</th>
            <th>Estado</th>
            <th>Acción</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="cita" items="${citas}">
            <tr>
                <td>${cita.id}</td>
                <td>${cita.nombrePaciente}</td>
                <td>${cita.fecha}</td>
                <td>${cita.hora}</td>
                <td>${cita.estado}</td>
                <td>
                    <c:choose>
                        <c:when test="${cita.estado == 'PENDIENTE'}">
                            <a href="${pageContext.request.contextPath}/medico/atenciones/nueva/${cita.id}" class="btn btn-primary">Atender</a>
                        </c:when>
                        <c:otherwise>
                            <span style="color: #28a745; font-weight: bold;">Atendido</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="../_footer.jsp" %>

</body>
</html>
