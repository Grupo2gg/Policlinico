<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Inicio administrativo"/></jsp:include>

<section style="margin-bottom:22px;">
  <h1 class="pagina-titulo" style="margin-bottom:4px;">Panel administrativo</h1>
  <p style="color:#666;margin:0;">Resumen general del policlínico con métricas calculadas desde la base de datos.</p>
</section>

<%-- ── Tarjetas de totales ── --%>
<section style="display:grid;grid-template-columns:repeat(auto-fit,minmax(150px,1fr));gap:14px;margin-bottom:26px;">
  <div style="background:#fff;border:1px solid #e9ecef;border-radius:12px;padding:16px 18px;box-shadow:0 1px 3px rgba(0,0,0,.05);">
    <div style="font-size:.72rem;letter-spacing:.04em;color:#888;text-transform:uppercase;">Usuarios</div>
    <div style="font-size:1.9rem;font-weight:700;color:#e9573f;">${totalUsuarios}</div>
  </div>
  <div style="background:#fff;border:1px solid #e9ecef;border-radius:12px;padding:16px 18px;box-shadow:0 1px 3px rgba(0,0,0,.05);">
    <div style="font-size:.72rem;letter-spacing:.04em;color:#888;text-transform:uppercase;">Médicos</div>
    <div style="font-size:1.9rem;font-weight:700;color:#2c3e50;">${totalMedicos}</div>
  </div>
  <div style="background:#fff;border:1px solid #e9ecef;border-radius:12px;padding:16px 18px;box-shadow:0 1px 3px rgba(0,0,0,.05);">
    <div style="font-size:.72rem;letter-spacing:.04em;color:#888;text-transform:uppercase;">Especialidades</div>
    <div style="font-size:1.9rem;font-weight:700;color:#2c3e50;">${totalEspecialidades}</div>
  </div>
  <div style="background:#fff;border:1px solid #e9ecef;border-radius:12px;padding:16px 18px;box-shadow:0 1px 3px rgba(0,0,0,.05);">
    <div style="font-size:.72rem;letter-spacing:.04em;color:#888;text-transform:uppercase;">Horarios</div>
    <div style="font-size:1.9rem;font-weight:700;color:#2c3e50;">${totalHorarios}</div>
  </div>
  <div style="background:#fff;border:1px solid #e9ecef;border-radius:12px;padding:16px 18px;box-shadow:0 1px 3px rgba(0,0,0,.05);">
    <div style="font-size:.72rem;letter-spacing:.04em;color:#888;text-transform:uppercase;">Citas</div>
    <div style="font-size:1.9rem;font-weight:700;color:#e9573f;">${totalCitas}</div>
  </div>
  <div style="background:#fff;border:1px solid #e9ecef;border-radius:12px;padding:16px 18px;box-shadow:0 1px 3px rgba(0,0,0,.05);">
    <div style="font-size:.72rem;letter-spacing:.04em;color:#888;text-transform:uppercase;">Atenciones</div>
    <div style="font-size:1.9rem;font-weight:700;color:#2c3e50;">${totalAtenciones}</div>
  </div>
</section>

<%-- ── Gráficas ── --%>
<section style="display:grid;grid-template-columns:repeat(auto-fit,minmax(300px,1fr));gap:20px;margin-bottom:26px;">
  <div style="background:#fff;border:1px solid #e9ecef;border-radius:12px;padding:20px;box-shadow:0 1px 3px rgba(0,0,0,.05);">
    <h3 style="margin:0 0 14px;font-size:1rem;color:#2c3e50;">Citas por estado</h3>
    <div style="position:relative;height:260px;width:100%;"><canvas id="chartCitas"></canvas></div>
  </div>
  <div style="background:#fff;border:1px solid #e9ecef;border-radius:12px;padding:20px;box-shadow:0 1px 3px rgba(0,0,0,.05);">
    <h3 style="margin:0 0 14px;font-size:1rem;color:#2c3e50;">Médicos activos vs inactivos</h3>
    <div style="position:relative;height:260px;width:100%;"><canvas id="chartMedicos"></canvas></div>
  </div>
  <div style="background:#fff;border:1px solid #e9ecef;border-radius:12px;padding:20px;box-shadow:0 1px 3px rgba(0,0,0,.05);">
    <h3 style="margin:0 0 14px;font-size:1rem;color:#2c3e50;">Usuarios por rol</h3>
    <div style="position:relative;height:260px;width:100%;"><canvas id="chartUsuarios"></canvas></div>
  </div>
  <div style="background:#fff;border:1px solid #e9ecef;border-radius:12px;padding:20px;box-shadow:0 1px 3px rgba(0,0,0,.05);">
    <h3 style="margin:0 0 14px;font-size:1rem;color:#2c3e50;">Resumen general</h3>
    <div style="position:relative;height:260px;width:100%;"><canvas id="chartResumen"></canvas></div>
  </div>
</section>

<%-- ── Panel de restricciones (reglas de negocio por estado) ── --%>
<section class="tarjeta" style="margin-bottom:10px;">
  <h2 class="tarjeta-titulo">Panel de restricciones</h2>
  <p style="color:#666;margin:4px 0 14px;font-size:.9rem;">Qué operaciones bloquea cada estado en el sistema.</p>
  <table class="tabla">
    <thead>
      <tr><th>Estado / Entidad</th><th>Operaciones que bloquea</th></tr>
    </thead>
    <tbody>
      <tr><td><span class="etiqueta-estado CANCELADA">Usuario INACTIVO</span></td><td>Inicio de sesión, reserva de citas y acceso al sistema.</td></tr>
      <tr><td><span class="etiqueta-estado CANCELADA">Médico INACTIVO</span></td><td>Recepción de nuevas citas y generación de horarios.</td></tr>
      <tr><td><span class="etiqueta-estado CANCELADA">Especialidad INACTIVA</span></td><td>Aparición en el proceso de reserva de citas.</td></tr>
      <tr><td><span class="etiqueta-estado CANCELADA">Disponibilidad INACTIVA</span></td><td>Visualización y reserva de sus horarios asociados.</td></tr>
      <tr><td><span class="etiqueta-estado BLOQUEADO">Horario BLOQUEADO</span></td><td>Reserva por parte de cualquier paciente.</td></tr>
      <tr><td><span class="etiqueta-estado ATENDIDA">Cita ATENDIDA</span></td><td>Cualquier cambio de estado (es estado final irreversible).</td></tr>
      <tr><td><span class="etiqueta-estado FINALIZADA">Atención FINALIZADA</span></td><td>Edición de la atención y modificación de la cita asociada.</td></tr>
    </tbody>
  </table>
</section>

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.min.js"></script>
<script>
window.addEventListener('load', function(){
  if (typeof Chart === 'undefined') return;
  Chart.defaults.plugins.legend.position = 'bottom';

  new Chart(document.getElementById('chartCitas'), {
    type:'doughnut',
    data:{ labels:['Pendiente','Confirmada','Atendida','Cancelada'],
      datasets:[{ data:[${citasPendientes},${citasConfirmadas},${citasAtendidas},${citasCanceladas}],
      backgroundColor:['#f4b942','#4a90d9','#27ae60','#e74c3c'], borderWidth:2, borderColor:'#fff' }] },
    options:{ responsive:true, maintainAspectRatio:false, cutout:'62%' }
  });

  new Chart(document.getElementById('chartMedicos'), {
    type:'doughnut',
    data:{ labels:['Activos','Inactivos'],
      datasets:[{ data:[${medicosActivos},${medicosInactivos}],
      backgroundColor:['#27ae60','#bdc3c7'], borderWidth:2, borderColor:'#fff' }] },
    options:{ responsive:true, maintainAspectRatio:false, cutout:'62%' }
  });

  new Chart(document.getElementById('chartUsuarios'), {
    type:'bar',
    data:{ labels:['Admin','Médico','Paciente'],
      datasets:[{ label:'Usuarios', data:[${usuariosAdmin},${usuariosMedico},${usuariosPaciente}],
      backgroundColor:['#2c3e50','#4a90d9','#e9573f'], borderRadius:6 }] },
    options:{ responsive:true, maintainAspectRatio:false,
      plugins:{legend:{display:false}}, scales:{y:{beginAtZero:true,ticks:{precision:0}}} }
  });

  new Chart(document.getElementById('chartResumen'), {
    type:'bar',
    data:{ labels:['Usuarios','Médicos','Especial.','Horarios','Citas','Atenc.'],
      datasets:[{ label:'Total', data:[${totalUsuarios},${totalMedicos},${totalEspecialidades},${totalHorarios},${totalCitas},${totalAtenciones}],
      backgroundColor:'#e9573f', borderRadius:6 }] },
    options:{ indexAxis:'y', responsive:true, maintainAspectRatio:false,
      plugins:{legend:{display:false}}, scales:{x:{beginAtZero:true,ticks:{precision:0}}} }
  });
});
</script>
</main></body></html>
