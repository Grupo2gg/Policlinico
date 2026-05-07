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
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/layout.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/especialidad-list.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/_header.jsp" />

<section>
    <div class="container">
        <h1 class="page-title">Nuestras Especialidades</h1>
        <p class="page-subtitle">Explora las areas medicas y esteticas disponibles en la clinica.</p>

        <c:if test="${not empty mensaje}">
            <div class="alert alert-warning">${mensaje}</div>
        </c:if>

        <c:if test="${empty especialidades}">
            <div class="alert">No hay especialidades disponibles</div>
        </c:if>

        <c:if test="${not empty especialidades}">
            <div class="grid">
                <c:forEach var="esp" items="${especialidades}">
                    <div class="esp-card">
                        <c:if test="${esp.activa}">
                            <span class="badge-activa">Disponible</span>
                        </c:if>
                        <h3>${esp.nombre}</h3>
                        <p>${esp.descripcion}</p>
                        <p class="horario">Horario disponible: ${esp.horarioDisponible}</p>
                        <a href="${pageContext.request.contextPath}/especialidad/ver/${esp.id}" class="btn-ver">Ver detalle</a>
                    </div>
                </c:forEach>
            </div>
        </c:if>
    </div>
</section>

<jsp:include page="/WEB-INF/views/_footer.jsp" />
</body>
</html>
