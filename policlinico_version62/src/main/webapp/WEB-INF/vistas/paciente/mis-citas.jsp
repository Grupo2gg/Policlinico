<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Mis citas"/></jsp:include>
<h1 class="pagina-titulo">Mis citas</h1>
<c:if test="${not empty error}">
    <p class="mensaje-error" style="color:#c0392b;margin-bottom:1rem;">${error}</p>
</c:if>
<table class="tabla">
    <thead><tr><th>Medico</th><th>Especialidad</th><th>Fecha</th><th>Horario</th><th>Estado</th><th>Acciones</th></tr></thead>
    <tbody>
    <c:forEach var="cita" items="${citas}">
        <tr><td>${cita.medico}</td><td>${cita.especialidad}</td><td>${cita.fechaTexto}</td><td>${cita.horaInicioTexto} - ${cita.horaFinTexto}</td><td><span class="etiqueta-estado ${cita.estado}">${cita.estado}</span></td><td class="tabla-acciones"><a class="boton boton-secundario boton-icono" href="${pageContext.request.contextPath}/citas/ver/${cita.id}">Leer</a><a class="boton boton-primario boton-icono" href="${pageContext.request.contextPath}/citas/editar/${cita.id}">Editar</a></td></tr>
    </c:forEach>
    </tbody>
</table>
</main></body></html>
