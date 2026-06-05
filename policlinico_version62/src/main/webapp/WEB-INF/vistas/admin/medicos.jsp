<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Médicos"/></jsp:include>
<h1 class="pagina-titulo">Gestión de Médicos</h1>

<c:if test="${not empty error}">
  <p style="color:#c0392b;background:#fdecea;border:1px solid #f5c6cb;border-radius:6px;padding:10px 14px;margin-bottom:16px;">${error}</p>
</c:if>

<c:if test="${not empty medicoDetalle}">
  <section class="tarjeta" style="margin-bottom:20px;border-left:4px solid #0d6efd;">
    <h2 class="tarjeta-titulo">Detalle del Médico</h2>
    <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(200px,1fr));gap:12px;margin-top:10px;">
      <div><small style="color:#666;display:block;">Nombre completo</small><strong>${medicoDetalle.nombre} ${medicoDetalle.apellido}</strong></div>
      <div><small style="color:#666;display:block;">Email</small><strong>${medicoDetalle.email}</strong></div>
      <div><small style="color:#666;display:block;">Especialidad</small><strong>${medicoDetalle.especialidad}</strong></div>
    </div>
    <div style="margin-top:14px;">
      <a class="boton boton-secundario" href="${pageContext.request.contextPath}/medico/admin/lista">← Cerrar detalle</a>
    </div>
  </section>
</c:if>

<section class="tarjeta" style="margin-bottom:24px;">
  <h2 class="tarjeta-titulo">${editando ? 'Actualizar Médico' : 'Registrar Nuevo Médico'}</h2>
  <form class="formulario" action="${pageContext.request.contextPath}/medico/admin/guardar" method="post">
    <c:if test="${editando}">
      <input type="hidden" name="id" value="${medicoFormulario.id}">
      <input type="hidden" name="usuarioId" value="${medicoFormulario.usuarioId}">
    </c:if>

    <div class="formulario-grupo">
      <label>Usuario <span style="color:#dc3545;">*</span></label>
      <c:choose>
        <c:when test="${editando}">
          <input class="campo-formulario" type="text" readonly
                 value="${medicoFormulario.nombre} ${medicoFormulario.apellido} — ${medicoFormulario.email}"
                 style="max-width:420px;background-color:#f1f3f5;color:#6c757d;cursor:not-allowed;">
          <small style="color:#666;">El usuario vinculado no se puede cambiar al editar.</small>
        </c:when>
        <c:otherwise>
          <select class="campo-formulario" name="usuarioId" required style="max-width:420px;">
            <option value="">— Seleccione un usuario —</option>
            <c:forEach var="usuario" items="${usuarios}">
              <option value="${usuario.id}" ${medicoFormulario.usuarioId == usuario.id ? 'selected' : ''}>
                ${usuario.nombre} ${usuario.apellido} — ${usuario.email}
              </option>
            </c:forEach>
          </select>
          <small style="color:#666;">Solo aparecen usuarios con rol MÉDICO que aún no están vinculados a un médico.</small>
        </c:otherwise>
      </c:choose>
    </div>

    <div class="formulario-grupo">
      <label>Especialidad <span style="color:#dc3545;">*</span></label>
      <select class="campo-formulario" name="especialidadId" required style="max-width:300px;">
        <option value="">— Seleccione especialidad —</option>
        <c:forEach var="especialidad" items="${especialidades}">
          <option value="${especialidad.id}" ${medicoFormulario.especialidadId == especialidad.id ? 'selected' : ''}>
            ${especialidad.nombre}
          </option>
        </c:forEach>
      </select>
    </div>

    <div class="formulario-grupo">
      <label>Estado</label>
      <select class="campo-formulario" name="activo" style="max-width:180px;">
        <option value="true"  ${medicoFormulario.activo eq true ? 'selected' : ''}>Activo</option>
        <option value="false" ${medicoFormulario.activo eq false ? 'selected' : ''}>Inactivo</option>
      </select>
    </div>

    <div class="formulario-acciones">
      <button class="boton boton-primario" type="submit">${editando ? 'Actualizar Médico' : 'Registrar Médico'}</button>
      <a class="boton boton-secundario" href="${pageContext.request.contextPath}/medico/admin/lista">Limpiar</a>
    </div>
  </form>
</section>

<section class="tarjeta">
  <h2 class="tarjeta-titulo">Lista de Médicos</h2>
  <table class="tabla">
    <thead>
      <tr><th>Nombre</th><th>Email</th><th>Especialidad</th><th>Estado</th><th>Acciones</th></tr>
    </thead>
    <tbody>
    <c:forEach var="medico" items="${medicos}">
      <tr>
        <td><strong>${medico.nombre} ${medico.apellido}</strong></td>
        <td>${medico.email}</td>
        <td>${medico.especialidad}</td>
        <td>
          <span class="etiqueta-estado ${medico.activo ? 'CONFIRMADA' : 'CANCELADA'}">
            ${medico.activo ? 'Activo' : 'Inactivo'}
          </span>
        </td>
        <td class="tabla-acciones">
          <a class="boton boton-secundario boton-icono" href="${pageContext.request.contextPath}/medico/admin/ver/${medico.id}">Ver</a>
          <a class="boton boton-primario boton-icono"   href="${pageContext.request.contextPath}/medico/admin/editar/${medico.id}">Editar</a>
          <a class="boton boton-peligro boton-icono"    href="${pageContext.request.contextPath}/medico/admin/eliminar/${medico.id}"
             onclick="return confirm('¿Eliminar al médico ${medico.nombre} ${medico.apellido}?')">Eliminar</a>
        </td>
      </tr>
    </c:forEach>
    <c:if test="${empty medicos}">
      <tr><td colspan="5" style="text-align:center;color:#999;padding:20px;">No hay médicos registrados.</td></tr>
    </c:if>
    </tbody>
  </table>
</section>
</main></body></html>
