<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Medico"/></jsp:include>
<section class="dashboard-bienvenida"><h1>Panel medico</h1><p>Citas asignadas: ${citas}</p><div class="acciones-rapidas"><a class="boton boton-primario" href="${pageContext.request.contextPath}/medico/mis-citas">Mis citas</a><a class="boton boton-secundario" href="${pageContext.request.contextPath}/atenciones/mis-atenciones">Mis atenciones</a></div></section>
</main></body></html>
