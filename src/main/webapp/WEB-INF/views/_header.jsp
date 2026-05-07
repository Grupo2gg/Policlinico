<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="navbar">
    <div class="logo">DERMO; PLASTICA S.R.L.</div>
    <div class="navbar-menu">
        <a href="${pageContext.request.contextPath}/main">Inicio</a>

        <c:if test="${not empty sessionScope.usuario}">
            <c:choose>
                <c:when test="${sessionScope.usuario.rol == 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/admin/dashboard">Panel Admin</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/citas">Mis Citas</a>
                </c:otherwise>
            </c:choose>
            <a href="${pageContext.request.contextPath}/publicidad">Promociones</a>
            <a href="${pageContext.request.contextPath}/contacto">Contacto</a>
            <c:if test="${sessionScope.usuario.rol != 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/perfil">Mi cuenta</a>
            </c:if>
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
