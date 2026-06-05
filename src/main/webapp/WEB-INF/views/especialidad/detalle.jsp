<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Detalle de Especialidad</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/layout.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/detail.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/_header.jsp" />

<section class="detail-section">
    <div class="detail-container">
        <div class="detail-card">
            <div class="detail-header">
                <div>
                    <h1>${especialidad.nombre}</h1>
                    <p>${especialidad.descripcion}</p>
                </div>
                <c:if test="${especialidad.activa}">
                    <span class="badge badge-disponible">Disponible</span>
                </c:if>
            </div>

            <div class="detail-block">
                <h2 class="section-title">Horarios disponibles</h2>
                <p>${especialidad.horarioDisponible}</p>
            </div>

            <div class="detail-block">
                <h2 class="section-title">Datos generales</h2>
                <div class="detail-grid">
                    <div class="detail-item"><strong>ID</strong> ${especialidad.id}</div>
                    <div class="detail-item"><strong>Estado</strong> ${especialidad.activa ? 'Activa' : 'No disponible'}</div>
                    <div class="detail-item"><strong>Area</strong> ${especialidad.nombre}</div>
                </div>
            </div>

            <div class="detail-actions">
                <a href="${pageContext.request.contextPath}/cita/nueva?especialidad=${especialidad.nombre}" class="btn btn-teal">
                    Reservar cita para esta especialidad
                </a>
                <a href="${pageContext.request.contextPath}/especialidad/list" class="btn btn-outline">Volver</a>
            </div>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/_footer.jsp" />
</body>
</html>
