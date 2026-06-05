<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Registro"/></jsp:include>
<section class="tarjeta">
    <h1 class="tarjeta-titulo">Registro de paciente</h1>
    <form class="formulario" action="${pageContext.request.contextPath}/registro" method="post">
        <div class="formulario-grupo"><label>Nombre</label><input class="campo-formulario" name="nombre" required></div>
        <div class="formulario-grupo"><label>Apellido</label><input class="campo-formulario" name="apellido" required></div>
        <div class="formulario-grupo"><label>DNI</label><input class="campo-formulario" name="dni" required maxlength="8" minlength="8" pattern="\d{8}"></div>
        <div class="formulario-grupo"><label>Gmail</label><input class="campo-formulario" type="email" name="email" required pattern="^[A-Za-z0-9._%+-]+@gmail\.com$"></div>
        <div class="formulario-grupo"><label>Password</label><input class="campo-formulario" type="password" name="password" required></div>
        <div class="formulario-grupo"><label>Telefono</label><input class="campo-formulario" name="telefono"></div>
        <div class="formulario-acciones"><button class="boton boton-primario" type="submit">Registrar</button><a class="boton boton-secundario" href="${pageContext.request.contextPath}/login">Volver</a></div>
    </form>
</section>
</main></body></html>
