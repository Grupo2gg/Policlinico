<div class="navbar">
    <div class="logo">DERMO &amp; PLÁSTICA S.R.L.</div>
    <div>
        <a href="${pageContext.request.contextPath}/main">Inicio</a>
        <a href="${pageContext.request.contextPath}/especialidad/list">Servicios</a>
        <a href="${pageContext.request.contextPath}/contacto">Contacto</a>
        <a href="${pageContext.request.contextPath}/publicidad">Publicidad</a>
        <a href="${pageContext.request.contextPath}/perfil">Mi cuenta</a>
        <c:if test="${not empty sessionScope.usuario}">
            <a href="${pageContext.request.contextPath}/perfil">
                ${sessionScope.usuario.nombre} ${sessionScope.usuario.apellido}
            </a>
        </c:if>
    </div>
</div>
