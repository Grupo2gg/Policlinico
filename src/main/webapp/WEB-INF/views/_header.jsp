<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="navbar">
    <div class="logo">DERMO; PLASTICA S.R.L.</div>
    <div class="navbar-menu">
        <a href="${pageContext.request.contextPath}/main">Inicio</a>

        <c:if test="${not empty sessionScope.usuario}">
            <a href="${pageContext.request.contextPath}/cita/list">Mis Citas</a>
            <a href="${pageContext.request.contextPath}/publicidad">Promociones</a>
            <a href="${pageContext.request.contextPath}/contacto">Contacto</a>
            <a href="${pageContext.request.contextPath}/perfil">Mi cuenta</a>
            <span class="navbar-user">
                ${sessionScope.usuario.nombre} ${sessionScope.usuario.apellido}
            </span>
            <form action="${pageContext.request.contextPath}/logout" method="post" class="navbar-logout-form">
                <button type="submit" class="navbar-logout">Cerrar sesion</button>
            </form>
        </c:if>

        <c:if test="${empty sessionScope.usuario}">
            <a href="${pageContext.request.contextPath}/publicidad">Promociones</a>
            <a href="${pageContext.request.contextPath}/contacto">Contacto</a>
            <a href="${pageContext.request.contextPath}/login">Iniciar sesion</a>
            <a href="${pageContext.request.contextPath}/registro">Registrarse</a>
        </c:if>
    </div>
</div>
