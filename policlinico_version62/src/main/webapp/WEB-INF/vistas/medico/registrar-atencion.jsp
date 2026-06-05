<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Registrar atencion"/></jsp:include>
<section class="tarjeta">
    <h1 class="tarjeta-titulo">Registrar atencion</h1>
    <form class="formulario" action="${pageContext.request.contextPath}/atenciones/guardar" method="post">
        <input type="hidden" name="citaId" value="${cita.id}">
        <input type="hidden" name="estado" value="EN_PROCESO">
        <div class="formulario-grupo"><label>Estado de atencion</label><div class="campo-formulario">EN_PROCESO</div></div>
        <div class="formulario-grupo"><label>Diagnostico</label><input class="campo-formulario" name="diagnostico" required></div>
        <div class="formulario-grupo"><label>Observaciones</label><textarea class="campo-formulario" name="observaciones"></textarea></div>
        <div class="formulario-acciones"><button class="boton boton-primario" type="submit">Guardar</button></div>
    </form>
</section>
</main></body></html>
