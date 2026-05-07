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
    <title>Citas Admin - DERMO; PLASTICA S.R.L.</title>
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
    </div>
</div>

<div class="container">
    <h2>Todas las citas</h2>
    <c:if test="${not empty mensaje}">
        <div class="alert-error">${mensaje}</div>
    </c:if>

    <div class="card">
        <h3>
            <c:choose>
                <c:when test="${modoEdicion}">Editar cita</c:when>
                <c:otherwise>Crear cita</c:otherwise>
            </c:choose>
        </h3>

        <c:choose>
            <c:when test="${modoEdicion}">
                <form action="${pageContext.request.contextPath}/admin/citas/actualizar" method="post">
                <input type="hidden" name="id" value="${cita.id}">
            </c:when>
            <c:otherwise>
                <form action="${pageContext.request.contextPath}/admin/citas/guardar" method="post">
            </c:otherwise>
        </c:choose>

            <div class="form-group">
                <label>Seleccionar Paciente *</label>
                <select name="usuarioId" id="pacienteRegistradoSelect" required>
                    <option value="0">-- Selecciona --</option>
                    <c:forEach var="paciente" items="${usuarios}">
                        <option value="${paciente.id}" <c:if test="${cita.usuarioId == paciente.id}">selected</c:if>>
                            ${paciente.nombre} ${paciente.apellido} - DNI: ${paciente.dni}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Especialidad</label>
                <select name="especialidad" id="especialidadSelect" required>
                    <option value="">-- Selecciona --</option>
                    <c:forEach var="esp" items="${especialidades}">
                        <option value="${esp.nombre}" <c:if test="${cita.especialidad == esp.nombre}">selected</c:if>>
                            ${esp.nombre}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Medico</label>
                <select name="medico" id="medicoSelect" required>
                    <option value="">-- Selecciona --</option>
                    <c:forEach var="medico" items="${medicos}">
                        <option value="${medico.nombre}" data-especialidad="${medico.especialidad}" <c:if test="${cita.medico == medico.nombre}">selected</c:if>>
                            ${medico.nombre}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Fecha</label>
                <input type="date" name="fecha" value="${cita.fecha}" required>
            </div>
            <div class="form-group">
                <label>Hora</label>
                <select name="hora" required>
                    <option value="">-- Selecciona --</option>
                    <c:forEach var="hora" items="${horas}">
                        <option value="${hora}" <c:if test="${cita.hora == hora}">selected</c:if>>
                            ${hora}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Estado</label>
                <select name="estado" required>
                    <option value="PENDIENTE" <c:if test="${cita.estado == 'PENDIENTE'}">selected</c:if>>Pendiente</option>
                    <option value="CONFIRMADA" <c:if test="${cita.estado == 'CONFIRMADA'}">selected</c:if>>Confirmada</option>
                    <option value="CANCELADA" <c:if test="${cita.estado == 'CANCELADA'}">selected</c:if>>Cancelada</option>
                </select>
            </div>
            <div class="form-group">
                <label>Motivo</label>
                <input type="text" name="motivo" value="${cita.motivo}" required>
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Guardar</button>
                <c:if test="${modoEdicion}">
                    <a href="${pageContext.request.contextPath}/admin/citas" class="btn btn-secondary">Cancelar</a>
                </c:if>
            </div>
        </form>
    </div>

    <table class="table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Paciente</th>
            <th>Especialidad</th>
            <th>Medico</th>
            <th>Fecha</th>
            <th>Hora</th>
            <th>Estado</th>
            <th>Motivo</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${citas}">
            <tr>
                <td>${item.id}</td>
                <td>${item.nombrePaciente}</td>
                <td>${item.especialidad}</td>
                <td>${item.medico}</td>
                <td>${item.fecha}</td>
                <td>${item.hora}</td>
                <td>${item.estado}</td>
                <td>${item.motivo}</td>
                <td>
                    <a class="btn btn-secondary"
                       href="${pageContext.request.contextPath}/admin/citas/editar/${item.id}">Editar</a>
                    <a class="btn btn-secondary"
                       href="${pageContext.request.contextPath}/admin/citas/eliminar/${item.id}"
                       onclick="return confirm('Eliminar cita?');">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
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
