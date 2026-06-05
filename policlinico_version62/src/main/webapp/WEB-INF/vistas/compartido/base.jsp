<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${empty param.titulo ? 'Policlinico' : param.titulo}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/variables.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navegacion.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/botones.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tablas.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mensajes.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
<jsp:include page="/WEB-INF/vistas/compartido/navegacion.jsp"/>
<main class="pagina-contenedor">
<jsp:include page="/WEB-INF/vistas/compartido/mensaje.jsp"/>
