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
          href="${pageContext.request.contextPath}/css/main.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color:#f5f8fb; }
        .detail-card { border:0; border-radius:1rem; box-shadow:0 .75rem 2rem rgba(15,23,42,.08); }
        .section-title { color:#1a3c5e; font-weight:700; }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/_header.jsp" />

<section class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-xl-9">
                <div class="card detail-card">
                    <div class="card-body p-4 p-lg-5">
                        <div class="d-flex justify-content-between align-items-start flex-wrap gap-3 mb-4">
                            <div>
                                <h1 class="fw-bold mb-1">${especialidad.nombre}</h1>
                                <p class="text-secondary mb-0">${especialidad.descripcion}</p>
                            </div>
                            <c:if test="${especialidad.activa}">
                                <span class="badge fs-6 text-bg-success">Disponible</span>
                            </c:if>
                        </div>

                        <div class="mb-4">
                            <h2 class="h5 section-title">Información del médico</h2>
                            <p class="mb-0">${especialidad.medico}</p>
                        </div>

                        <div class="mb-4">
                            <h2 class="h5 section-title">Horarios disponibles</h2>
                            <p class="mb-0">${especialidad.horarioDisponible}</p>
                        </div>

                        <div class="mb-4">
                            <h2 class="h5 section-title">Datos generales</h2>
                            <div class="row g-3">
                                <div class="col-md-4"><strong>ID:</strong> ${especialidad.id}</div>
                                <div class="col-md-4"><strong>Estado:</strong> ${especialidad.activa ? 'Activa' : 'No disponible'}</div>
                                <div class="col-md-4"><strong>Área:</strong> ${especialidad.nombre}</div>
                            </div>
                        </div>

                        <div class="d-flex gap-2 flex-wrap">
                            <a href="${pageContext.request.contextPath}/cita/nueva?especialidad=${especialidad.nombre}" class="btn text-white" style="background-color:#0d7c6e;">
                                Reservar cita para esta especialidad
                            </a>
                            <a href="${pageContext.request.contextPath}/especialidad/list" class="btn btn-outline-secondary">Volver</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/_footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
