<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Detalle de cita"/></jsp:include>
<section class="tarjeta">
    <h1 class="tarjeta-titulo">Detalle de cita</h1>
    <p>Medico: ${cita.medico}</p>
    <p>Especialidad: ${cita.especialidad}</p>
    <p>Fecha: ${cita.fechaTexto}</p>
    <p>Horario: ${cita.horaInicioTexto} - ${cita.horaFinTexto}</p>
    <p>Estado: ${cita.estado}</p>
    <p>Motivo: ${cita.motivo}</p>
    <div class="formulario-acciones"><a class="boton boton-primario" href="${pageContext.request.contextPath}/citas/editar/${cita.id}">Editar</a><a class="boton boton-secundario" href="${pageContext.request.contextPath}/citas/mis-citas">Volver</a></div>
</section>
</main></body></html>
