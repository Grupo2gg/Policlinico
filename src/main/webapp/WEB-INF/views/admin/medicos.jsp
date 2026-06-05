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
    <title>Medicos Admin - DERMO; PLASTICA S.R.L.</title>
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
    <h2>Medicos</h2>

    <c:if test="${not empty mensaje}">
        <div class="alert-error">${mensaje}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert-error">${error}</div>
    </c:if>

    <div class="card">
        <h3>
            <c:choose>
                <c:when test="${modoEdicion}">Editar medico</c:when>
                <c:otherwise>Registrar medico</c:otherwise>
            </c:choose>
        </h3>

        <c:choose>
            <c:when test="${modoEdicion}">
                <form action="${pageContext.request.contextPath}/admin/medicos/actualizar" method="post">
                <input type="hidden" name="id" value="${medico.id}">
            </c:when>
            <c:otherwise>
                <form action="${pageContext.request.contextPath}/admin/medicos/guardar" method="post">
            </c:otherwise>
        </c:choose>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
                <!-- Nombre -->
                <div class="form-group">
                    <label>Nombre *</label>
                    <input type="text" name="nombre" value="${medico.nombre}" placeholder="Ej: Juan Pérez" required>
                </div>

                <!-- Género -->
                <div class="form-group">
                    <label>Género *</label>
                    <select name="genero" required>
                        <option value="">-- Selecciona --</option>
                        <option value="MASCULINO" <c:if test="${medico.genero == 'MASCULINO'}">selected</c:if>>
                            Masculino (Dr.)
                        </option>
                        <option value="FEMENINO" <c:if test="${medico.genero == 'FEMENINO'}">selected</c:if>>
                            Femenino (Dra.)
                        </option>
                    </select>
                </div>

                <!-- Cédula -->
                <div class="form-group">
                    <label>Cédula/Documento *</label>
                    <input type="text" name="cedula" value="${medico.cedula}" placeholder="Ej: 123456789" required>
                </div>

                <!-- Teléfono -->
                <div class="form-group">
                    <label>Teléfono *</label>
                    <input type="tel" name="telefono" value="${medico.telefono}" placeholder="Ej: +34 666 123 456" required>
                </div>

                <!-- Email -->
                <div class="form-group">
                    <label>Email</label>
                    <input type="email" name="email" value="${medico.email}" placeholder="Ej: doctor@hospital.es">
                </div>

                <!-- Dirección -->
                <div class="form-group">
                    <label>Dirección</label>
                    <input type="text" name="direccion" value="${medico.direccion}" placeholder="Ej: Calle Principal 123">
                </div>

                <!-- Especialidad -->
                <div class="form-group">
                    <label>Especialidad *</label>
                    <select name="especialidad" required>
                        <option value="">-- Selecciona --</option>
                        <c:forEach var="esp" items="${especialidades}">
                            <option value="${esp.nombre}" <c:if test="${medico.especialidad == esp.nombre}">selected</c:if>>
                                ${esp.nombre}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Estado -->
                <div class="form-group">
                    <label>Estado *</label>
                    <select name="estado" required>
                        <option value="ACTIVO" <c:if test="${medico.estado == 'ACTIVO'}">selected</c:if>>ACTIVO</option>
                        <option value="INACTIVO" <c:if test="${medico.estado == 'INACTIVO'}">selected</c:if>>INACTIVO</option>
                    </select>
                </div>
            </div>

            <div class="form-actions" style="margin-top: 20px;">
                <button type="submit" class="btn btn-primary">Guardar</button>
                <c:if test="${modoEdicion}">
                    <a href="${pageContext.request.contextPath}/admin/medicos" class="btn btn-secondary">Cancelar</a>
                </c:if>
            </div>
        </form>
    </div>

    <h3>Lista de médicos</h3>
    <table class="table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Género</th>
            <th>Cédula</th>
            <th>Teléfono</th>
            <th>Especialidad</th>
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${medicos}">
            <tr>
                <td>${item.id}</td>
                <td>${item.nombre}</td>
                <td>${item.genero}</td>
                <td>${item.cedula}</td>
                <td>${item.telefono}</td>
                <td>${item.especialidad}</td>
                <td>${item.estado}</td>
                <td>
                    <a class="btn btn-secondary"
                       href="${pageContext.request.contextPath}/admin/medicos/editar/${item.id}">Editar</a>
                    <a class="btn btn-secondary"
                       href="${pageContext.request.contextPath}/admin/medicos/eliminar/${item.id}"
                       onclick="return confirm('Eliminar medico?');">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
