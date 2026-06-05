<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Login"/></jsp:include>
<section class="tarjeta" style="max-width:420px;margin:40px auto;">
    <h1 class="tarjeta-titulo">Iniciar sesion con Gmail</h1>
    <form class="formulario" action="${pageContext.request.contextPath}/login" method="post">
        <div class="formulario-grupo"><label>Gmail</label><input class="campo-formulario" type="email" name="email" required pattern="^[A-Za-z0-9._%+-]+@gmail\.com$"></div>
        <div class="formulario-grupo"><label>Password</label><input class="campo-formulario" type="password" name="password" required></div>
        <div class="formulario-acciones"><button class="boton boton-primario" type="submit">Ingresar</button><a class="boton boton-secundario" href="${pageContext.request.contextPath}/registro">Registro</a></div>
    </form>
</section>
</main></body></html>
