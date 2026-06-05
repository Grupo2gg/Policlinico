<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Usuarios"/></jsp:include>
<h1 class="pagina-titulo">Gestión de Usuarios</h1>

<c:if test="${not empty error}">
    <div style="background-color: #f8d7da; color: #721c24; padding: 12px 20px; border: 1px solid #f5c6cb; border-radius: 6px; margin-bottom: 20px; font-family: sans-serif;">
        <strong>¡Atención!</strong> ${error}
    </div>
</c:if>

<c:if test="${not empty usuarioDetalle}">
    <section class="tarjeta" style="margin-bottom:20px; border-left:4px solid #0d6efd;">
        <h2 class="tarjeta-titulo">Detalle del Usuario</h2>
        <div style="display:grid; grid-template-columns:repeat(auto-fill,minmax(200px,1fr)); gap:12px; margin-top:10px;">
            <div><small style="color:#666; display:block;">Nombre completo</small><strong>${usuarioDetalle.nombre} ${usuarioDetalle.apellido}</strong></div>
            <div><small style="color:#666; display:block;">Email</small><strong>${usuarioDetalle.email}</strong></div>
            <div><small style="color:#666; display:block;">DNI</small><strong>${usuarioDetalle.dni}</strong></div>
            <div><small style="color:#666; display:block;">Teléfono</small><strong>${usuarioDetalle.telefono}</strong></div>
            <div><small style="color:#666; display:block;">Rol</small>
                <span class="etiqueta-estado ${usuarioDetalle.rol == 'ADMIN' ? 'CONFIRMADA' : usuarioDetalle.rol == 'MEDICO' ? 'PENDIENTE' : ''}">
                    ${usuarioDetalle.rol}
                </span>
            </div>
        </div>
        <div style="margin-top:14px;">
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/admin/usuarios">← Cerrar detalle</a>
        </div>
    </section>
</c:if>

<section class="tarjeta" style="margin-bottom:24px;">
    <h2 class="tarjeta-titulo">${editando ? 'Actualizar Usuario' : 'Registrar Nuevo Usuario'}</h2>
    <form class="formulario" action="${pageContext.request.contextPath}/admin/usuarios/guardar" method="post">
        <c:if test="${editando}"><input type="hidden" name="id" value="${usuarioFormulario.id}"></c:if>

        <div style="display:grid; grid-template-columns:1fr 1fr; gap:0 20px;">
            <div class="formulario-grupo">
                <label>Nombre <span style="color:#dc3545;">*</span></label>
                <input class="campo-formulario" name="nombre" value="${usuarioFormulario.nombre}" required placeholder="Nombres">
            </div>
            <div class="formulario-grupo">
                <label>Apellido <span style="color:#dc3545;">*</span></label>
                <input class="campo-formulario" name="apellido" value="${usuarioFormulario.apellido}" required placeholder="Apellidos">
            </div>
        </div>

        <div class="formulario-grupo">
                   <label>DNI <span style="color:#dc3545;">*</span></label>
                   <input class="campo-formulario" name="dni" value="${usuarioFormulario.dni}" required
                          maxlength="8" minlength="8" pattern="\d{8}" placeholder="8 dígitos"
                          style="max-width:200px; ${editando ? 'background-color:#f1f3f5; color:#6c757d; cursor:not-allowed;' : ''}"
                          ${editando ? 'readonly="readonly"' : ''}>
                   <c:if test="${editando}"><small style="color:#666; display:block; margin-top:4px;">El DNI no puede modificarse en la edición.</small></c:if>
        </div>

        <div class="formulario-grupo">
            <label>Email <span style="color:#dc3545;">*</span></label>
            <input class="campo-formulario" type="email" name="email" value="${usuarioFormulario.email}" required placeholder="usuario@gmail.com"
                   ${editando ? 'readonly="readonly" style="background-color:#f1f3f5; color:#6c757d; cursor:not-allowed;"' : ''}>
            <c:choose>
                <c:when test="${editando}">
                    <small style="color:#666; display:block; margin-top:4px;">El Correo electrónico no puede modificarse.</small>
                </c:when>
                <c:otherwise>
                    <small style="color:#666; display:block; margin-top:4px;">Solo se aceptan correos Gmail.</small>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="formulario-grupo">
            <label>Contraseña <span style="color:#dc3545;">*</span></label>
            <input class="campo-formulario" type="password" name="password" value="${usuarioFormulario.password}" required
                   placeholder="${editando ? 'Dejar en blanco para no cambiar' : 'Contraseña'}">
        </div>

        <div class="formulario-grupo">
            <label>Teléfono</label>
            <input class="campo-formulario" name="telefono" value="${usuarioFormulario.telefono}" placeholder="Ej: 999 888 777" style="max-width:220px;">
        </div>

        <div class="formulario-grupo">
            <label>Rol <span style="color:#dc3545;">*</span></label>
            <select class="campo-formulario" name="rol" style="max-width:200px;">
                <option value="PACIENTE" ${usuarioFormulario.rol == 'PACIENTE' ? 'selected' : ''}>Paciente</option>
                <option value="MEDICO"   ${usuarioFormulario.rol == 'MEDICO'   ? 'selected' : ''}>Médico</option>
                <option value="ADMIN"    ${usuarioFormulario.rol == 'ADMIN'    ? 'selected' : ''}>Administrador</option>
            </select>
        </div>

        <div class="formulario-acciones">
            <button class="boton boton-primario" type="submit">${editando ? 'Actualizar Usuario' : 'Registrar Usuario'}</button>
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/admin/usuarios">Limpiar</a>
        </div>
    </form>
</section>

<section class="tarjeta">
    <h2 class="tarjeta-titulo">Lista de Usuarios</h2>
    <table class="tabla">
        <thead>
            <tr>
                <th>Nombre</th>
                <th>DNI</th>
                <th>Email</th>
                <th>Teléfono</th>
                <th>Rol</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="usuario" items="${usuarios}">
            <tr>
                <td><strong>${usuario.nombre} ${usuario.apellido}</strong></td>
                <td>${usuario.dni}</td>
                <td>${usuario.email}</td>
                <td>${usuario.telefono}</td>
                <td>
                    <span class="etiqueta-estado ${usuario.rol == 'ADMIN' ? 'CONFIRMADA' : usuario.rol == 'MEDICO' ? 'PENDIENTE' : ''}">
                        ${usuario.rol}
                    </span>
                </td>
                <td class="tabla-acciones">
                    <a class="boton boton-secundario boton-icono" href="${pageContext.request.contextPath}/admin/usuarios/ver/${usuario.id}">Ver</a>
                    <a class="boton boton-primario boton-icono" href="${pageContext.request.contextPath}/admin/usuarios/editar/${usuario.id}">Editar</a>
                    <a class="boton boton-peligro boton-icono" href="${pageContext.request.contextPath}/admin/usuarios/eliminar/${usuario.id}"
                       onclick="return confirm('¿Eliminar al usuario ${usuario.nombre} ${usuario.apellido}?')">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty usuarios}">
            <tr><td colspan="6" style="text-align:center; color:#999; padding:20px;">No hay usuarios registrados.</td></tr>
        </c:if>
        </tbody>
    </table>
</section>
</main></body></html>
