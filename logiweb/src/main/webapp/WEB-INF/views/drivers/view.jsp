<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />

<jsp:include page="../_fragments/page-template-before-main.jsp"/>

<div id="core-screen" class="table col-sm-4"></div>

<%-- Handelbars template for order list and ticket list--%>
<script src="/webjars/handlebars/handlebars.min.js"></script>
<script src="${contextPath}/resources/js/handelbars-helpers.js"></script>
<jsp:include page="template-drivers-view.jsp"/>

<%-- Scripts specific for the page --%>
<script src="/webjars/sockjs-client/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/stomp.min.js"></script>
<script src="${contextPath}/resources/js/websocket.js"></script>

<jsp:include page="../_fragments/page-template-after-main.jsp"/>