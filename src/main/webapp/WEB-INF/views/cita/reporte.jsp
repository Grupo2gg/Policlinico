<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Reporte de Mis Citas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color:#f5f8fb; }
        .metric-card, .panel-card { border:0; border-radius:1rem; box-shadow:0 .75rem 2rem rgba(15,23,42,.08); }
        .metric-value { font-size:2rem; font-weight:800; }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<section class="py-5">
    <div class="container">
        <div class="mb-4">
            <h1 class="fw-bold mb-1" style="color:#1a3c5e;">Reporte de Mis Citas</h1>
            <p class="text-secondary mb-0">Resumen visual y detalle completo del historial de citas.</p>
        </div>

        <div class="row g-4 mb-4">
            <div class="col-md-4">
                <div class="card metric-card h-100">
                    <div class="card-body p-4">
                        <p class="text-secondary mb-2">Pendientes</p>
                        <div class="metric-value text-warning">${totalPendientes}</div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card metric-card h-100">
                    <div class="card-body p-4">
                        <p class="text-secondary mb-2">Confirmadas</p>
                        <div class="metric-value text-success">${totalConfirmadas}</div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card metric-card h-100">
                    <div class="card-body p-4">
                        <p class="text-secondary mb-2">Canceladas</p>
                        <div class="metric-value text-danger">${totalCanceladas}</div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row g-4 mb-4">
            <div class="col-lg-7">
                <div class="card panel-card">
                    <div class="card-body p-4">
                        <h2 class="h5 fw-bold mb-3" style="color:#1a3c5e;">Distribución por estado</h2>
                        <canvas id="citasChart" height="220"></canvas>
                    </div>
                </div>
            </div>
            <div class="col-lg-5">
                <div class="card panel-card h-100">
                    <div class="card-body p-4">
                        <h2 class="h5 fw-bold mb-3" style="color:#1a3c5e;">Frecuencia por especialidad</h2>
                        <c:set var="especialidadesProcesadas" value="" />
                        <div class="table-responsive">
                            <table class="table table-sm align-middle mb-0">
                                <thead>
                                <tr>
                                    <th>Especialidad</th>
                                    <th>Cantidad</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="cita" items="${citas}">
                                    <c:if test="${not fn:contains(especialidadesProcesadas, '|' + cita.especialidad + '|')}">
                                        <c:set var="cantidadEspecialidad" value="0" />
                                        <c:forEach var="item" items="${citas}">
                                            <c:if test="${item.especialidad == cita.especialidad}">
                                                <c:set var="cantidadEspecialidad" value="${cantidadEspecialidad + 1}" />
                                            </c:if>
                                        </c:forEach>
                                        <tr>
                                            <td>${cita.especialidad}</td>
                                            <td>${cantidadEspecialidad}</td>
                                        </tr>
                                        <c:set var="especialidadesProcesadas" value="${especialidadesProcesadas}|${cita.especialidad}|" />
                                    </c:if>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="card panel-card">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                        <tr>
                            <th>#</th>
                            <th>Especialidad</th>
                            <th>Médico</th>
                            <th>Fecha</th>
                            <th>Hora</th>
                            <th>Estado</th>
                            <th>Motivo</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="cita" items="${citas}" varStatus="status">
                            <tr>
                                <td>${status.index + 1}</td>
                                <td>${cita.especialidad}</td>
                                <td>${cita.medico}</td>
                                <td>${cita.fecha}</td>
                                <td>${cita.hora}</td>
                                <td>
                                    <c:if test="${cita.estado == 'PENDIENTE'}"><span class="badge text-bg-warning">PENDIENTE</span></c:if>
                                    <c:if test="${cita.estado == 'CONFIRMADA'}"><span class="badge text-bg-success">CONFIRMADA</span></c:if>
                                    <c:if test="${cita.estado == 'CANCELADA'}"><span class="badge text-bg-danger">CANCELADA</span></c:if>
                                </td>
                                <td>${cita.motivo}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    var pendientes = ${totalPendientes};
    var confirmadas = ${totalConfirmadas};
    var canceladas = ${totalCanceladas};

    new Chart(document.getElementById('citasChart'), {
        type: 'doughnut',
        data: {
            labels: ['Pendientes', 'Confirmadas', 'Canceladas'],
            datasets: [{
                data: [pendientes, confirmadas, canceladas],
                backgroundColor: ['#f0ad4e', '#198754', '#dc3545']
            }]
        },
        options: {
            plugins: {
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
</script>
</body>
</html>
