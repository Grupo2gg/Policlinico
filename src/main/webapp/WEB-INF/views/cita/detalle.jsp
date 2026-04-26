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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color:#f5f8fb; }
        .detail-card { border:0; border-radius:1rem; box-shadow:0 .75rem 2rem rgba(15,23,42,.08); }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<section class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-xl-8">
                <div class="card detail-card">
                    <div class="card-body p-4 p-lg-5">
                        <div class="d-flex justify-content-between align-items-start flex-wrap gap-3 mb-4">
                            <div>
                                <h1 class="fw-bold mb-1" style="color:#1a3c5e;">Detalle de Cita</h1>
                                <p class="text-secondary mb-0">Información completa de la reserva médica.</p>
                            </div>
                            <div>
                                <c:if test="${cita.estado == 'PENDIENTE'}"><span class="badge fs-6 text-bg-warning">PENDIENTE</span></c:if>
                                <c:if test="${cita.estado == 'CONFIRMADA'}"><span class="badge fs-6 text-bg-success">CONFIRMADA</span></c:if>
                                <c:if test="${cita.estado == 'CANCELADA'}"><span class="badge fs-6 text-bg-danger">CANCELADA</span></c:if>
                            </div>
                        </div>

                        <div class="row g-3">
                            <div class="col-md-6"><strong>ID:</strong> ${cita.id}</div>
                            <div class="col-md-6"><strong>Usuario ID:</strong> ${cita.usuarioId}</div>
                            <div class="col-md-6"><strong>Paciente:</strong> ${cita.nombrePaciente}</div>
                            <div class="col-md-6"><strong>Especialidad:</strong> ${cita.especialidad}</div>
                            <div class="col-md-6"><strong>Médico:</strong> ${cita.medico}</div>
                            <div class="col-md-6"><strong>Fecha:</strong> ${cita.fecha}</div>
                            <div class="col-md-6"><strong>Hora:</strong> ${cita.hora}</div>
                            <div class="col-md-6"><strong>Fecha de creación:</strong> ${cita.fechaCreacion}</div>
                            <div class="col-12"><strong>Motivo:</strong> ${cita.motivo}</div>
                        </div>

                        <div class="d-flex gap-2 mt-4 flex-wrap">
                            <c:if test="${cita.estado == 'PENDIENTE'}">
                                <a href="${pageContext.request.contextPath}/cita/editar/${cita.id}" class="btn btn-outline-primary">Editar</a>
                                <a href="${pageContext.request.contextPath}/cita/cancelar/${cita.id}" class="btn btn-outline-danger">Cancelar</a>
                            </c:if>
                            <a href="${pageContext.request.contextPath}/cita/list" class="btn btn-secondary">Volver a lista</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
