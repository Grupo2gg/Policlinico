<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Promociones"/></jsp:include>
<section class="dashboard-bienvenida">
    <h1>Promociones</h1>
    <p>Servicios preventivos y paquetes de atencion para pacientes del policlinico.</p>
</section>
<section class="dashboard-tarjetas">
    <div class="tarjeta-resumen">
        <span class="tarjeta-resumen-numero">S/ 80</span>
        <span class="tarjeta-resumen-label">Consulta de medicina general</span>
    </div>
    <div class="tarjeta-resumen">
        <span class="tarjeta-resumen-numero">S/ 120</span>
        <span class="tarjeta-resumen-label">Control pediatrico integral</span>
    </div>
    <div class="tarjeta-resumen">
        <span class="tarjeta-resumen-numero">S/ 150</span>
        <span class="tarjeta-resumen-label">Evaluacion cardiologica basica</span>
    </div>
</section>
<section class="tarjeta">
    <h2 class="tarjeta-titulo">Beneficios para pacientes registrados</h2>
    <p>Reserva desde el sistema, consulta tus citas y recibe orientacion por nuestros canales de contacto.</p>
    <div class="formulario-acciones">
        <a class="boton boton-primario" href="${pageContext.request.contextPath}/citas/nueva">Reservar cita</a>
        <a class="boton boton-secundario" href="${pageContext.request.contextPath}/contacto">Contactar</a>
    </div>
</section>
</main></body></html>
