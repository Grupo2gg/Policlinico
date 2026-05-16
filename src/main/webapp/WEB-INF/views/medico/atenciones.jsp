<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Citas del Médico</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 10px; border: 1px solid #ccc; text-align: left; }
        th { background-color: #f4f4f4; }
        .btn { padding: 5px 10px; background-color: #007bff; color: white; text-decoration: none; border-radius: 3px; }
        .btn:hover { background-color: #0056b3; }
        .btn-inicio { background-color: #6c757d; }
    </style>
</head>
<body>
    <h2>Citas para Atención Médica</h2>
    
    <a href="/main" class="btn btn-inicio">Volver al Menú Principal</a>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Paciente</th>
                <th>Fecha</th>
                <th>Hora</th>
                <th>Estado</th>
                <th>Acción</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="cita" items="${citas}">
                <tr>
                    <td>${cita.id}</td>
                    <td>${cita.nombrePaciente}</td>
                    <td>${cita.fecha}</td>
                    <td>${cita.hora}</td>
                    <td>${cita.estado}</td>
                    <td>
                        <c:if test="${cita.estado == 'PENDIENTE'}">
                            <a href="/medico/atenciones/nueva/${cita.id}" class="btn">Atender</a>
                        </c:if>
                        <c:if test="${cita.estado != 'PENDIENTE'}">
                            <span style="color: green; font-weight: bold;">Atendido</span>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
