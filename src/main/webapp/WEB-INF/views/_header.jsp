<div class="navbar">
    <div class="logo">DERMO; PLASTICA S.R.L.</div>
    <div>
        <a href="${pageContext.request.contextPath}/main">Inicio</a>
        <a href="${pageContext.request.contextPath}/cita/list">Mis Citas</a>
        <a href="${pageContext.request.contextPath}/publicidad">Promociones</a>
        <a href="${pageContext.request.contextPath}/contacto">Contacto</a>
        <a href="${pageContext.request.contextPath}/perfil">Mi cuenta</a>
        <c:if test="${not empty sessionScope.usuario}">
            <a href="${pageContext.request.contextPath}/perfil">
                ${sessionScope.usuario.nombre} ${sessionScope.usuario.apellido}
            </a>
        </c:if>
    </div>
</div>
