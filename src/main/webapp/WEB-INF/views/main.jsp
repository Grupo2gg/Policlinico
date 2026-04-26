<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Inicio - Dermo &amp; Plástica</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>

<%@ include file="_header.jsp" %>

<div class="container">

    <div class="hero">
        <h1>Bienvenido a Dermo &amp; Plástica S.R.L.</h1>
        <p>Especialistas en dermatología clínica y cirugía estética en Huancayo</p>
        <a href="${pageContext.request.contextPath}/cita/nueva" class="btn-hero">
            Reservar mi cita
        </a>
    </div>

    <div class="servicios">
        <c:forEach var="esp" items="${especialidades}">
            <div class="servicio-card">
                <h3>${esp.nombre}</h3>
                <p>${esp.descripcion}</p>
            </div>
        </c:forEach>
    </div>

</div>

<%@ include file="_footer.jsp" %>

</body>
</html>
