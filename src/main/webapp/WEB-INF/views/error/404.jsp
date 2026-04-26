<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Error 404</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color:#f5f8fb; }
        .error-card { border:0; border-radius:1rem; box-shadow:0 .75rem 2rem rgba(15,23,42,.08); }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-7">
                <div class="card error-card">
                    <div class="card-body p-5 text-center">
                        <h1 class="display-4 fw-bold text-danger">404</h1>
                        <h2 class="h4 fw-bold mb-3">No pudimos encontrar lo que buscas</h2>
                        <p class="text-secondary mb-4">${not empty mensaje ? mensaje : 'El recurso solicitado no está disponible o ya no existe.'}</p>
                        <a href="${pageContext.request.contextPath}/main" class="btn text-white" style="background-color:#0d7c6e;">Volver al inicio</a>
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
