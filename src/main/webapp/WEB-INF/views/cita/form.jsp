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

    <%-- La JSP es la capa de presentacion: solo muestra datos preparados
         por el controlador y envia el formulario de vuelta al controlador. --%>
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
            <%-- Si la cita no tiene id, el formulario enviara una creacion nueva
                 a /cita/guardar, que sera atendido por CitaController. --%>
            <form action="${pageContext.request.contextPath}/cita/guardar" method="post">
        </c:when>
        <c:otherwise>
            <%-- Si la cita ya existe, el flujo cambia al metodo de actualizacion
                 del controlador, pero la misma vista se reutiliza. --%>
            <form action="${pageContext.request.contextPath}/cita/actualizar" method="post">
            <input type="hidden" name="id" value="${cita.id}"/>
        </c:otherwise>
    </c:choose>

        <div class="form-group">
            <label>Especialidad</label>
            <select name="especialidad" id="especialidadSelect" required>
                <option value="">-- Selecciona --</option>
                <%-- 'especialidades' viene del Model cargado por el controlador. --%>
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
            <select name="medico" id="medicoSelect" required>
                <option value="">-- Selecciona --</option>
                <c:forEach var="medico" items="${medicos}">
                    <option value="${medico.nombre}" data-especialidad="${medico.especialidad}"
                        <c:if test="${cita.medico == medico.nombre}">selected</c:if>>
                        ${medico.nombre}
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
                <%-- La vista no calcula horarios; solo renderiza la lista entregada por servicio. --%>
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

<script>
    (function () {
        const especialidadSelect = document.getElementById('especialidadSelect');
        const medicoSelect = document.getElementById('medicoSelect');

        function filtrarMedicos() {
            const especialidad = especialidadSelect.value;
            let visibleSeleccionado = false;

            Array.from(medicoSelect.options).forEach(function (option) {
                if (!option.value) {
                    option.hidden = false;
                    option.disabled = false;
                    return;
                }

                const visible = option.dataset.especialidad === especialidad;
                option.hidden = !visible;
                option.disabled = !visible;
                if (visible && option.selected) {
                    visibleSeleccionado = true;
                }
            });

            if (!visibleSeleccionado) {
                medicoSelect.value = '';
                const unicoMedico = Array.from(medicoSelect.options).find(function (option) {
                    return option.value && !option.disabled;
                });
                if (unicoMedico) {
                    medicoSelect.value = unicoMedico.value;
                }
            }
        }

        especialidadSelect.addEventListener('change', filtrarMedicos);
        filtrarMedicos();
    })();
</script>

</body>
</html>
