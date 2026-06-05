<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${not empty exito}"><div class="mensaje mensaje-exito">${exito}</div></c:if>
<c:if test="${not empty error}"><div class="mensaje mensaje-error">${error}</div></c:if>
