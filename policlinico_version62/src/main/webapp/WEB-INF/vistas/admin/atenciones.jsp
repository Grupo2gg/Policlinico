<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Atenciones"/></jsp:include>
<h1 class="pagina-titulo">Atenciones</h1>

<c:if test="${not empty atencionDetalle}">
  <section class="tarjeta" style="margin-bottom:20px; border-left:4px solid #0d6efd;">
    <h2 class="tarjeta-titulo">Detalle de atención</h2>
    <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(200px,1fr));gap:12px;margin-top:10px;">
      <div><small style="color:#666;display:block;">Paciente</small><strong>${atencionDetalle.paciente}</strong></div>
      <div><small style="color:#666;display:block;">Médico</small><strong>${atencionDetalle.nombreMedico}</strong></div>
      <div><small style="color:#666;display:block;">Fecha</small><strong>${atencionDetalle.fechaAtencion}</strong></div>
      <div><small style="color:#666;display:block;">Diagnóstico</small><strong>${atencionDetalle.diagnostico}</strong></div>
      <div><small style="color:#666;display:block;">Observaciones</small><strong>${atencionDetalle.observaciones}</strong></div>
      <div><small style="color:#666;display:block;">Estado</small>
        <span class="etiqueta-estado ${atencionDetalle.estado}">${atencionDetalle.estado}</span>
      </div>
    </div>
    <div style="margin-top:14px;">
      <a class="boton boton-secundario" href="${pageContext.request.contextPath}/atenciones/admin/lista">← Cerrar detalle</a>
    </div>
  </section>
</c:if>

<section class="tarjeta">
  <h2 class="tarjeta-titulo">Lista de Atenciones</h2>
  <table class="tabla">
    <thead>
      <tr><th>Paciente</th><th>Médico</th><th>Fecha</th><th>Diagnóstico</th><th>Observaciones</th><th>Estado</th><th>Acciones</th></tr>
    </thead>
    <tbody>
    <c:forEach var="a" items="${atenciones}">
      <tr>
        <td><strong>${a.paciente}</strong></td>
        <td>${a.nombreMedico}</td>
        <td>${a.fechaAtencion}</td>
        <td>${a.diagnostico}</td>
        <td>${a.observaciones}</td>
        <td><span class="etiqueta-estado ${a.estado}">${a.estado}</span></td>
        <td class="tabla-acciones">
          <a class="boton boton-secundario boton-icono" href="${pageContext.request.contextPath}/atenciones/admin/ver/${a.id}">Ver</a>
        </td>
      </tr>
    </c:forEach>
    <c:if test="${empty atenciones}">
      <tr><td colspan="7" style="text-align:center;color:#999;padding:20px;">No hay atenciones registradas.</td></tr>
    </c:if>
    </tbody>
  </table>
</section>
</main></body></html>
