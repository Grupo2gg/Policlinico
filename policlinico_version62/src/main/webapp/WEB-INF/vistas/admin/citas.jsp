<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Citas"/></jsp:include>
<h1 class="pagina-titulo">Gestión de Citas</h1>

<c:if test="${not empty error or not empty param.error}">
  <p style="color:#c0392b;background:#fdecea;border:1px solid #f5c6cb;border-radius:6px;padding:10px 14px;margin-bottom:16px;">
    ${not empty error ? error : 'Revise que haya elegido un horario y vuelva a intentar.'}
  </p>
</c:if>

<%-- ══════════════════ WIZARD: NUEVA / EDITAR CITA ══════════════════ --%>
<section class="tarjeta" style="margin-bottom:24px;">
  <h2 class="tarjeta-titulo">${editando ? 'Actualizar Cita' : 'Nueva Cita'}</h2>

  <%-- Indicador de pasos --%>
  <div id="stepIndicator" style="display:flex;gap:0;margin-bottom:24px;">
    <div class="step-ind active" id="ind1" style="flex:1;text-align:center;padding:10px 6px;border-bottom:3px solid #0d6efd;font-weight:bold;font-size:.88rem;color:#0d6efd;cursor:pointer;" onclick="irPaso(1)">
      1 · Paciente
    </div>
    <div class="step-ind" id="ind2" style="flex:1;text-align:center;padding:10px 6px;border-bottom:3px solid #dee2e6;font-weight:normal;font-size:.88rem;color:#aaa;">
      2 · Médico y Horario
    </div>
    <div class="step-ind" id="ind3" style="flex:1;text-align:center;padding:10px 6px;border-bottom:3px solid #dee2e6;font-weight:normal;font-size:.88rem;color:#aaa;">
      3 · Motivo y Confirmar
    </div>
  </div>

  <form id="frmCita" action="${pageContext.request.contextPath}/citas/admin/guardar" method="post">
    <c:if test="${editando}">
      <input type="hidden" name="id" value="${citaFormulario.id}">
      <input type="hidden" name="estado" value="${citaFormulario.estado}">
    </c:if>
    <input type="hidden" name="horarioId" id="h_horarioId">

    <%-- ── PASO 1: Paciente ──────────────────────────────────────── --%>
    <div id="paso1">
      <div class="formulario-grupo">
        <label>BUSCAR PACIENTE</label>
        <input type="text" class="campo-formulario" id="buscarPaciente" placeholder="Buscar por nombre o DNI…"
               oninput="filtrarPacientes()" style="max-width:440px; margin-bottom:10px;">
        <div id="listaPacientes" style="display:grid;grid-template-columns:repeat(auto-fill,minmax(260px,1fr));gap:8px;max-height:300px;overflow-y:auto;"></div>
        <input type="hidden" name="pacienteId" id="h_pacienteId" value="${citaFormulario.pacienteId}">
        <div id="pacienteSeleccionado" style="display:none;margin-top:12px;padding:12px 16px;background:#e7f1ff;border:1px solid #b6d4fe;border-radius:8px;">
          <strong id="lblPaciente"></strong><br>
          <small id="lblPacienteDni" style="color:#666;"></small>
        </div>
      </div>
      <div style="margin-top:18px;display:flex;justify-content:flex-end;">
        <button type="button" class="boton boton-primario" onclick="siguientePaso(1)" style="padding:10px 28px;">
          Continuar →
        </button>
      </div>
    </div>

    <%-- ── PASO 2: Especialidad, Médico y Horario ─────────────────── --%>
    <div id="paso2" style="display:none;">
      <div style="display:grid;grid-template-columns:1fr 1fr;gap:16px;margin-bottom:18px;">
        <div class="formulario-grupo" style="margin:0;">
          <label>ESPECIALIDAD</label>
          <select class="campo-formulario" id="selEspecialidad" onchange="filtrarMedicosPorEsp()">
            <option value="">— Todas —</option>
            <c:forEach var="esp" items="${especialidades}">
              <option value="${esp.id}">${esp.nombre}</option>
            </c:forEach>
          </select>
        </div>
        <div class="formulario-grupo" style="margin:0;">
          <label>MÉDICO</label>
          <select class="campo-formulario" id="medicoSelect" onchange="filtrarFechas()">
            <option value="">— Seleccione especialidad —</option>
            <c:forEach var="med" items="${medicos}">
              <option value="${med.id}" data-esp="${med.especialidadId}"
                      ${citaFormulario.medicoId == med.id ? 'selected' : ''}>
                ${med.nombre} ${med.apellido}
              </option>
            </c:forEach>
          </select>
        </div>
      </div>

      <%-- Fechas disponibles --%>
      <div class="formulario-grupo" id="secFechas" style="display:none;">
        <label>FECHA</label>
        <div id="gridFechas" style="display:flex;flex-wrap:wrap;gap:8px;margin-top:8px;"></div>
        <input type="hidden" id="h_fecha">
      </div>

      <%-- Horarios en cuadrícula --%>
      <div class="formulario-grupo" id="secHorarios" style="display:none;margin-top:16px;">
        <label>BLOQUE HORARIO DISPONIBLE</label>
        <div id="gridHorarios" style="display:grid;grid-template-columns:repeat(auto-fill,minmax(110px,1fr));gap:8px;margin-top:8px;"></div>
        <small style="color:#666;margin-top:6px;display:flex;gap:16px;">
          <span>⬜ Disponible</span>
          <span style="color:#0d6efd;">🟦 Seleccionado</span>
        </small>
      </div>

      <div style="margin-top:18px;display:flex;justify-content:space-between;">
        <button type="button" class="boton boton-secundario" onclick="irPaso(1)">← Atrás</button>
        <button type="button" class="boton boton-primario" onclick="siguientePaso(2)" style="padding:10px 28px;">Continuar →</button>
      </div>
    </div>

    <%-- ── PASO 3: Motivo, Observaciones y Confirmar ───────────────── --%>
    <div id="paso3" style="display:none;">
      <%-- Estado estático PENDIENTE --%>
      <div class="formulario-grupo">
        <label>ESTADO DE LA CITA</label>
        <div style="display:flex;gap:10px;flex-wrap:wrap;margin-top:6px;">
          <span style="padding:7px 18px;border-radius:20px;background:#fff3cd;border:2px solid #ffc107;color:#856404;font-weight:bold;">⏳ PENDIENTE</span>
        </div>
        <small style="color:#999;margin-top:5px;display:block;">La cita se guardará en estado Pendiente automáticamente.</small>
      </div>

      <div class="formulario-grupo">
        <label>MOTIVO DE CONSULTA <span style="color:#dc3545;">*</span></label>
        <textarea class="campo-formulario" name="motivo" rows="3" required
                  placeholder="Describa el motivo de la consulta…">${citaFormulario.motivo}</textarea>
      </div>

      <%-- Resumen --%>
      <div style="background:#f0f4ff;border:1px solid #c7d5f8;border-radius:8px;padding:14px 18px;margin-bottom:18px;">
        <strong style="display:block;margin-bottom:8px;">Resumen de la cita</strong>
        <div style="display:grid;grid-template-columns:1fr 1fr;gap:6px;font-size:.9rem;">
          <div><span style="color:#666;">Paciente:</span> <strong id="rPaciente">—</strong></div>
          <div><span style="color:#666;">Médico:</span> <strong id="rMedico">—</strong></div>
          <div><span style="color:#666;">Fecha:</span> <strong id="rFecha">—</strong></div>
          <div><span style="color:#666;">Horario:</span> <strong id="rHorario">—</strong></div>
        </div>
      </div>

      <div style="display:flex;justify-content:space-between;">
        <button type="button" class="boton boton-secundario" onclick="irPaso(2)">← Atrás</button>
        <button type="submit" class="boton boton-primario" style="padding:11px 28px;">
          ${editando ? '💾 Actualizar Cita' : '✅ Confirmar y Guardar Cita'}
        </button>
      </div>
    </div>
  </form>
</section>

<%-- ══════════════════ TABLA DE CITAS ══════════════════ --%>
<section class="tarjeta">
  <h2 class="tarjeta-titulo">Citas Registradas</h2>
  <table class="tabla">
    <thead>
      <tr><th>Paciente</th><th>Médico</th><th>Especialidad</th><th>Fecha</th><th>Horario</th><th>Estado</th><th>Acciones</th></tr>
    </thead>
    <tbody>
    <c:forEach var="cita" items="${citas}">
      <tr>
        <td><strong>${cita.paciente}</strong></td>
        <td>${cita.medico}</td>
        <td>${cita.especialidad}</td>
        <td>${cita.fechaTexto}</td>
        <td>${cita.horaInicioTexto} – ${cita.horaFinTexto}</td>
        <td><span class="etiqueta-estado ${cita.estado}">${cita.estado}</span></td>
        <td class="tabla-acciones">
          <a class="boton boton-secundario boton-icono" href="${pageContext.request.contextPath}/citas/admin/ver/${cita.id}">Ver</a>
          <a class="boton boton-primario boton-icono"   href="${pageContext.request.contextPath}/citas/admin/editar/${cita.id}">Editar</a>
          <a class="boton boton-peligro boton-icono"    href="${pageContext.request.contextPath}/citas/admin/eliminar/${cita.id}"
             onclick="return confirm('¿Eliminar esta cita?')">Eliminar</a>
        </td>
      </tr>
    </c:forEach>
    <c:if test="${empty citas}">
      <tr><td colspan="7" style="text-align:center;color:#999;padding:20px;">No hay citas registradas.</td></tr>
    </c:if>
    </tbody>
  </table>
</section>

<script>
/* ── Datos del servidor ───────────────────────────── */
const horariosDisponibles = ${empty horariosJson ? '[]' : horariosJson};
const todosPacientes = [
  <c:forEach var="p" items="${pacientes}" varStatus="st">
    {id:${p.id}, nombre:'${p.nombre} ${p.apellido}', dni:'${p.dni}'}${!st.last?',':''}
  </c:forEach>
];
const todosMedicos = [
  <c:forEach var="m" items="${medicos}" varStatus="st">
    {id:${m.id}, nombre:'${m.nombre} ${m.apellido}', espId:${m.especialidadId != null ? m.especialidadId : 0}}${!st.last?',':''}
  </c:forEach>
];

/* ── Estado del wizard ────────────────────────────── */
var pasoActual = 1;
var pacienteSelId   = '${citaFormulario.pacienteId}';
var pacienteSelNombre = '';
var medicoSelId     = '';
var medicoSelNombre = '';
var fechaSel        = '';
var horarioSelId    = '';
var horarioSelTexto = '';

/* ── Navegación ───────────────────────────────────── */
function irPaso(n) {
    [1,2,3].forEach(function(i){
        document.getElementById('paso'+i).style.display = i===n?'block':'none';
        var ind = document.getElementById('ind'+i);
        if (i===n) { ind.style.borderBottomColor='#0d6efd'; ind.style.color='#0d6efd'; ind.style.fontWeight='bold'; }
        else if (i<n) { ind.style.borderBottomColor='#198754'; ind.style.color='#198754'; ind.style.fontWeight='normal'; }
        else { ind.style.borderBottomColor='#dee2e6'; ind.style.color='#aaa'; ind.style.fontWeight='normal'; }
    });
    pasoActual = n;
    if (n===3) actualizarResumen();
}

function siguientePaso(actual) {
    if (actual===1) {
        if (!document.getElementById('h_pacienteId').value) { alert('Seleccione un paciente para continuar.'); return; }
        irPaso(2);
        filtrarMedicosPorEsp();
    } else if (actual===2) {
        if (!medicoSelId)   { alert('Seleccione un médico.'); return; }
        if (!fechaSel)      { alert('Seleccione una fecha.'); return; }
        if (!horarioSelId)  { alert('Seleccione un horario.'); return; }
        document.getElementById('h_horarioId').value = horarioSelId;
        irPaso(3);
    }
}

/* ── PASO 1: Pacientes ───────────────────────────── */
function filtrarPacientes() {
    var q = document.getElementById('buscarPaciente').value.toLowerCase();
    var lista = document.getElementById('listaPacientes');
    var filtrados = q ? todosPacientes.filter(function(p){ return (p.nombre+p.dni).toLowerCase().includes(q); }) : todosPacientes;
    lista.innerHTML = filtrados.slice(0,12).map(function(p){
        var sel = String(p.id) === String(pacienteSelId);
        return '<div onclick="elegirPaciente('+p.id+',\''+p.nombre+'\',\''+p.dni+'\')"'
              +' style="padding:10px 14px;border:2px solid '+(sel?'#0d6efd':'#dee2e6')+';'
              +'background:'+(sel?'#e7f1ff':'#fff')+';border-radius:8px;cursor:pointer;">'
              +'<strong>'+p.nombre+'</strong><br>'
              +'<small style="color:#666;">DNI: '+p.dni+'</small></div>';
    }).join('');
}
function elegirPaciente(id, nombre, dni) {
    pacienteSelId = id;
    pacienteSelNombre = nombre;
    document.getElementById('h_pacienteId').value = id;
    document.getElementById('lblPaciente').textContent = nombre;
    document.getElementById('lblPacienteDni').textContent = 'DNI: '+dni;
    document.getElementById('pacienteSeleccionado').style.display = 'block';
    filtrarPacientes();
}

/* ── PASO 2: Especialidad → Médico → Fecha → Horario ── */
function filtrarMedicosPorEsp() {
    var espId = document.getElementById('selEspecialidad').value;
    var sel   = document.getElementById('medicoSelect');
    Array.from(sel.options).forEach(function(opt){
        if (!opt.value) return;
        opt.style.display = (!espId || String(opt.dataset.esp)===String(espId)) ? '' : 'none';
    });
    sel.value = '';
    medicoSelId = ''; medicoSelNombre = '';
    document.getElementById('secFechas').style.display='none';
    document.getElementById('secHorarios').style.display='none';
}

document.getElementById('medicoSelect').addEventListener('change', function(){ filtrarFechas(); });
function filtrarFechas() {
    medicoSelId = document.getElementById('medicoSelect').value;
    medicoSelNombre = medicoSelId ? document.getElementById('medicoSelect').selectedOptions[0].text : '';
    fechaSel=''; horarioSelId=''; horarioSelTexto='';
    document.getElementById('h_fecha').value='';
    document.getElementById('h_horarioId').value='';

    var sec = document.getElementById('secFechas');
    if (!medicoSelId) { sec.style.display='none'; document.getElementById('secHorarios').style.display='none'; return; }

    var fechas = [...new Set(
        horariosDisponibles.filter(function(h){ return String(h.medicoId)===String(medicoSelId); })
            .map(function(h){ return h.fecha; }).filter(Boolean)
    )].sort();

    if (!fechas.length) {
        document.getElementById('gridFechas').innerHTML = '<p style="color:#999;font-size:.87rem;">Sin fechas disponibles para este médico.</p>';
        sec.style.display='block'; document.getElementById('secHorarios').style.display='none'; return;
    }
    document.getElementById('gridFechas').innerHTML = fechas.map(function(f){
        var hm = horariosDisponibles.find(function(h){ return String(h.medicoId)===String(medicoSelId) && h.fecha===f; });
        var dia = hm ? hm.diaSemana : '';
        return '<button type="button" id="btnF_'+f+'" onclick="elegirFecha(\''+f+'\',\''+dia+'\')"'
              +' style="padding:8px 14px;border:2px solid #dee2e6;border-radius:7px;background:#fff;cursor:pointer;font-size:.85rem;">'
              +f+(dia?'<br><small style="color:#666;">'+dia+'</small>':'')+'</button>';
    }).join('');
    sec.style.display='block';
    document.getElementById('secHorarios').style.display='none';
}

function elegirFecha(f, dia) {
    fechaSel = f;
    document.getElementById('h_fecha').value = f;
    document.querySelectorAll('[id^="btnF_"]').forEach(function(b){
        b.style.borderColor='#dee2e6'; b.style.background='#fff'; b.style.color='';
    });
    var btn = document.getElementById('btnF_'+f);
    if (btn) { btn.style.borderColor='#0d6efd'; btn.style.background='#e7f1ff'; btn.style.color='#0d6efd'; }
    renderHorarios();
}

function renderHorarios() {
    var hs = horariosDisponibles.filter(function(h){
        return String(h.medicoId)===String(medicoSelId) && h.fecha===fechaSel;
    });
    var sec = document.getElementById('secHorarios');
    if (!hs.length) { sec.style.display='none'; return; }
    document.getElementById('gridHorarios').innerHTML = hs.map(function(h){
        var sel = String(h.id)===String(horarioSelId);
        return '<button type="button" id="btnH_'+h.id+'" onclick="elegirHorario(\''+h.id+'\',\''+h.horaInicio+' – '+h.horaFin+'\')"'
              +' style="padding:10px 6px;border:2px solid '+(sel?'#0d6efd':'#dee2e6')+';'
              +'background:'+(sel?'#0d6efd':'#fff')+';color:'+(sel?'#fff':'#333')+';'
              +'border-radius:7px;cursor:pointer;font-weight:500;font-size:.88rem;text-align:center;">'
              +h.horaInicio+'</button>';
    }).join('');
    sec.style.display='block';
}

function elegirHorario(id, texto) {
    horarioSelId = id; horarioSelTexto = texto;
    document.querySelectorAll('[id^="btnH_"]').forEach(function(b){
        b.style.borderColor='#dee2e6'; b.style.background='#fff'; b.style.color='#333';
    });
    var btn = document.getElementById('btnH_'+id);
    if (btn) { btn.style.borderColor='#0d6efd'; btn.style.background='#0d6efd'; btn.style.color='#fff'; }
}

/* ── PASO 3: Resumen ─────────────────────────────── */
function actualizarResumen() {
    document.getElementById('rPaciente').textContent = pacienteSelNombre || '—';
    document.getElementById('rMedico').textContent   = medicoSelNombre   || '—';
    document.getElementById('rFecha').textContent    = fechaSel          || '—';
    document.getElementById('rHorario').textContent  = horarioSelTexto   || '—';
}

/* ── Init ────────────────────────────────────────── */
filtrarPacientes();
// Si venía editando, pre-cargar estado
<c:if test="${editando}">
(function(){
    var pid = '${citaFormulario.pacienteId}';
    var p   = todosPacientes.find(function(x){ return String(x.id)===String(pid); });
    if (p) elegirPaciente(p.id, p.nombre, p.dni);
    var mid = '${citaFormulario.medicoId}';
    document.getElementById('medicoSelect').value = mid;
    filtrarFechas();
})();
</c:if>
</script>
</main></body></html>
