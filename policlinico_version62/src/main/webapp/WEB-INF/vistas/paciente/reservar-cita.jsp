<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Reservar cita"/></jsp:include>
<h1 class="pagina-titulo">${empty cita.id ? 'Reservar Nueva Cita' : 'Actualizar Cita'}</h1>

<c:if test="${not empty error or not empty param.error}">
  <p style="color:#c0392b;background:#fdecea;border:1px solid #f5c6cb;border-radius:6px;padding:10px 14px;margin-bottom:16px;">
    ${not empty error ? error : 'Revise que haya elegido un horario y vuelva a intentar.'}
  </p>
</c:if>

<section class="tarjeta">

  <%-- Indicador de pasos --%>
  <div style="display:flex;gap:0;margin-bottom:24px;">
    <div id="ind1" style="flex:1;text-align:center;padding:10px 6px;border-bottom:3px solid #0d6efd;font-weight:bold;font-size:.88rem;color:#0d6efd;">
      1 · Tus Datos
    </div>
    <div id="ind2" style="flex:1;text-align:center;padding:10px 6px;border-bottom:3px solid #dee2e6;font-weight:normal;font-size:.88rem;color:#aaa;">
      2 · Médico y Horario
    </div>
    <div id="ind3" style="flex:1;text-align:center;padding:10px 6px;border-bottom:3px solid #dee2e6;font-weight:normal;font-size:.88rem;color:#aaa;">
      3 · Motivo y Confirmar
    </div>
  </div>

  <form id="frmCita" action="${pageContext.request.contextPath}${empty cita.id ? '/citas/guardar' : '/citas/actualizar'}" method="post">
    <c:if test="${not empty cita.id}">
      <input type="hidden" name="id" value="${cita.id}">
    </c:if>
    <input type="hidden" name="horarioId" id="h_horarioId">

    <%-- ── PASO 1: Datos del paciente (estático) ────────────────── --%>
    <div id="paso1">
      <div class="formulario-grupo">
        <label>PACIENTE</label>
        <div style="display:flex;align-items:center;gap:12px;padding:12px 16px;background:#e7f1ff;border:1px solid #b6d4fe;border-radius:8px;max-width:440px;">
          <div style="width:38px;height:38px;border-radius:50%;background:#0d6efd;color:#fff;display:flex;align-items:center;justify-content:center;font-weight:bold;font-size:1rem;flex-shrink:0;">
            ${sessionScope.nombreUsuario.substring(0,1).toUpperCase()}
          </div>
          <div>
            <strong>${sessionScope.nombreUsuario}</strong><br>
            <small style="color:#666;">${sessionScope.email}</small>
          </div>
        </div>
      </div>
      <div style="margin-top:18px;display:flex;justify-content:flex-end;">
        <button type="button" class="boton boton-primario" onclick="irPaso(2)" style="padding:10px 28px;">
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
            <option value="">— Seleccione —</option>
            <c:forEach var="med" items="${medicos}">
              <option value="${med.id}" data-esp="${med.especialidadId}">${med.nombre} ${med.apellido}</option>
            </c:forEach>
          </select>
        </div>
      </div>

      <div class="formulario-grupo" id="secFechas" style="display:none;">
        <label>FECHA</label>
        <div id="gridFechas" style="display:flex;flex-wrap:wrap;gap:8px;margin-top:8px;"></div>
        <input type="hidden" id="h_fecha">
      </div>

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
        <button type="button" class="boton boton-primario" onclick="siguientePaso2()" style="padding:10px 28px;">Continuar →</button>
      </div>
    </div>

    <%-- ── PASO 3: Motivo y Confirmar ──────────────────────────────── --%>
    <div id="paso3" style="display:none;">
      <div class="formulario-grupo">
        <label>MOTIVO DE CONSULTA <span style="color:#dc3545;">*</span></label>
        <textarea class="campo-formulario" name="motivo" rows="4" required
                  placeholder="Describe el motivo de tu consulta…">${cita.motivo}</textarea>
      </div>

      <%-- Resumen --%>
      <div style="background:#f0f4ff;border:1px solid #c7d5f8;border-radius:8px;padding:14px 18px;margin-bottom:18px;">
        <strong style="display:block;margin-bottom:8px;">Resumen de tu cita</strong>
        <div style="display:grid;grid-template-columns:1fr 1fr;gap:6px;font-size:.9rem;">
          <div><span style="color:#666;">Médico:</span> <strong id="rMedico">—</strong></div>
          <div><span style="color:#666;">Fecha:</span>  <strong id="rFecha">—</strong></div>
          <div><span style="color:#666;">Horario:</span><strong id="rHorario">—</strong></div>
          <div><span style="color:#666;">Estado:</span> <strong style="color:#856404;">⏳ PENDIENTE</strong></div>
        </div>
      </div>

      <div style="display:flex;justify-content:space-between;">
        <button type="button" class="boton boton-secundario" onclick="irPaso(2)">← Atrás</button>
        <button type="submit" class="boton boton-primario" style="padding:11px 28px;">
          ✅ ${empty cita.id ? 'Reservar Cita' : 'Actualizar Cita'}
        </button>
      </div>
    </div>
  </form>
</section>

<script>
const horariosDisponibles = ${empty horariosJson ? '[]' : horariosJson};
var medicoSelId='', medicoSelNombre='', fechaSel='', horarioSelId='', horarioSelTexto='';

function irPaso(n) {
    [1,2,3].forEach(function(i){
        document.getElementById('paso'+i).style.display = i===n?'block':'none';
        var ind = document.getElementById('ind'+i);
        if (i===n) { ind.style.borderBottomColor='#0d6efd'; ind.style.color='#0d6efd'; ind.style.fontWeight='bold'; }
        else if (i<n){ ind.style.borderBottomColor='#198754'; ind.style.color='#198754'; ind.style.fontWeight='normal'; }
        else { ind.style.borderBottomColor='#dee2e6'; ind.style.color='#aaa'; ind.style.fontWeight='normal'; }
    });
    if (n===3) actualizarResumen();
}

function filtrarMedicosPorEsp() {
    var espId = document.getElementById('selEspecialidad').value;
    Array.from(document.getElementById('medicoSelect').options).forEach(function(opt){
        if (!opt.value) return;
        opt.style.display = (!espId || String(opt.dataset.esp)===String(espId)) ? '' : 'none';
    });
    document.getElementById('medicoSelect').value='';
    medicoSelId=''; medicoSelNombre=''; fechaSel=''; horarioSelId='';
    document.getElementById('secFechas').style.display='none';
    document.getElementById('secHorarios').style.display='none';
}

document.getElementById('medicoSelect').addEventListener('change', filtrarFechas);
function filtrarFechas() {
    medicoSelId = document.getElementById('medicoSelect').value;
    medicoSelNombre = medicoSelId ? document.getElementById('medicoSelect').selectedOptions[0].text : '';
    fechaSel=''; horarioSelId='';
    var sec = document.getElementById('secFechas');
    if (!medicoSelId) { sec.style.display='none'; document.getElementById('secHorarios').style.display='none'; return; }
    var fechas=[...new Set(horariosDisponibles.filter(function(h){return String(h.medicoId)===String(medicoSelId);}).map(function(h){return h.fecha;}).filter(Boolean))].sort();
    if (!fechas.length) {
        document.getElementById('gridFechas').innerHTML='<p style="color:#999;font-size:.87rem;">Sin fechas disponibles.</p>';
        sec.style.display='block'; document.getElementById('secHorarios').style.display='none'; return;
    }
    document.getElementById('gridFechas').innerHTML = fechas.map(function(f){
        var hm=horariosDisponibles.find(function(h){return String(h.medicoId)===String(medicoSelId)&&h.fecha===f;});
        var dia=hm?hm.diaSemana:'';
        return '<button type="button" id="btnF_'+f+'" onclick="elegirFecha(\''+f+'\',\''+dia+'\')"'
              +' style="padding:8px 14px;border:2px solid #dee2e6;border-radius:7px;background:#fff;cursor:pointer;font-size:.85rem;">'
              +f+(dia?'<br><small style="color:#666;">'+dia+'</small>':'')+'</button>';
    }).join('');
    sec.style.display='block'; document.getElementById('secHorarios').style.display='none';
}
function elegirFecha(f, dia) {
    fechaSel=f; document.getElementById('h_fecha').value=f;
    document.querySelectorAll('[id^="btnF_"]').forEach(function(b){ b.style.borderColor='#dee2e6'; b.style.background='#fff'; b.style.color=''; });
    var btn=document.getElementById('btnF_'+f);
    if (btn){ btn.style.borderColor='#0d6efd'; btn.style.background='#e7f1ff'; btn.style.color='#0d6efd'; }
    var hs=horariosDisponibles.filter(function(h){return String(h.medicoId)===String(medicoSelId)&&h.fecha===fechaSel;});
    var sec=document.getElementById('secHorarios');
    if (!hs.length){ sec.style.display='none'; return; }
    document.getElementById('gridHorarios').innerHTML=hs.map(function(h){
        return '<button type="button" id="btnH_'+h.id+'" onclick="elegirHorario(\''+h.id+'\',\''+h.horaInicio+' – '+h.horaFin+'\')"'
              +' style="padding:10px 6px;border:2px solid #dee2e6;border-radius:7px;background:#fff;color:#333;cursor:pointer;font-weight:500;font-size:.88rem;text-align:center;">'
              +h.horaInicio+'</button>';
    }).join('');
    sec.style.display='block';
}
function elegirHorario(id, texto) {
    horarioSelId=id; horarioSelTexto=texto;
    document.querySelectorAll('[id^="btnH_"]').forEach(function(b){ b.style.borderColor='#dee2e6'; b.style.background='#fff'; b.style.color='#333'; });
    var btn=document.getElementById('btnH_'+id);
    if (btn){ btn.style.borderColor='#0d6efd'; btn.style.background='#0d6efd'; btn.style.color='#fff'; }
}
function siguientePaso2() {
    if (!medicoSelId)  { alert('Seleccione un médico.'); return; }
    if (!fechaSel)     { alert('Seleccione una fecha.'); return; }
    if (!horarioSelId) { alert('Seleccione un horario.'); return; }
    document.getElementById('h_horarioId').value = horarioSelId;
    irPaso(3);
}
function actualizarResumen() {
    document.getElementById('rMedico').textContent  = medicoSelNombre  || '—';
    document.getElementById('rFecha').textContent   = fechaSel         || '—';
    document.getElementById('rHorario').textContent = horarioSelTexto  || '—';
}
</script>
</main></body></html>
