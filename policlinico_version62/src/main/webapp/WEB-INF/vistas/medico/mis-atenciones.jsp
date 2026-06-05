<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Atenciones"/></jsp:include>
<h1 class="pagina-titulo">Atenciones</h1>

<c:if test="${not empty error}">
  <p style="color:#c0392b;background:#fdecea;border:1px solid #f5c6cb;border-radius:6px;padding:10px 14px;margin-bottom:16px;">${error}</p>
</c:if>

<c:if test="${not empty atencionEditar}">
  <section class="tarjeta" style="margin-bottom:24px;border-left:4px solid #f0ad4e;">
    <h2 class="tarjeta-titulo">Editar atención (EN_PROCESO)</h2>
    <form class="formulario" action="${pageContext.request.contextPath}/atenciones/actualizar" method="post">
      <input type="hidden" name="id" value="${atencionEditar.id}">
      <input type="hidden" name="citaId" value="${atencionEditar.citaId}">

      <div class="formulario-grupo">
        <label>Paciente</label>
        <div style="padding:9px 13px;background:#f8f9fa;border:1px solid #dee2e6;border-radius:6px;max-width:400px;font-weight:500;">
          ${atencionEditar.paciente}
        </div>
      </div>

      <div class="formulario-grupo">
        <label>Diagnóstico</label>
        <input class="campo-formulario" name="diagnostico" required value="${atencionEditar.diagnostico}">
      </div>

      <div class="formulario-grupo">
        <label>Observaciones</label>
        <textarea class="campo-formulario" name="observaciones" rows="4">${atencionEditar.observaciones}</textarea>
      </div>

      <div class="formulario-grupo">
        <label>Estado</label>
        <div style="padding:9px 13px;background:#f8f9fa;border:1px solid #dee2e6;border-radius:6px;max-width:220px;font-weight:500;">
          EN_PROCESO
        </div>
      </div>

      <div class="formulario-acciones">
        <button class="boton boton-primario" type="submit">Guardar cambios</button>
        <a class="boton boton-secundario" href="${pageContext.request.contextPath}/atenciones/mis-atenciones">Cancelar</a>
      </div>
    </form>
  </section>
</c:if>

<section class="tarjeta" style="margin-bottom:24px;">
  <h2 class="tarjeta-titulo">Crear atención</h2>
  <form class="formulario" id="frmAtencion" action="${pageContext.request.contextPath}/atenciones/guardar" method="post">

    <div class="formulario-grupo">
      <label>Cita</label>
      <select class="campo-formulario" name="citaId" id="selCita" required onchange="actualizarFecha()">
        <option value="">— Seleccione una cita confirmada —</option>
        <c:forEach var="cita" items="${citasConfirmadas}">
          <option value="${cita.id}"
                  data-fecha="${cita.fechaTexto}"
                  ${cita.id == citaPreseleccionada ? 'selected' : ''}>
            #${cita.id} — ${cita.paciente} — ${cita.fechaTexto} ${cita.horaInicioTexto}
          </option>
        </c:forEach>
      </select>
    </div>

    <div class="formulario-grupo">
      <label>Médico</label>
      <div style="padding:9px 13px;background:#f8f9fa;border:1px solid #dee2e6;border-radius:6px;max-width:400px;font-weight:500;">
        ${sessionScope.nombreUsuario}
      </div>
    </div>

    <div class="formulario-grupo">
      <label>Fecha atención</label>
      <%-- Fecha estática: se muestra como texto y el hidden envía el valor --%>
      <input type="hidden" name="fechaAtencion" id="h_fechaAtencion" value="${fechaAtenciónPreseleccionada}">
      <div id="lblFecha" style="padding:9px 13px;background:#f8f9fa;border:1px solid #dee2e6;border-radius:6px;max-width:220px;font-weight:500;color:#495057;">
        <c:choose>
          <c:when test="${not empty fechaAtenciónPreseleccionada}">${fechaAtenciónPreseleccionada}</c:when>
          <c:otherwise>— Seleccione una cita —</c:otherwise>
        </c:choose>
      </div>
    </div>

    <div class="formulario-grupo">
      <label>Diagnóstico</label>
      <input class="campo-formulario" name="diagnostico" required placeholder="Ingrese el diagnóstico">
    </div>

    <div class="formulario-grupo">
      <label>Observaciones</label>
      <textarea class="campo-formulario" name="observaciones" rows="4" placeholder="Observaciones adicionales..."></textarea>
    </div>

    <input type="hidden" name="estado" value="EN_PROCESO">
    <div class="formulario-grupo">
      <label>Estado inicial</label>
      <div style="padding:9px 13px;background:#f8f9fa;border:1px solid #dee2e6;border-radius:6px;max-width:220px;font-weight:500;">
        EN_PROCESO
      </div>
    </div>

    <div class="formulario-acciones">
      <button class="boton boton-primario" type="submit">Crear</button>
      <a class="boton boton-secundario" href="${pageContext.request.contextPath}/atenciones/mis-atenciones">Limpiar</a>
    </div>
  </form>
</section>

<section class="tarjeta">
  <h2 class="tarjeta-titulo">Mis atenciones registradas</h2>
  <table class="tabla">
    <thead>
      <tr><th>Paciente</th><th>Fecha</th><th>Diagnóstico</th><th>Observaciones</th><th>Estado</th><th>Acciones</th></tr>
    </thead>
    <tbody>
    <c:forEach var="a" items="${atenciones}">
      <tr>
        <td><strong>${a.paciente}</strong></td>
        <td>${a.fechaAtencion}</td>
        <td>${a.diagnostico}</td>
        <td>${a.observaciones}</td>
        <td><span class="etiqueta-estado ${a.estado}">${a.estado}</span></td>
        <td class="tabla-acciones">
          <c:if test="${a.estado == 'EN_PROCESO'}">
            <a class="boton boton-secundario boton-icono" href="${pageContext.request.contextPath}/atenciones/editar/${a.id}">Editar</a>
            <a class="boton boton-primario boton-icono" href="${pageContext.request.contextPath}/atenciones/finalizar/${a.id}"
               onclick="return confirm('¿Finalizar esta atención? La cita quedará ATENDIDA.')">Finalizar</a>
          </c:if>
        </td>
      </tr>
    </c:forEach>
    <c:if test="${empty atenciones}">
      <tr><td colspan="6" style="text-align:center;color:#999;padding:20px;">No hay atenciones registradas.</td></tr>
    </c:if>
    </tbody>
  </table>
</section>

<script>
// Datos de citas para obtener la fecha
var citaFechas = {};
document.querySelectorAll('#selCita option[data-fecha]').forEach(function(opt){
    citaFechas[opt.value] = opt.dataset.fecha;
});

function actualizarFecha() {
    var citaId = document.getElementById('selCita').value;
    var fecha  = citaFechas[citaId] || '';
    document.getElementById('h_fechaAtencion').value = fecha;
    document.getElementById('lblFecha').textContent  = fecha || '— Seleccione una cita —';
}

// Si ya hay cita preseleccionada, aplicar al cargar
window.addEventListener('DOMContentLoaded', function(){
    var sel = document.getElementById('selCita');
    if (sel.value) actualizarFecha();
});
</script>
</main></body></html>
