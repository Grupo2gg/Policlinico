<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Disponibilidades"/></jsp:include>
<h1 class="pagina-titulo">Disponibilidades Médicas</h1>

<%-- PASO 1: Médico y mes --%>
<section class="tarjeta" style="margin-bottom:20px;">
  <div style="display:flex;align-items:center;gap:10px;margin-bottom:4px;">
    <div style="background:#0d6efd;color:#fff;width:26px;height:26px;border-radius:50%;display:flex;align-items:center;justify-content:center;font-weight:bold;font-size:.9rem;flex-shrink:0;">1</div>
    <div><strong>Médico y mes</strong><br><small style="color:#666;">Solo se puede configurar el siguiente mes</small></div>
  </div>
  <hr style="margin:10px 0 18px;">

  <div class="formulario-grupo">
    <label>MÉDICO</label>
    <select class="campo-formulario" id="selMedico" style="max-width:420px;">
      <option value="">— Seleccione un médico —</option>
      <c:forEach var="med" items="${medicos}">
        <option value="${med.id}">${med.nombre} ${med.apellido}</option>
      </c:forEach>
    </select>
  </div>

  <div class="formulario-grupo">
    <label>MES A CONFIGURAR</label>
    <div style="display:flex;align-items:center;gap:10px;max-width:420px;">
      <input class="campo-formulario" type="month" id="inputMes" style="flex:1;background-color:#f1f3f5;color:#495057;cursor:not-allowed;" readonly onchange="renderCalendario();">
    </div>
    <div id="avisoMes" style="color:#856404;background:#fff3cd;border:1px solid #ffc107;border-radius:5px;padding:7px 12px;margin-top:8px;font-size:.84rem;">
      ⚠ Solo se permite configurar el mes actual. No se puede editar meses pasados ni futuros.
    </div>
  </div>

  <div class="formulario-grupo">
    <label>DURACIÓN DE CADA BLOQUE</label>
    <div style="padding:9px 13px;background:#f8f9fa;border:1px solid #dee2e6;border-radius:6px;max-width:180px;font-weight:500;color:#495057;">
      30 minutos
    </div>
  </div>
</section>

<%-- PASO 2: Cuadrícula del mes + botón guardar --%>
<section class="tarjeta" style="margin-bottom:20px;">
  <div style="display:flex;align-items:center;gap:10px;margin-bottom:4px;">
    <div style="background:#0d6efd;color:#fff;width:26px;height:26px;border-radius:50%;display:flex;align-items:center;justify-content:center;font-weight:bold;font-size:.9rem;flex-shrink:0;">2</div>
    <div><strong>Días de semana</strong><br><small style="color:#666;">Selecciona los días del mes para generar disponibilidad</small></div>
  </div>
  <hr style="margin:10px 0 18px;">

  <form id="frmDisp" action="${pageContext.request.contextPath}/disponibilidades/guardar-mensual" method="post">
    <input type="hidden" name="medicoId" id="h_medicoId">
    <input type="hidden" name="mes"      id="h_mes">
    <input type="hidden" name="duracion" value="30">
    <input type="hidden" name="horaInicio" value="08:00">
    <input type="hidden" name="horaFin"    value="20:00">
    <div id="diasHidden"></div>

    <div class="formulario-grupo">
      <label>DÍAS DE LA SEMANA</label>
      <div id="calGrid" style="margin-top:10px;"></div>
      <small id="lblDias" style="color:#666;margin-top:6px;display:block;">Seleccionados: ninguno</small>
    </div>

    <div class="formulario-acciones" style="margin-top:22px;">
      <button class="boton boton-primario" type="button" onclick="enviar()" style="padding:11px 26px;">
        Guardar Disponibilidad Mensual
      </button>
    </div>
  </form>
</section>

<%-- PASO 3: Vista previa --%>
<section class="tarjeta">
  <div style="display:flex;align-items:center;gap:10px;margin-bottom:4px;">
    <div style="background:#6c757d;color:#fff;width:26px;height:26px;border-radius:50%;display:flex;align-items:center;justify-content:center;font-weight:bold;font-size:.9rem;flex-shrink:0;">3</div>
    <div><strong>Vista previa — Disponibilidades generadas</strong></div>
  </div>
  <hr style="margin:10px 0 16px;">

  <strong>DISPONIBILIDADES · ${fn:length(disponibilidades)} FECHAS</strong>
  <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(190px,1fr));gap:8px;margin-top:12px;">
    <c:forEach var="d" items="${disponibilidades}">
      <div style="background:#f8f9fa;border:1px solid #dee2e6;border-radius:6px;padding:9px 13px;display:flex;justify-content:space-between;align-items:center;gap:8px;">
        <div>
          <span style="display:inline-block;width:9px;height:9px;border-radius:50%;background:#198754;margin-right:5px;"></span>
          <strong>${d.fecha}</strong>
        </div>
        <span style="color:#666;font-size:.83rem;">${d.diaSemana}</span>
        <a class="boton boton-peligro boton-icono" style="font-size:.75rem;padding:2px 7px;flex-shrink:0;"
           href="${pageContext.request.contextPath}/disponibilidades/eliminar/${d.id}"
           onclick="return confirm('¿Eliminar?')">✕</a>
      </div>
    </c:forEach>
    <c:if test="${empty disponibilidades}">
      <p style="color:#999;grid-column:1/-1;padding:10px 0;">No hay disponibilidades registradas.</p>
    </c:if>
  </div>
</section>

<script>
var diasSeleccionados = {};
var DIAS_ES = ['Dom','Lun','Mar','Mié','Jue','Vie','Sáb'];

function pad(n){ return String(n).padStart(2,'0'); }

function renderCalendario() {
    var mesStr = document.getElementById('inputMes').value;
    var grid   = document.getElementById('calGrid');
    if (!mesStr) {
        grid.innerHTML = '<p style="color:#999;font-size:.88rem;">Seleccione un mes para ver el calendario.</p>';
        actualizarDiasLabel(); return;
    }
    var p = mesStr.split('-'), anio = parseInt(p[0],10), mes = parseInt(p[1],10);
    var primero = new Date(anio, mes-1, 1);
    var ultimo  = new Date(anio, mes, 0).getDate();
    var MESES = ['','Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];

    var html = '<div style="font-weight:bold;margin-bottom:8px;color:#0d6efd;">'+MESES[mes]+' '+anio+'</div>';
    html += '<div id="calCeldas" style="display:grid;grid-template-columns:repeat(7,minmax(36px,1fr));gap:4px;max-width:340px;">';
    DIAS_ES.forEach(function(d){ html += '<div style="text-align:center;font-size:.75rem;font-weight:bold;color:#666;padding:4px 0;">'+d+'</div>'; });
    var offset = primero.getDay();
    for (var i=0; i<offset; i++) html += '<div></div>';
    var hoyCal = new Date(); hoyCal.setHours(0,0,0,0);
    for (var d=1; d<=ultimo; d++) {
        var dow = new Date(anio, mes-1, d).getDay();
        var key = anio+'-'+pad(mes)+'-'+pad(d);
        var fechaDia = new Date(anio, mes-1, d); fechaDia.setHours(0,0,0,0);
        var esPasado = fechaDia <= hoyCal; // hoy y días anteriores no se pueden configurar
        var sel = !!diasSeleccionados[key];
        if (esPasado) {
            html += '<div style="text-align:center;padding:6px 2px;border-radius:6px;font-size:.85rem;'
                  +'border:2px solid #f1f3f5;background:#f1f3f5;color:#ccc;cursor:not-allowed;">'+d+'</div>';
        } else {
            html += '<div data-key="'+key+'" data-dow="'+dow+'"'
                  +' style="text-align:center;padding:6px 2px;border-radius:6px;font-size:.85rem;cursor:pointer;user-select:none;'
                  +'border:2px solid '+(sel?'#0d6efd':'#dee2e6')+';'
                  +'background:'+(sel?'#0d6efd':'#fff')+';'
                  +'color:'+(sel?'#fff':'#333')+';">'+d+'</div>';
        }
    }
    html += '</div>';
    html += '<div style="display:flex;flex-wrap:wrap;gap:7px;margin-top:12px;">'
          + '<button type="button" class="boton boton-secundario" data-quick="LMV"  style="font-size:.8rem;padding:4px 10px;">L·M·V</button>'
          + '<button type="button" class="boton boton-secundario" data-quick="MJ"   style="font-size:.8rem;padding:4px 10px;">M·J</button>'
          + '<button type="button" class="boton boton-secundario" data-quick="LAB"  style="font-size:.8rem;padding:4px 10px;">Lun–Vie</button>'
          + '<button type="button" class="boton boton-secundario" data-quick="TODO" style="font-size:.8rem;padding:4px 10px;">Todo</button>'
          + '<button type="button" class="boton boton-secundario" data-quick="NADA" style="font-size:.8rem;padding:4px 10px;">Limpiar</button>'
          + '</div>';
    grid.innerHTML = html;

    // Listener para celdas (toggle)
    var celdas = document.getElementById('calCeldas');
    if (celdas) {
        celdas.addEventListener('click', function(e) {
            var cel = e.target.closest('[data-key]');
            if (!cel) return;
            var key = cel.getAttribute('data-key');
            var dow = parseInt(cel.getAttribute('data-dow'), 10);
            if (diasSeleccionados.hasOwnProperty(key)) {
                delete diasSeleccionados[key];
                cel.style.border     = '2px solid #dee2e6';
                cel.style.background = '#fff';
                cel.style.color      = '#333';
            } else {
                diasSeleccionados[key] = dow;
                cel.style.border     = '2px solid #0d6efd';
                cel.style.background = '#0d6efd';
                cel.style.color      = '#fff';
            }
            actualizarDiasLabel();
        });
    }

    // Listener para botones rápidos
    grid.querySelectorAll('[data-quick]').forEach(function(btn){
        btn.addEventListener('click', function(){ selQuick(btn.getAttribute('data-quick')); });
    });

    actualizarDiasLabel();
}

function selQuick(tipo) {
    var mesStr = document.getElementById('inputMes').value;
    if (!mesStr) return;
    var p = mesStr.split('-'), anio = parseInt(p[0],10), mes = parseInt(p[1],10);
    var ultimo = new Date(anio, mes, 0).getDate();
    var hoyQ = new Date(); hoyQ.setHours(0,0,0,0);
    diasSeleccionados = {};
    for (var d=1; d<=ultimo; d++) {
        var fechaDia = new Date(anio, mes-1, d); fechaDia.setHours(0,0,0,0);
        if (fechaDia <= hoyQ) continue; // saltar hoy y días pasados
        var dow = new Date(anio, mes-1, d).getDay();
        var ok  = tipo==='TODO'
               || (tipo==='LMV' && (dow===1||dow===3||dow===5))
               || (tipo==='MJ'  && (dow===2||dow===4))
               || (tipo==='LAB' && dow>=1 && dow<=5);
        if (ok) diasSeleccionados[anio+'-'+pad(mes)+'-'+pad(d)] = dow;
    }
    renderCalendario();
}

function actualizarDiasLabel() {
    var cnt = Object.keys(diasSeleccionados).length;
    document.getElementById('lblDias').textContent = cnt ? 'Seleccionados: '+cnt+' días' : 'Seleccionados: ninguno';
}

function fijarMesActual() {
    var hoy = new Date(), anio = hoy.getFullYear(), mes = hoy.getMonth()+1;
    document.getElementById('inputMes').value = anio+'-'+pad(mes);
    renderCalendario();
}

function mostrarAviso(){ var a=document.getElementById('avisoMes'); if(a) a.style.display='block'; }

function enviar() {
    if (!document.getElementById('selMedico').value) { alert('Seleccione un médico.'); return; }
    if (!document.getElementById('inputMes').value)  { alert('Seleccione el mes.'); return; }
    if (!Object.keys(diasSeleccionados).length) { alert('Seleccione al menos un día.'); return; }
    var dowToEsp = {0:'DOMINGO',1:'LUNES',2:'MARTES',3:'MIERCOLES',4:'JUEVES',5:'VIERNES',6:'SABADO'};
    var diasUnicos = {};
    Object.keys(diasSeleccionados).forEach(function(k){ diasUnicos[dowToEsp[diasSeleccionados[k]]] = true; });
    var cont = document.getElementById('diasHidden');
    cont.innerHTML = '';
    Object.keys(diasUnicos).forEach(function(diaEsp){
        var inp = document.createElement('input');
        inp.type='hidden'; inp.name='dias'; inp.value=diaEsp;
        cont.appendChild(inp);
    });
    document.getElementById('h_medicoId').value = document.getElementById('selMedico').value;
    document.getElementById('h_mes').value       = document.getElementById('inputMes').value;
    document.getElementById('frmDisp').submit();
}

// Inicializar
fijarMesActual();
</script>
</main></body></html>
