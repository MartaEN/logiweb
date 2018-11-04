<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />

<!DOCTYPE html>
<html>

<head>
    <jsp:include page="../_fragments/head-common.jsp"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style-flex.css">
</head>

<body>

<jsp:include page="../_fragments/header.jsp"/>


<nav id="mainNav" class="sideNav"></nav>
<main id="driversView"></main>


<jsp:include page="../_fragments/footer.jsp"/>

<%-- Handelbars template for order list and ticket list--%>
<script src="${contextPath}/webjars/handlebars/handlebars.min.js"></script>
<script src="${contextPath}/resources/js/handelbars-helpers.js"></script>
<jsp:include page="template-drivers-nav.jsp"/>
<jsp:include page="template-drivers-view.jsp"/>

<%-- Scripts specific for the page --%>
<script src="${contextPath}/webjars/sockjs-client/sockjs.min.js"></script>
<script src="${contextPath}/webjars/stomp-websocket/stomp.min.js"></script>
<script src="${contextPath}/resources/js/websocket.js"></script>

</body>
</html>