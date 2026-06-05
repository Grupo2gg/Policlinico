<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Mis citas"/></jsp:include>
<h1 class="pagina-titulo">Mis citas</h1>
<c:if test="${empty citas}">
    <p style="color:#666;margin-bottom:1rem;">No hay citas asignadas aun.</p>
</c:if>
<table class="tabla">
    <thead>
        <tr>
            <th>Paciente</th>
            <th>Fecha</th>
            <th>Horario</th>
            <th>Motivo</th>
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="cita" items="${citas}">
            <tr>
                <td>${cita.paciente}</td>
                <td>${cita.fechaTexto}</td>
                <td>${cita.horaInicioTexto} - ${cita.horaFinTexto}</td>
                <td>${cita.motivo}</td>
                <td><span class="etiqueta-estado ${cita.estado}">${cita.estado}</span></td>
                <td class="tabla-acciones">
                    <a class="boton boton-secundario boton-icono" href="${pageContext.request.contextPath}/medico/detalle-cita/${cita.id}">Detalle</a>
                    <form action="${pageContext.request.contextPath}/medico/cita/estado" method="post" style="display:inline-flex;gap:6px;align-items:center;">
                        <input type="hidden" name="citaId" value="${cita.id}">
                        <select class="campo-formulario" name="estado" style="min-width:140px;">
                            <option value="PENDIENTE" ${cita.estado == 'PENDIENTE' ? 'selected' : ''}>PENDIENTE</option>
                            <option value="CONFIRMADA" ${cita.estado == 'CONFIRMADA' ? 'selected' : ''}>CONFIRMADA</option>
                            <option value="CANCELADA" ${cita.estado == 'CANCELADA' ? 'selected' : ''}>CANCELADA</option>
                        </select>
                        <button class="boton boton-primario boton-icono" type="submit">Cambiar</button>
                    </form>
                    <c:if test="${cita.estado == 'CONFIRMADA'}">
                        <a class="boton boton-primario boton-icono" href="${pageContext.request.contextPath}/atenciones/registrar/${cita.id}">Atender</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
</main></body></html>
