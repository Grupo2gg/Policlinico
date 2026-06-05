<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Especialidades"/></jsp:include>
<h1 class="pagina-titulo">Especialidades</h1>
<c:if test="${not empty error}">
  <p style="color:#c0392b;background:#fdecea;border:1px solid #f5c6cb;border-radius:6px;padding:10px 14px;margin-bottom:16px;">${error}</p>
</c:if>
<c:if test="${not empty especialidadDetalle}">
    <section class="tarjeta"><h2 class="tarjeta-titulo">Detalle de especialidad</h2><p>${especialidadDetalle.nombre}</p><p>${especialidadDetalle.descripcion}</p><p><strong>Estado:</strong> <span class="etiqueta-estado ${especialidadDetalle.estado}">${especialidadDetalle.estado}</span></p></section>
</c:if>
<section class="tarjeta">
    <h2 class="tarjeta-titulo">${editando ? 'Actualizar especialidad' : 'Crear especialidad'}</h2>
    <form class="formulario" action="${pageContext.request.contextPath}/especialidades/guardar" method="post">
        <c:if test="${editando}"><input type="hidden" name="id" value="${especialidadFormulario.id}"></c:if>
        <div class="formulario-grupo"><label>Nombre</label><input class="campo-formulario" name="nombre" value="${especialidadFormulario.nombre}" required></div>
        <div class="formulario-grupo"><label>Descripcion</label><textarea class="campo-formulario" name="descripcion">${especialidadFormulario.descripcion}</textarea></div>
        <div class="formulario-grupo"><label>Estado</label><select class="campo-formulario" name="estado"><c:forEach var="estado" items="${estadosEspecialidad}"><option value="${estado}" ${especialidadFormulario.estado == estado ? 'selected' : ''}>${estado}</option></c:forEach></select></div>
        <div class="formulario-acciones"><button class="boton boton-primario" type="submit">${editando ? 'Actualizar' : 'Crear'}</button><a class="boton boton-secundario" href="${pageContext.request.contextPath}/especialidades">Limpiar</a></div>
    </form>
</section>
<table class="tabla">
    <thead><tr><th>Nombre</th><th>Descripcion</th><th>Estado</th><th>Acciones</th></tr></thead>
    <tbody>
    <c:forEach var="especialidad" items="${especialidades}">
        <tr><td>${especialidad.nombre}</td><td>${especialidad.descripcion}</td><td><span class="etiqueta-estado ${especialidad.estado}">${especialidad.estado}</span></td><td class="tabla-acciones"><a class="boton boton-secundario boton-icono" href="${pageContext.request.contextPath}/especialidades/ver/${especialidad.id}">Leer</a><a class="boton boton-primario boton-icono" href="${pageContext.request.contextPath}/especialidades/editar/${especialidad.id}">Editar</a><a class="boton boton-peligro boton-icono" href="${pageContext.request.contextPath}/especialidades/eliminar/${especialidad.id}">Eliminar</a></td></tr>
    </c:forEach>
    </tbody>
</table>
</main></body></html>
