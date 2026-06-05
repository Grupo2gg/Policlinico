<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Mi Disponibilidad"/></jsp:include>
<h1 class="pagina-titulo">Mi Disponibilidad</h1>

<%-- ── PASO 1: Médico y mes ─────────────────────────────────────────────── --%>
<section class="tarjeta" style="margin-bottom:20px;">
  <div style="display:flex;align-items:center;gap:10px;margin-bottom:4px;">
    <div style="background:#0d6efd;color:#fff;width:26px;height:26px;border-radius:50%;display:flex;align-items:center;justify-content:center;font-weight:bold;font-size:.9rem;flex-shrink:0;">1</div>
    <div><strong>Médico y mes</strong><br><small style="color:#666;">Solo se puede configurar el siguiente mes</small></div>
  </div>
  <hr style="margin:10px 0 18px;">

  <%-- Nombre estático desde sesión --%>
  <div class="formulario-grupo">
    <label>MÉDICO</label>
    <div style="display:flex;align-items:center;gap:10px;padding:9px 13px;background:#f8f9fa;border:1px solid #dee2e6;border-radius:6px;max-width:380px;">
      <div style="width:30px;height:30px;border-radius:50%;background:#0d6efd;color:#fff;display:flex;align-items:center;justify-content:center;font-weight:bold;font-size:.85rem;flex-shrink:0;">
        <c:choose>
          <c:when test="${not empty sessionScope.nombreUsuario}">${sessionScope.nombreUsuario.substring(0,1).toUpperCase()}</c:when>
          <c:otherwise>M</c:otherwise>
        </c:choose>
      </div>
      <span style="font-weight:500;">${sessionScope.nombreUsuario}</span>
    </div>
  </div>

  <div class="formulario-grupo">
    <label>MES A CONFIGURAR</label>
    <div style="display:flex;align-items:center;gap:10px;max-width:380px;">
      <input class="campo-formulario" type="month" id="inputMes" style="flex:1;" onchange="mostrarAviso();actualizarResumen();">
      <button type="button" class="boton boton-primario" onclick="ponerSiguienteMes()">Siguiente mes</button>
    </div>
    <div id="avisoMes" style="display:none;color:#856404;background:#fff3cd;border:1px solid #ffc107;border-radius:5px;padding:7px 12px;margin-top:8px;font-size:.84rem;">
      ⚠ Solo se permite configurar el mes de <span id="lblMes"></span>. No se puede editar meses pasados ni futuros adicionales.
    </div>
  </div>

  <div class="formulario-grupo">
    <label>DURACIÓN DE CADA BLOQUE</label>
    <select class="campo-formulario" id="selDuracion" style="max-width:180px;" onchange="actualizarResumen()">
      <option value="30">30 minutos</option>
      <option value="60">60 minutos</option>
    </select>
  </div>
</section>

<%-- ── PASO 2: Días y rango horario ────────────────────────────────────── --%>
<section class="tarjeta" style="margin-bottom:20px;">
  <div style="display:flex;align-items:center;gap:10px;margin-bottom:4px;">
    <div style="background:#0d6efd;color:#fff;width:26px;height:26px;border-radius:50%;display:flex;align-items:center;justify-content:center;font-weight:bold;font-size:.9rem;flex-shrink:0;">2</div>
    <div><strong>Días de semana y rango horario</strong><br><small style="color:#666;">El sistema generará automáticamente cada bloque</small></div>
  </div>
  <hr style="margin:10px 0 18px;">

  <form id="frmDisp" action="${pageContext.request.contextPath}/medico/disponibilidad/guardar" method="post">
    <input type="hidden" name="mes"            id="h_mes">
    <input type="hidden" name="duracion"       id="h_duracion">

    <div class="formulario-grupo">
      <label>DÍAS DE LA SEMANA</label>
      <div style="display:flex;flex-wrap:wrap;gap:8px;margin-top:8px;">
        <label class="lbl-dia"><input type="checkbox" name="dias" value="LUNES"     class="chk-dia" style="display:none;"><span class="boton boton-secundario btn-dia">Lunes</span></label>
        <label class="lbl-dia"><input type="checkbox" name="dias" value="MARTES"    class="chk-dia" style="display:none;"><span class="boton boton-secundario btn-dia">Martes</span></label>
        <label class="lbl-dia"><input type="checkbox" name="dias" value="MIERCOLES" class="chk-dia" style="display:none;"><span class="boton boton-secundario btn-dia">Miércoles</span></label>
        <label class="lbl-dia"><input type="checkbox" name="dias" value="JUEVES"    class="chk-dia" style="display:none;"><span class="boton boton-secundario btn-dia">Jueves</span></label>
        <label class="lbl-dia"><input type="checkbox" name="dias" value="VIERNES"   class="chk-dia" style="display:none;"><span class="boton boton-secundario btn-dia">Viernes</span></label>
        <label class="lbl-dia"><input type="checkbox" name="dias" value="SABADO"    class="chk-dia" style="display:none;"><span class="boton boton-secundario btn-dia">Sábado</span></label>
      </div>
      <small id="lblDias" style="color:#666;margin-top:5px;display:block;">Seleccionados: ninguno</small>
    </div>

    <div class="formulario-grupo">
      <label>RANGO HORARIO DE ATENCIÓN</label>
      <div style="display:flex;align-items:center;gap:12px;max-width:380px;margin-top:8px;">
        <div style="flex:1;"><small style="color:#666;display:block;margin-bottom:3px;">Desde</small>
          <input class="campo-formulario" type="time" name="horaInicioRango" id="horaInicio" value="08:00" onchange="actualizarResumen()">
        </div>
        <div style="font-size:1.3rem;color:#aaa;padding-top:16px;">→</div>
        <div style="flex:1;"><small style="color:#666;display:block;margin-bottom:3px;">Hasta</small>
          <input class="campo-formulario" type="time" name="horaFinRango" id="horaFin" value="12:00" onchange="actualizarResumen()">
        </div>
      </div>
    </div>

    <%-- Resumen automático --%>
    <div id="resumen" style="display:none;background:#f0f4ff;border:1px solid #c7d5f8;border-radius:7px;padding:12px 16px;margin-top:10px;">
      <strong style="font-size:.9rem;">📋 Resumen automático del sistema</strong>
      <div style="display:flex;gap:28px;margin-top:8px;flex-wrap:wrap;font-size:.9rem;">
        <span><strong id="rDisp">0</strong> disponibilidades</span>
        <span><strong id="rBloqDia">0</strong> horarios/día</span>
        <span><strong id="rTotal">0</strong> bloques totales</span>
      </div>
    </div>

    <%-- Vista bloques por día --%>
    <div id="secBloques" style="display:none;margin-top:18px;">
      <div style="display:flex;justify-content:space-between;align-items:center;">
        <label>HORARIOS POR DÍA · <span id="lblNBloques">0</span> BLOQUES</label>
        <span id="lblMinBloque" style="color:#666;font-size:.84rem;">30 min/bloque</span>
      </div>
      <div id="listaBloques" style="margin-top:10px;display:grid;grid-template-columns:repeat(auto-fill,minmax(210px,1fr));gap:7px;"></div>
    </div>

    <div class="formulario-acciones" style="margin-top:22px;">
      <button class="boton boton-primario" type="button" onclick="enviar()" style="padding:11px 26px;">
        Guardar Disponibilidad Mensual
      </button>
    </div>
  </form>
</section>

<%-- ── PASO 3: Vista previa ────────────────────────────────────────────── --%>
<section class="tarjeta">
  <div style="display:flex;align-items:center;gap:10px;margin-bottom:4px;">
    <div style="background:#6c757d;color:#fff;width:26px;height:26px;border-radius:50%;display:flex;align-items:center;justify-content:center;font-weight:bold;font-size:.9rem;flex-shrink:0;">3</div>
    <div><strong>Vista previa — Generación automática</strong><br><small style="color:#666;">El sistema creará estas disponibilidades y horarios al confirmar</small></div>
  </div>
  <hr style="margin:10px 0 16px;">

  <strong>DISPONIBILIDADES · ${fn:length(disponibilidades)} FECHAS</strong>
  <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(190px,1fr));gap:8px;margin-top:12px;">
    <c:forEach var="d" items="${disponibilidades}">
      <div style="background:#f8f9fa;border:1px solid #dee2e6;border-radius:6px;padding:9px 13px;display:flex;justify-content:space-between;align-items:center;">
        <div>
          <span style="display:inline-block;width:9px;height:9px;border-radius:50%;background:#198754;margin-right:5px;"></span>
          <strong>${d.fecha}</strong>
        </div>
        <span style="color:#666;font-size:.83rem;">${d.diaSemana}</span>
      </div>
    </c:forEach>
    <c:if test="${empty disponibilidades}">
      <p style="color:#999;grid-column:1/-1;padding:10px 0;">Aún no tienes disponibilidades generadas. Configura el formulario superior.</p>
    </c:if>
  </div>
</section>

<script>
var MESES = ['','Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];
function ponerSiguienteMes() {
    var hoy = new Date(), anio = hoy.getFullYear(), mes = hoy.getMonth() + 2;
    if (mes > 12) { mes = 1; anio++; }
    document.getElementById('inputMes').value = anio + '-' + String(mes).padStart(2,'0');
    document.getElementById('lblMes').textContent = MESES[mes] + ' ' + anio;
    mostrarAviso(); actualizarResumen();
}
function mostrarAviso() { document.getElementById('avisoMes').style.display = 'block'; }

document.querySelectorAll('.chk-dia').forEach(function(chk) {
    chk.nextElementSibling.addEventListener('click', function() { chk.click(); });
    chk.addEventListener('change', function() {
        var sp = this.nextElementSibling;
        sp.style.background  = this.checked ? '#0d6efd' : '';
        sp.style.color       = this.checked ? '#fff'    : '';
        sp.style.borderColor = this.checked ? '#0d6efd' : '';
        actualizarDiasLabel(); actualizarResumen();
    });
});
function actualizarDiasLabel() {
    var map = {LUNES:'Lunes',MARTES:'Martes',MIERCOLES:'Miércoles',JUEVES:'Jueves',VIERNES:'Viernes',SABADO:'Sábado'};
    var sel = Array.from(document.querySelectorAll('.chk-dia:checked')).map(function(c){return map[c.value]||c.value;});
    document.getElementById('lblDias').textContent = 'Seleccionados: ' + (sel.length ? sel.join(', ') : 'ninguno');
}
function bloques(hi, hf, dur) {
    var h1=+hi.split(':')[0], m1=+hi.split(':')[1], h2=+hf.split(':')[0], m2=+hf.split(':')[1];
    var arr=[], cur=h1*60+m1;
    while (cur+dur <= h2*60+m2) {
        var s=pad(~~(cur/60))+':'+pad(cur%60); cur+=dur;
        arr.push(s+' – '+pad(~~(cur/60))+':'+pad(cur%60));
    }
    return arr;
}
function pad(n){ return String(n).padStart(2,'0'); }
function diasEnMes(mesStr, diasSel) {
    if (!mesStr) return 0;
    var p=mesStr.split('-'), anio=+p[0], mes=+p[1];
    var map={LUNES:1,MARTES:2,MIERCOLES:3,JUEVES:4,VIERNES:5,SABADO:6,DOMINGO:0};
    var cnt=0, ult=new Date(anio,mes,0).getDate();
    for (var d=1;d<=ult;d++){var dow=new Date(anio,mes-1,d).getDay(); if(diasSel.some(function(x){return map[x]===dow;}))cnt++;}
    return cnt;
}
function actualizarResumen() {
    var mes=document.getElementById('inputMes').value;
    var hi=document.getElementById('horaInicio').value;
    var hf=document.getElementById('horaFin').value;
    var dur=parseInt(document.getElementById('selDuracion').value);
    var dias=Array.from(document.querySelectorAll('.chk-dia:checked')).map(function(c){return c.value;});
    var bl=bloques(hi,hf,dur), nd=diasEnMes(mes,dias);
    document.getElementById('lblMinBloque').textContent = dur+' min/bloque';
    if (dias.length && mes && bl.length) {
        document.getElementById('rDisp').textContent    = nd;
        document.getElementById('rBloqDia').textContent = bl.length;
        document.getElementById('rTotal').textContent   = nd*bl.length;
        document.getElementById('resumen').style.display   = 'block';
        document.getElementById('lblNBloques').textContent = bl.length;
        document.getElementById('listaBloques').innerHTML  = bl.map(function(b,i){
            return '<div style="background:#f8f9fa;border:1px solid #dee2e6;border-radius:5px;padding:7px 11px;display:flex;justify-content:space-between;align-items:center;">'+
                '<span style="color:#aaa;font-size:.78rem;">#'+(i+1)+'</span><span>'+b+'</span>'+
                '<span style="color:#198754;font-size:.78rem;">disponible</span></div>';
        }).join('');
        document.getElementById('secBloques').style.display = 'block';
    } else {
        document.getElementById('resumen').style.display    = 'none';
        document.getElementById('secBloques').style.display = 'none';
    }
}
function enviar() {
    if (!document.getElementById('inputMes').value)  { alert('Seleccione el mes.'); return; }
    if (!document.querySelectorAll('.chk-dia:checked').length) { alert('Seleccione al menos un día.'); return; }
    document.getElementById('h_mes').value      = document.getElementById('inputMes').value;
    document.getElementById('h_duracion').value = document.getElementById('selDuracion').value;
    document.getElementById('frmDisp').submit();
}
</script>
</main></body></html>
