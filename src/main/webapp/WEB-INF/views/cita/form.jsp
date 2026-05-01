<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Cita - DERMO; PLASTICA S.R.L.</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/layout.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/cita-form.css">
</head>
<body>

<%@ include file="../_header.jsp" %>

<div class="container">
<div class="card">

    <h2>
        <c:choose>
            <c:when test="${cita.id == 0}">Nueva Cita</c:when>
            <c:otherwise>Editar Cita</c:otherwise>
        </c:choose>
    </h2>

    <c:if test="${not empty error}">
        <div class="alert-error">${error}</div>
    </c:if>

    <c:choose>
        <c:when test="${cita.id == 0}">
            <form action="${pageContext.request.contextPath}/cita/guardar" method="post">
        </c:when>
        <c:otherwise>
            <form action="${pageContext.request.contextPath}/cita/actualizar" method="post">
            <input type="hidden" name="id" value="${cita.id}"/>
        </c:otherwise>
    </c:choose>

        <div class="form-group">
            <label>Especialidad</label>
            <select name="especialidad" required>
                <option value="">-- Selecciona --</option>
                <c:forEach var="esp" items="${especialidades}">
                    <option value="${esp.nombre}"
                        <c:if test="${cita.especialidad == esp.nombre}">selected</c:if>>
                        ${esp.nombre}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Médico</label>
            <select name="medico" required>
                <option value="">-- Selecciona --</option>
                <c:forEach var="medico" items="${medicos}">
                    <option value="${medico}"
                        <c:if test="${cita.medico == medico}">selected</c:if>>
                        ${medico}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Fecha</label>
            <input type="date" name="fecha" value="${cita.fecha}" min="${hoy}" required/>
        </div>

        <div class="form-group">
            <label>Hora</label>
            <select name="hora" required>
                <option value="">-- Selecciona --</option>
                <c:forEach var="hora" items="${horas}">
                    <option value="${hora}"
                        <c:if test="${cita.hora == hora}">selected</c:if>>
                        ${hora}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Motivo de consulta</label>
            <textarea name="motivo" required>${cita.motivo}</textarea>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Guardar</button>
            <a href="${pageContext.request.contextPath}/cita/list"
               class="btn btn-secondary">Cancelar</a>
        </div>

    </form>
</div>
</div>

<%@ include file="../_footer.jsp" %>

</body>
</html>
