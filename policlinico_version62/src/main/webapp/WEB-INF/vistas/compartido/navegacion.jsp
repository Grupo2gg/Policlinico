<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:choose>
    <c:when test="${not empty sessionScope.usuarioId}">
        <nav class="navegacion">
            <a class="navegacion-logo" href="${pageContext.request.contextPath}/">Policlinico</a>
            <div class="navegacion-links">
                <c:choose>
                    <c:when test="${sessionScope.rol == 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/admin/inicio">Inicio</a>
                        <a href="${pageContext.request.contextPath}/admin/usuarios">Usuarios</a>
                        <a href="${pageContext.request.contextPath}/medico/admin/lista">Medicos</a>
                        <a href="${pageContext.request.contextPath}/especialidades">Especialidades</a>
                        <a href="${pageContext.request.contextPath}/disponibilidades">Disponibilidad</a>
                        <a href="${pageContext.request.contextPath}/horarios">Horarios</a>
                        <a href="${pageContext.request.contextPath}/citas/admin/lista">Citas</a>
                        <a href="${pageContext.request.contextPath}/atenciones/admin/lista">Atenciones</a>
                        <a href="${pageContext.request.contextPath}/mi-perfil">Mi perfil</a>
                    </c:when>
                    <c:when test="${sessionScope.rol == 'MEDICO'}">
                        <a href="${pageContext.request.contextPath}/medico/inicio">Inicio</a>
                        <a href="${pageContext.request.contextPath}/medico/mis-citas">Mis citas</a>
                        <a href="${pageContext.request.contextPath}/atenciones/mis-atenciones">Atenciones</a>
                        <a href="${pageContext.request.contextPath}/medico/mis-horarios">Mis horarios</a>
                        <a href="${pageContext.request.contextPath}/mi-perfil">Mi perfil</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/paciente/inicio">Inicio</a>
                        <a href="${pageContext.request.contextPath}/citas/nueva">Reservar cita</a>
                        <a href="${pageContext.request.contextPath}/citas/mis-citas">Mis citas</a>
                        <a href="${pageContext.request.contextPath}/mi-perfil">Mi perfil</a>
                        <a href="${pageContext.request.contextPath}/contacto">Contacto</a>
                        <a href="${pageContext.request.contextPath}/promociones">Promociones</a>
                    </c:otherwise>
                </c:choose>
                <div class="navegacion-usuario">
                    <span>${sessionScope.nombreUsuario}</span>
                    <span class="insignia-rol ${sessionScope.rol}">${sessionScope.rol}</span>
                </div>
                <form action="${pageContext.request.contextPath}/logout" method="post">
                    <button type="submit">Salir</button>
                </form>
            </div>
        </nav>
    </c:when>
    <c:otherwise>
        <nav class="navegacion">
            <div style="display: flex; gap: 20px; align-items: center;">
                <a class="navegacion-logo" href="${pageContext.request.contextPath}/">Policlinico</a>
                <a href="${pageContext.request.contextPath}/contacto" style="color: white; text-decoration: none; font-size: 0.95rem; font-weight: 500;">Contacto</a>
                <a href="${pageContext.request.contextPath}/promociones" style="color: white; text-decoration: none; font-size: 0.95rem; font-weight: 500;">Promociones</a>
            </div>
            <div class="navegacion-links">
                <a class="boton boton-primario" href="${pageContext.request.contextPath}/login" style="padding: 6px 12px; font-size: 0.9rem; text-decoration: none;">Ingresar</a>
                <a class="boton boton-secundario" href="${pageContext.request.contextPath}/registro" style="padding: 6px 12px; font-size: 0.9rem; text-decoration: none; color: white;">Registro</a>
            </div>
        </nav>
    </c:otherwise>
</c:choose>
