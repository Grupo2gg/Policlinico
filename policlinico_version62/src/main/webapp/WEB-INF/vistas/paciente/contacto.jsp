<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Contacto"/></jsp:include>
<section class="dashboard-bienvenida">
    <h1>Contacto</h1>
    <p>Canales de atencion del policlinico para consultas, reservas y orientacion al paciente.</p>
</section>
<section class="grid">
    <div class="tarjeta">
        <h2 class="tarjeta-titulo">Central telefonica</h2>
        <p>Telefono: 987 654 321</p>
        <p>Horario: lunes a sabado de 8:00 a 18:00</p>
    </div>
    <div class="tarjeta">
        <h2 class="tarjeta-titulo">Correo</h2>
        <p>atencionpoliclinico@gmail.com</p>
        <p>Respuesta dentro del horario de atencion.</p>
    </div>
    <div class="tarjeta">
        <h2 class="tarjeta-titulo">Ubicacion</h2>
        <p>Av. Principal 123, Lima</p>
        <p>Referencia: frente al parque central.</p>
    </div>
</section>
</main></body></html>
