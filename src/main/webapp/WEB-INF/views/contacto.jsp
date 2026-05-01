<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Contacto - Dermo &amp; Plástica</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/layout.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/contacto.css">
</head>
<body>

<%@ include file="_header.jsp" %>

<div class="container">

    <div class="card">
        <h2>Datos de Contacto</h2>
        <div class="info-item"><strong>Dirección:</strong> Jr. Ejemplo 123, Huancayo</div>
        <div class="info-item"><strong>Teléfono:</strong> 064-123456</div>
        <div class="info-item"><strong>Email:</strong> contacto@dermoplastica.pe</div>
        <div class="info-item"><strong>Horario:</strong> Lun - Sab, 8:00am - 6:00pm</div>
    </div>

    <div class="card">
        <h2>Envíanos un mensaje</h2>
        <form>
            <div class="form-group">
                <label>Nombre</label>
                <input type="text" placeholder="Tu nombre"/>
            </div>
            <div class="form-group">
                <label>Email</label>
                <input type="email" placeholder="Tu correo"/>
            </div>
            <div class="form-group">
                <label>Mensaje</label>
                <textarea placeholder="Escribe tu mensaje..."></textarea>
            </div>
            <button type="button" class="btn-enviar">Enviar</button>
        </form>
    </div>

</div>

<%@ include file="_footer.jsp" %>

</body>
</html>
