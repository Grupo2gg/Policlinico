<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Detalle de Cita</title>
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
                    <h1>Detalle de Cita</h1>
                    <p>Informacion completa de la reserva medica.</p>
                </div>
                <div>
                    <c:if test="${cita.estado == 'PENDIENTE'}"><span class="badge badge-pendiente">PENDIENTE</span></c:if>
                    <c:if test="${cita.estado == 'CONFIRMADA'}"><span class="badge badge-confirmada">CONFIRMADA</span></c:if>
                    <c:if test="${cita.estado == 'CANCELADA'}"><span class="badge badge-cancelada">CANCELADA</span></c:if>
                </div>
            </div>

            <div class="detail-grid">
                <div class="detail-item"><strong>ID</strong> ${cita.id}</div>
                <div class="detail-item"><strong>Usuario ID</strong> ${cita.usuarioId}</div>
                <div class="detail-item"><strong>Paciente</strong> ${cita.nombrePaciente}</div>
                <div class="detail-item"><strong>Especialidad</strong> ${cita.especialidad}</div>
                <div class="detail-item"><strong>Medico</strong> ${cita.medico}</div>
                <div class="detail-item"><strong>Fecha</strong> ${cita.fecha}</div>
                <div class="detail-item"><strong>Hora</strong> ${cita.hora}</div>
                <div class="detail-item"><strong>Fecha de creacion</strong> ${cita.fechaCreacion}</div>
                <div class="detail-item detail-item-full"><strong>Motivo</strong> ${cita.motivo}</div>
            </div>

            <div class="detail-actions">
                <c:if test="${cita.estado == 'PENDIENTE'}">
                    <a href="${pageContext.request.contextPath}/cita/editar/${cita.id}" class="btn btn-primary">Editar</a>
                    <a href="${pageContext.request.contextPath}/cita/cancelar/${cita.id}" class="btn btn-danger">Cancelar</a>
                </c:if>
                <a href="${pageContext.request.contextPath}/cita/list" class="btn btn-secondary">Volver a lista</a>
            </div>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/_footer.jsp" />
</body>
</html>
