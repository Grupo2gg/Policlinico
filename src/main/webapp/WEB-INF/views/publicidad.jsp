<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Servicios - Dermo &amp; Plástica</title>
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
