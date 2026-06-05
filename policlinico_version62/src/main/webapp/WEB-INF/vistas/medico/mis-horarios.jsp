<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/vistas/compartido/base.jsp"><jsp:param name="titulo" value="Mis Horarios"/></jsp:include>
<h1 class="pagina-titulo">Mis Horarios</h1>

<section class="tarjeta">
  <p style="color:#666;margin-bottom:18px;font-size:.93rem;">
    Aquí puedes ver todos los bloques de horario asignados por el administrador para tu agenda.
  </p>

  <c:choose>
    <c:when test="${empty horarios}">
      <div style="text-align:center;padding:40px;color:#999;">
        <div style="font-size:2.5rem;margin-bottom:10px;">📅</div>
        <p>No tienes horarios asignados aún. El administrador configurará tu disponibilidad.</p>
      </div>
    </c:when>
    <c:otherwise>
      <%-- Agrupar por fecha usando variable de control --%>
      <c:set var="fechaActual" value=""/>
      <c:forEach var="h" items="${horarios}">
        <c:if test="${h.fechaTexto != fechaActual}">
          <c:if test="${fechaActual != ''}">
            </div><%-- cierra grid de bloques --%>
            <div style="height:18px;"></div>
          </c:if>
          <c:set var="fechaActual" value="${h.fechaTexto}"/>
          <div style="display:flex;align-items:center;gap:12px;margin-bottom:10px;">
            <div style="background:#0d6efd;color:#fff;border-radius:8px;padding:8px 16px;font-weight:bold;font-size:.95rem;min-width:120px;text-align:center;">
              ${h.fechaTexto}
            </div>
            <span style="color:#666;font-size:.9rem;">${h.diaSemana}</span>
          </div>
          <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(170px,1fr));gap:8px;margin-bottom:4px;">
        </c:if>
        <%-- Bloque de horario --%>
        <div style="border:1px solid ${h.estado == 'DISPONIBLE' ? '#c3e6cb' : h.estado == 'RESERVADO' ? '#ffc107' : '#dee2e6'};
                    background:${h.estado == 'DISPONIBLE' ? '#f0fff4' : h.estado == 'RESERVADO' ? '#fff8e1' : '#f8f9fa'};
                    border-radius:7px;padding:9px 13px;display:flex;flex-direction:column;gap:4px;">
          <div style="display:flex;align-items:center;gap:6px;">
            <span style="display:inline-block;width:8px;height:8px;border-radius:50%;
                         background:${h.estado == 'DISPONIBLE' ? '#198754' : h.estado == 'RESERVADO' ? '#ffc107' : '#adb5bd'};
                         flex-shrink:0;"></span>
            <strong style="font-size:.92rem;">${h.horaInicioTexto} – ${h.horaFinTexto}</strong>
          </div>
          <span style="font-size:.78rem;color:#666;">${h.estado}</span>
        </div>
      </c:forEach>
      <c:if test="${fechaActual != ''}">
        </div><%-- cierra último grid --%>
      </c:if>
    </c:otherwise>
  </c:choose>
</section>
</main></body></html>
