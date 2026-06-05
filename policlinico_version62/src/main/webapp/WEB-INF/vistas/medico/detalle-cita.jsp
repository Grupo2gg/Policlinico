<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Detalle cita"/></jsp:include>
<section class="tarjeta">
    <h1 class="tarjeta-titulo">Detalle de cita</h1>
    <p><strong>Paciente:</strong> ${cita.paciente}</p>
    <p><strong>Fecha:</strong> ${cita.fechaTexto}</p>
    <p><strong>Horario:</strong> ${cita.horaInicioTexto} - ${cita.horaFinTexto}</p>
    <p><strong>Motivo:</strong> ${cita.motivo}</p>
    <p><strong>Estado:</strong> <span class="etiqueta-estado ${cita.estado}">${cita.estado}</span></p>
    
    <div style="margin-top: 20px; display: flex; gap: 10px;">
        <c:if test="${cita.estado == 'PENDIENTE'}">
            <a class="boton boton-primario" style="background-color: #28a745; border-color: #28a745;" href="${pageContext.request.contextPath}/medico/confirmar-cita/${cita.id}">Confirmar</a>
            <a class="boton boton-peligro" href="${pageContext.request.contextPath}/medico/no-confirmar-cita/${cita.id}">No Confirmar</a>
        </c:if>
        <c:if test="${cita.estado == 'CONFIRMADA'}">
            <a class="boton boton-primario" href="${pageContext.request.contextPath}/atenciones/registrar/${cita.id}">Registrar atencion</a>
        </c:if>
        <a class="boton boton-secundario" href="${pageContext.request.contextPath}/medico/mis-citas">Volver</a>
    </div>
</section>
</main></body></html>
