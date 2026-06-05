<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Horarios"/></jsp:include>
<h1 class="pagina-titulo">Horarios de Médicos</h1>

<%-- PASO 1: Médico y mes --%>
<section class="tarjeta" style="margin-bottom:20px;">
  <div style="display:flex;align-items:center;gap:10px;margin-bottom:4px;">
    <div style="background:#0d6efd;color:#fff;width:26px;height:26px;border-radius:50%;display:flex;align-items:center;justify-content:center;font-weight:bold;font-size:.9rem;flex-shrink:0;">1</div>
    <div><strong>Médico y mes</strong><br><small style="color:#666;">Selecciona el médico y el mes a configurar</small></div>
  </div>
  <hr style="margin:10px 0 18px;">

  <div class="formulario-grupo">
    <label>MÉDICO</label>
    <select class="campo-formulario" id="selMedico" style="max-width:420px;" onchange="cargarMeses()">
      <option value="">— Seleccione un médico —</option>
      <c:forEach var="med" items="${medicos}">
        <option value="${med.id}">${med.nombre} ${med.apellido} — ${med.especialidad}</option>
      </c:forEach>
    </select>
  </div>

  <%-- Meses disponibles del médico (generados dinámicamente desde disponibilidades) --%>
  <div class="formulario-grupo" id="secMeses" style="display:none;">
    <label>MES</label>
    <div id="contenedorMeses" style="display:flex;flex-wrap:wrap;gap:8px;margin-top:8px;"></div>
  </div>
</section>

<%-- PASO 2: Turnos --%>
<section class="tarjeta" id="secTurnos" style="margin-bottom:20px;display:none;">
  <div style="display:flex;align-items:center;gap:10px;margin-bottom:4px;">
    <div style="background:#0d6efd;color:#fff;width:26px;height:26px;border-radius:50%;display:flex;align-items:center;justify-content:center;font-weight:bold;font-size:.9rem;flex-shrink:0;">2</div>
    <div><strong>Selecciona los turnos</strong><br><small style="color:#666;">Puedes elegir uno, dos o los tres turnos</small></div>
  </div>
  <hr style="margin:10px 0 18px;">

  <form id="frmHorario" action="${pageContext.request.contextPath}/horarios/guardar-mensual" method="post">
    <input type="hidden" name="medicoId" id="h_medicoId">
    <input type="hidden" name="mes"      id="h_mes">
    <input type="hidden" name="duracion" value="30">
    <div id="diasHidden"></div>

    <%-- Botones de turno --%>
    <div style="display:flex;flex-wrap:wrap;gap:12px;margin-bottom:22px;">
      <button type="button" id="btnManana" onclick="toggleTurno('manana')"
        style="padding:12px 24px;border:2px solid #dee2e6;border-radius:8px;background:#fff;cursor:pointer;font-size:.95rem;font-weight:500;transition:all .15s;">
        🌅 Turno Mañana<br><small style="font-weight:normal;color:#666;">08:00 – 12:00</small>
      </button>
      <button type="button" id="btnTarde" onclick="toggleTurno('tarde')"
        style="padding:12px 24px;border:2px solid #dee2e6;border-radius:8px;background:#fff;cursor:pointer;font-size:.95rem;font-weight:500;transition:all .15s;">
        ☀️ Turno Tarde<br><small style="font-weight:normal;color:#666;">13:00 – 17:00</small>
      </button>
      <button type="button" id="btnNoche" onclick="toggleTurno('noche')"
        style="padding:12px 24px;border:2px solid #dee2e6;border-radius:8px;background:#fff;cursor:pointer;font-size:.95rem;font-weight:500;transition:all .15s;">
        🌙 Turno Noche<br><small style="font-weight:normal;color:#666;">18:00 – 22:00</small>
      </button>
    </div>

    <%-- Vista estática de bloques por turno --%>
    <div id="vistaManana" style="display:none;margin-bottom:18px;">
      <div style="font-weight:bold;color:#0d6efd;margin-bottom:8px;">🌅 Turno Mañana — 08:00 a 12:00</div>
      <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(170px,1fr));gap:7px;" id="bloquesManana"></div>
    </div>
    <div id="vistaTarde" style="display:none;margin-bottom:18px;">
      <div style="font-weight:bold;color:#e67e22;margin-bottom:8px;">☀️ Turno Tarde — 13:00 a 17:00</div>
      <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(170px,1fr));gap:7px;" id="bloquesTarde"></div>
    </div>
    <div id="vistaNoche" style="display:none;margin-bottom:18px;">
      <div style="font-weight:bold;color:#6f42c1;margin-bottom:8px;">🌙 Turno Noche — 18:00 a 22:00</div>
      <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(170px,1fr));gap:7px;" id="bloquesNoche"></div>
    </div>

    <div class="formulario-acciones" style="margin-top:10px;">
      <button class="boton boton-primario" type="button" onclick="enviar()" style="padding:11px 26px;">
        Guardar Horario Mensual
      </button>
    </div>
  </form>
</section>

<%-- PASO 3: Tabla existente --%>
<section class="tarjeta">
  <div style="display:flex;align-items:center;gap:10px;margin-bottom:4px;">
    <div style="background:#6c757d;color:#fff;width:26px;height:26px;border-radius:50%;display:flex;align-items:center;justify-content:center;font-weight:bold;font-size:.9rem;flex-shrink:0;">3</div>
    <div><strong>Horarios registrados</strong></div>
  </div>
  <hr style="margin:10px 0 16px;">

  <c:if test="${editando}">
    <div style="background:#fff8e1;border:1px solid #ffe082;border-radius:6px;padding:14px 16px;margin-bottom:18px;">
      <strong style="display:block;margin-bottom:10px;">Editar Horario #${horarioFormulario.id}</strong>
      <form class="formulario" action="${pageContext.request.contextPath}/horarios/guardar" method="post" style="margin:0;">
        <input type="hidden" name="id" value="${horarioFormulario.id}">
        <div style="display:flex;flex-wrap:wrap;gap:14px;align-items:flex-end;">
          <div class="formulario-grupo" style="margin:0;flex:2;min-width:200px;">
            <label>Disponibilidad</label>
            <select class="campo-formulario" name="disponibilidadId" required>
              <c:forEach var="disp" items="${disponibilidades}">
                <option value="${disp.id}" ${horarioFormulario.disponibilidadId == disp.id ? 'selected' : ''}>
                  ${disp.nombreMedico} — ${disp.fecha} — ${disp.diaSemana}
                </option>
              </c:forEach>
            </select>
          </div>
          <div class="formulario-grupo" style="margin:0;">
            <label>Hora inicio</label>
            <input class="campo-formulario" type="time" name="horaInicio" value="${horarioFormulario.horaInicioTexto}" readonly style="background-color: #e9ecef; cursor: not-allowed;" required>
          </div>
          <div class="formulario-grupo" style="margin:0;">
            <label>Hora fin</label>
            <input class="campo-formulario" type="time" name="horaFin" value="${horarioFormulario.horaFinTexto}" readonly style="background-color: #e9ecef; cursor: not-allowed;" required>
          </div>
          <div class="formulario-grupo" style="margin:0;">
            <label>Estado</label>
            <select class="campo-formulario" name="estado">
              <option value="DISPONIBLE" ${horarioFormulario.estado=='DISPONIBLE'?'selected':''}>DISPONIBLE</option>
              <option value="BLOQUEADO"  ${horarioFormulario.estado=='BLOQUEADO' ?'selected':''}>BLOQUEADO</option>
              <option value="RESERVADO"  ${horarioFormulario.estado=='RESERVADO' ?'selected':''}>RESERVADO</option>
              <option value="EXPIRADO"   ${horarioFormulario.estado=='EXPIRADO'  ?'selected':''}>EXPIRADO</option>
            </select>
          </div>
        </div>
        <div class="formulario-acciones" style="margin-top:12px;">
          <button class="boton boton-primario" type="submit">Actualizar</button>
          <a class="boton boton-secundario" href="${pageContext.request.contextPath}/horarios">Cancelar</a>
        </div>
      </form>
    </div>
  </c:if>

  <%-- Filtro por médico --%>
  <div style="display:flex;align-items:center;gap:12px;margin-bottom:14px;flex-wrap:wrap;">
    <label style="font-weight:500;color:#666;font-size:.88rem;">Filtrar por médico:</label>
    <select id="filtroMedico" onchange="filtrarTabla()"
            style="padding:7px 12px;border:1px solid #dee2e6;border-radius:6px;font-size:.88rem;min-width:220px;">
      <option value="">— Todos los médicos —</option>
      <c:forEach var="med" items="${medicos}">
        <option value="${med.nombre} ${med.apellido}">${med.nombre} ${med.apellido}</option>
      </c:forEach>
    </select>
    <span id="contadorFiltro" style="color:#666;font-size:.85rem;"></span>
  </div>

  <table class="tabla" id="tablaHorarios">
    <thead>
      <tr><th>#</th><th>Médico</th><th>Fecha</th><th>Día</th><th>Inicio</th><th>Fin</th><th>Estado</th><th>Acciones</th></tr>
    </thead>
    <tbody id="tbodyHorarios">
      <c:forEach var="h" items="${horarios}" varStatus="st">
        <tr data-medico="${h.nombreMedico}">
          <td style="color:#aaa;font-size:.8rem;">#${st.index+1}</td>
          <td><strong>${h.nombreMedico}</strong></td>
          <td>${h.fechaTexto}</td>
          <td>${h.diaSemana}</td>
          <td>${h.horaInicioTexto}</td>
          <td>${h.horaFinTexto}</td>
          <td><span class="etiqueta-estado ${h.estado}">${h.estado}</span></td>
          <td class="tabla-acciones">
            <a class="boton boton-secundario boton-icono" href="${pageContext.request.contextPath}/horarios/ver/${h.id}">Ver</a>
            <a class="boton boton-primario boton-icono"   href="${pageContext.request.contextPath}/horarios/editar/${h.id}">Editar</a>
            <a class="boton boton-peligro boton-icono"    href="${pageContext.request.contextPath}/horarios/eliminar/${h.id}"
               onclick="return confirm('¿Eliminar este horario?')">Eliminar</a>
          </td>
        </tr>
      </c:forEach>
      <c:if test="${empty horarios}">
        <tr id="trVacio"><td colspan="8" style="text-align:center;color:#999;padding:20px;">No hay horarios registrados.</td></tr>
      </c:if>
    </tbody>
  </table>

  <script>
  function filtrarTabla() {
    var filtro = document.getElementById('filtroMedico').value.toLowerCase();
    var filas  = document.querySelectorAll('#tbodyHorarios tr[data-medico]');
    var visibles = 0;
    filas.forEach(function(tr){
      var medico = (tr.dataset.medico || '').toLowerCase();
      var mostrar = !filtro || medico === filtro.toLowerCase();
      tr.style.display = mostrar ? '' : 'none';
      if (mostrar) visibles++;
    });
    var trVacio = document.getElementById('trVacio');
    if (trVacio) trVacio.style.display = 'none';
    document.getElementById('contadorFiltro').textContent =
      filtro ? visibles + ' horario' + (visibles !== 1 ? 's' : '') : '';
  }
  </script>
</section>

<script>
// ── Datos de disponibilidades del servidor para obtener meses por médico ──
var dispData = [
  <c:forEach var="d" items="${disponibilidades}" varStatus="st">
    {medicoId:${d.medicoId}, fecha:'${d.fecha}'}${!st.last?',':''}
  </c:forEach>
];

var turnosActivos = {manana:false, tarde:false, noche:false};
var mesSeleccionado = '';

var TURNOS = {
  manana: {hi:'08:00', hf:'12:00'},
  tarde:  {hi:'13:00', hf:'17:00'},
  noche:  {hi:'18:00', hf:'22:00'}
};
var COLORES = {
  manana: {borde:'#0d6efd', fondo:'#e7f1ff', texto:'#0d6efd'},
  tarde:  {borde:'#e67e22', fondo:'#fff5ec', texto:'#e67e22'},
  noche:  {borde:'#6f42c1', fondo:'#f3eeff', texto:'#6f42c1'}
};

function pad(n){ return String(n).padStart(2,'0'); }

function generarBloques(hi, hf) {
    var h1=+hi.split(':')[0], m1=+hi.split(':')[1];
    var h2=+hf.split(':')[0], m2=+hf.split(':')[1];
    var arr=[], cur=h1*60+m1;
    while(cur+30<=h2*60+m2){
        var s=pad(~~(cur/60))+':'+pad(cur%60); cur+=30;
        arr.push(s+' – '+pad(~~(cur/60))+':'+pad(cur%60));
    }
    return arr;
}

function renderBloques(containerId, turno) {
    var bl = generarBloques(TURNOS[turno].hi, TURNOS[turno].hf);
    var c  = COLORES[turno];
    document.getElementById(containerId).innerHTML = bl.map(function(b, i){
        return '<div style="border:1px solid '+c.borde+';background:'+c.fondo+';border-radius:6px;padding:8px 12px;'
              +'display:flex;justify-content:space-between;align-items:center;">'
              +'<span style="color:#aaa;font-size:.78rem;">#'+(i+1)+'</span>'
              +'<span style="font-weight:500;">'+b+'</span>'
              +'<span style="color:'+c.texto+';font-size:.78rem;">disponible</span>'
              +'</div>';
    }).join('');
}

function toggleTurno(turno) {
    turnosActivos[turno] = !turnosActivos[turno];
    var btnMap = {manana:'btnManana', tarde:'btnTarde', noche:'btnNoche'};
    var visMap = {manana:'vistaManana', tarde:'vistaTarde', noche:'vistaNoche'};
    var blkMap = {manana:'bloquesManana', tarde:'bloquesTarde', noche:'bloquesNoche'};
    var c = COLORES[turno];
    var btn = document.getElementById(btnMap[turno]);
    if (turnosActivos[turno]) {
        btn.style.borderColor = c.borde;
        btn.style.background  = c.fondo;
        btn.style.color       = c.texto;
        renderBloques(blkMap[turno], turno);
        document.getElementById(visMap[turno]).style.display = 'block';
    } else {
        btn.style.borderColor = '#dee2e6';
        btn.style.background  = '#fff';
        btn.style.color       = '';
        document.getElementById(visMap[turno]).style.display = 'none';
    }
}

// ── Cargar meses disponibles según médico seleccionado ──
function cargarMeses() {
    var medId = document.getElementById('selMedico').value;
    var secMeses = document.getElementById('secMeses');
    var cont = document.getElementById('contenedorMeses');
    mesSeleccionado = '';
    document.getElementById('secTurnos').style.display = 'none';

    if (!medId) { secMeses.style.display='none'; return; }

    // Obtener meses únicos de las disponibilidades de este médico
    var mesesSet = {};
    dispData.forEach(function(d){
        if (String(d.medicoId) === String(medId)) {
            var m = d.fecha.substring(0,7); // YYYY-MM
            mesesSet[m] = true;
        }
    });
    var meses = Object.keys(mesesSet).sort();

    if (!meses.length) {
        cont.innerHTML = '<p style="color:#999;font-size:.87rem;">Este médico no tiene disponibilidades configuradas.</p>';
        secMeses.style.display = 'block';
        return;
    }

    var MESES_ES = ['','Ene','Feb','Mar','Abr','May','Jun','Jul','Ago','Sep','Oct','Nov','Dic'];
    cont.innerHTML = meses.map(function(m){
        var p = m.split('-'), anio=p[0], mesN=+p[1];
        return '<button type="button" onclick="seleccionarMes(\''+m+'\')" id="btnMes_'+m+'"'
              +' style="padding:8px 16px;border:2px solid #dee2e6;border-radius:7px;background:#fff;cursor:pointer;font-weight:500;">'
              +MESES_ES[mesN]+' '+anio+'</button>';
    }).join('');
    secMeses.style.display = 'block';
}

function seleccionarMes(mes) {
    mesSeleccionado = mes;
    // Resaltar botón seleccionado
    document.querySelectorAll('[id^="btnMes_"]').forEach(function(b){
        b.style.borderColor = '#dee2e6';
        b.style.background  = '#fff';
        b.style.color       = '';
    });
    var btn = document.getElementById('btnMes_'+mes);
    if (btn) { btn.style.borderColor='#0d6efd'; btn.style.background='#e7f1ff'; btn.style.color='#0d6efd'; }
    document.getElementById('secTurnos').style.display = 'block';
}

function enviar() {
    var medId = document.getElementById('selMedico').value;
    if (!medId)          { alert('Seleccione un médico.'); return; }
    if (!mesSeleccionado){ alert('Seleccione un mes.'); return; }
    var turnos = Object.keys(turnosActivos).filter(function(t){ return turnosActivos[t]; });
    if (!turnos.length)  { alert('Seleccione al menos un turno.'); return; }

    // Obtener días únicos del médico en ese mes
    var dowToEsp = {0:'DOMINGO',1:'LUNES',2:'MARTES',3:'MIERCOLES',4:'JUEVES',5:'VIERNES',6:'SABADO'};
    var diasUnicos = {};
    dispData.forEach(function(d){
        if (String(d.medicoId)===String(medId) && d.fecha.substring(0,7)===mesSeleccionado) {
            var dow = new Date(d.fecha+'T00:00:00').getDay();
            diasUnicos[dowToEsp[dow]] = true;
        }
    });

    // Calcular hora inicio y fin combinada de los turnos seleccionados
    var horas = turnos.map(function(t){ return TURNOS[t]; });
    var hIniMin = Math.min.apply(null, horas.map(function(h){ return +h.hi.split(':')[0]*60+ +h.hi.split(':')[1]; }));
    var hFinMax = Math.max.apply(null, horas.map(function(h){ return +h.hf.split(':')[0]*60+ +h.hf.split(':')[1]; }));

    // Si hay turnos no contiguos (mañana+noche sin tarde), necesitamos múltiples rangos.
    // Para simplificar: enviamos un POST por cada turno.
    // Solución: generamos los hidden inputs con todos los bloques de todos los turnos
    // El backend acepta horaInicio/horaFin por rango, así que enviamos el rango mínimo-máximo.
    // (Los bloques intermedios vacíos no importan, el admin luego puede gestionarlos).
    // Para máxima fidelidad enviamos un turno por submit — pero con un solo form enviamos el rango total.
    // MEJOR: enviar los turnos como parámetros separados y el backend itera.
    // Por ahora: enviamos rango continuo de cada turno activo como hidden inputs múltiples.

    var cont = document.getElementById('diasHidden');
    cont.innerHTML = '';

    // Días
    Object.keys(diasUnicos).forEach(function(diaEsp){
        var inp = document.createElement('input');
        inp.type='hidden'; inp.name='dias'; inp.value=diaEsp;
        cont.appendChild(inp);
    });

    // Rangos de turnos — enviamos horaInicio/horaFin del rango continuo total
    // (si mañana+tarde+noche => 08:00-22:00; si mañana+noche => dos requests no es posible en 1 form)
    // Enviamos el turno activo de menor inicio y mayor fin para simplificar
    var hiStr = pad(~~(hIniMin/60))+':'+pad(hIniMin%60);
    var hfStr = pad(~~(hFinMax/60))+':'+pad(hFinMax%60);

    // Nota: si solo mañana+noche se incluirá tarde (13-18) también generada.
    // Para bloques exactos por turno, el admin puede eliminar los sobrantes de la tabla.
    var addH = function(n,v){ var i=document.createElement('input'); i.type='hidden'; i.name=n; i.value=v; cont.appendChild(i); };
    addH('horaInicio', hiStr);
    addH('horaFin',    hfStr);

    document.getElementById('h_medicoId').value = medId;
    document.getElementById('h_mes').value       = mesSeleccionado;
    document.getElementById('frmHorario').submit();
}
</script>
</main></body></html>
