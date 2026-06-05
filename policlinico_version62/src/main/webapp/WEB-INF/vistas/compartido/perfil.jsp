<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Mi perfil"/></jsp:include>
<section class="tarjeta">
    <h1 class="tarjeta-titulo">Mi perfil</h1>
    <form class="formulario" action="${pageContext.request.contextPath}/mi-perfil" method="post">
        <div class="formulario-grupo"><label>Nombre</label><input class="campo-formulario" name="nombre" value="${usuario.nombre}" required></div>
        <div class="formulario-grupo"><label>Apellido</label><input class="campo-formulario" name="apellido" value="${usuario.apellido}" required></div>
        <div class="formulario-grupo"><label>DNI</label><input class="campo-formulario" name="dni" value="${usuario.dni}" required readonly="readonly" style="background-color: #f1f3f5; color: #6c757d; cursor: not-allowed;"></div>
        <div class="formulario-grupo"><label>Email</label><input class="campo-formulario" type="email" name="email" value="${usuario.email}" required readonly="readonly" style="background-color: #f1f3f5; color: #6c757d; cursor: not-allowed;"></div>
        <div class="formulario-grupo"><label>Telefono</label><input class="campo-formulario" name="telefono" value="${usuario.telefono}"></div>
        <div class="formulario-grupo"><label>Nueva password</label><input class="campo-formulario" type="password" name="password"></div>
        <div class="formulario-acciones">
            <button class="boton boton-primario" type="submit">Actualizar datos</button>
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/">Volver</a>
        </div>
    </form>
</section>
</main></body></html>
