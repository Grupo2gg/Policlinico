<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty sessionScope.usuario}">
    <c:redirect url="/login"/>
</c:if>
<c:if test="${sessionScope.usuario.rol != 'ADMIN'}">
    <c:redirect url="/login"/>
</c:if>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dashboard Admin - DERMO; PLASTICA S.R.L.</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<div class="navbar">
    <div class="logo">DERMO; PLASTICA S.R.L. - Admin</div>
    <div class="navbar-menu">
        <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/usuarios">Usuarios</a>
        <a href="${pageContext.request.contextPath}/especialidad/list">Especialidades</a>
        <a href="${pageContext.request.contextPath}/admin/medicos">Medicos</a>
        <a href="${pageContext.request.contextPath}/admin/horarios">Horarios</a>
        <a href="${pageContext.request.contextPath}/admin/citas">Citas</a>
        <form action="${pageContext.request.contextPath}/logout" method="post" class="navbar-logout-form">
            <button type="submit" class="navbar-logout">Cerrar sesion</button>
        </form>
    </div>
</div>

<div class="container">
    <h2>Panel Administrativo</h2>
    <p>Administrador: ${usuario.nombre} ${usuario.apellido}</p>

    <table class="table">
        <tbody>
        <tr><td>Total usuarios</td><td>${totalUsuarios}</td></tr>
        <tr><td>Total citas</td><td>${totalCitas}</td></tr>
        <tr><td>Total especialidades activas</td><td>${totalEspecialidades}</td></tr>
        <tr><td>Total medicos</td><td>${totalMedicos}</td></tr>
        <tr><td>Total horarios</td><td>${totalHorarios}</td></tr>
        </tbody>
    </table>
</div>
</body>
</html>
