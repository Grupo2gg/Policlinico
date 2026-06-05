<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Paciente"/></jsp:include>
<section class="dashboard-bienvenida"><h1>Hola, ${sessionScope.nombreUsuario}</h1><p>Gestiona tus citas medicas y revisa la informacion del policlinico.</p><div class="acciones-rapidas"><a class="boton boton-primario" href="${pageContext.request.contextPath}/citas/nueva">Reservar cita</a><a class="boton boton-secundario" href="${pageContext.request.contextPath}/citas/mis-citas">Mis citas</a><a class="boton boton-secundario" href="${pageContext.request.contextPath}/paciente/mi-perfil">Actualizar datos</a><a class="boton boton-secundario" href="${pageContext.request.contextPath}/contacto">Contacto</a><a class="boton boton-secundario" href="${pageContext.request.contextPath}/promociones">Promociones</a></div></section>
</main></body></html>
