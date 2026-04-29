<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Servicios - DERMO; PLASTICA S.R.L.</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/publicidad.css">
</head>
<body>

<%@ include file="_header.jsp" %>

<div class="container">
    <h2 class="page-title">Nuestros Servicios</h2>

    <div class="servicios-grid">
        <c:forEach var="s" items="${servicios}">
            <div class="servicio-item">
                <h3>${s.nombre}</h3>
                <p>${s.descripcion}</p>
                <div class="precio">${s.precio}</div>
            </div>
        </c:forEach>
    </div>

    <h2 class="page-title">Especialidades Disponibles</h2>

    <div class="servicios-grid">
        <c:forEach var="esp" items="${especialidades}">
            <div class="servicio-item">
                <h3>${esp.nombre}</h3>
                <p>${esp.descripcion}</p>
                <div class="detalle-item"><strong>Médico:</strong> ${esp.medico}</div>
                <div class="detalle-item"><strong>Horario:</strong> ${esp.horarioDisponible}</div>
                <a class="btn-detalle"
                   href="${pageContext.request.contextPath}/especialidad/ver/${esp.id}">
                    Ver detalle
                </a>
            </div>
        </c:forEach>
    </div>

    <div class="cta">
        <h3>¿Listo para empezar?</h3>
        <a href="${pageContext.request.contextPath}/cita/nueva">
            Reservar mi cita ahora
        </a>
    </div>
</div>

<%@ include file="_footer.jsp" %>

</body>
</html>
