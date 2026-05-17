<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty sessionScope.usuario}">
    <c:redirect url="/login"/>
</c:if>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Registrar Atención Médica - DERMO; PLASTICA S.R.L.</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>

<%@ include file="../_header.jsp" %>

<div class="container">
    <h2>Registrar Atención Médica</h2>

    <div class="card" style="margin-bottom: 20px;">
        <h3>Información de la Cita</h3>
        <p><strong>Paciente:</strong> ${cita.nombrePaciente}</p>
        <p><strong>Motivo de Consulta:</strong> ${cita.motivo}</p>
        <p><strong>Fecha y Hora:</strong> ${cita.fecha} - ${cita.hora}</p>
    </div>

    <div class="card">
        <form action="${pageContext.request.contextPath}/medico/atenciones/guardar" method="post">
            <input type="hidden" name="citaId" value="${atencion.citaId}">
            
            <div class="form-group">
                <label for="diagnostico">Diagnóstico:</label>
                <input type="text" id="diagnostico" name="diagnostico" required placeholder="Ej. Faringitis aguda">
            </div>
            
            <div class="form-group">
                <label for="tratamiento">Tratamiento o Receta:</label>
                <textarea id="tratamiento" name="tratamiento" rows="4" required placeholder="Ej. Paracetamol 500mg cada 8 horas por 3 días" style="width: 100%; padding: 8px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px; font-family: inherit; resize: vertical;"></textarea>
            </div>
            
            <div class="form-group">
                <label for="observaciones">Observaciones (Opcional):</label>
                <textarea id="observaciones" name="observaciones" rows="3" placeholder="Notas adicionales sobre el paciente" style="width: 100%; padding: 8px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px; font-family: inherit; resize: vertical;"></textarea>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Guardar Atención</button>
                <a href="${pageContext.request.contextPath}/medico/atenciones" class="btn btn-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="../_footer.jsp" %>

</body>
</html>
