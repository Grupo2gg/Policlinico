<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Nuestras Especialidades</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color:#f5f8fb; }
        .service-card { border:0; border-radius:1rem; box-shadow:0 .75rem 2rem rgba(15,23,42,.08); }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<section class="py-5">
    <div class="container">
        <div class="mb-4">
            <h1 class="fw-bold mb-1" style="color:#1a3c5e;">Nuestras Especialidades</h1>
            <p class="text-secondary mb-0">Explora las áreas médicas y estéticas disponibles en la clínica.</p>
        </div>

        <c:if test="${empty especialidades}">
            <div class="alert alert-light border text-secondary">
                No hay especialidades disponibles
            </div>
        </c:if>

        <c:if test="${not empty especialidades}">
            <div class="row g-4">
                <c:forEach var="esp" items="${especialidades}">
                    <div class="col-lg-4 col-md-6">
                        <div class="card service-card h-100">
                            <div class="card-body p-4">
                                <div class="d-flex justify-content-between align-items-start mb-3">
                                    <h2 class="h5 fw-bold mb-0">${esp.nombre}</h2>
                                    <c:if test="${esp.activa}">
                                        <span class="badge text-bg-success">Disponible</span>
                                    </c:if>
                                </div>
                                <p class="text-secondary">${esp.descripcion}</p>
                                <p class="mb-1"><strong>Médico responsable:</strong> ${esp.medico}</p>
                                <p class="mb-3"><strong>Horario disponible:</strong> ${esp.horarioDisponible}</p>
                                <a href="${pageContext.request.contextPath}/especialidad/ver/${esp.id}" class="btn btn-outline-primary">Ver detalle</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>
    </div>
</section>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
