<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>



    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <title>LogiWeb</title>

    <%--Bootstrap--%>
    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/bootstrap/js/bootstrap.min.js"></script>

    <%--fontawesome--%>
    <link href="/webjars/font-awesome/css/all.min.css" rel="stylesheet">

    <%--csrf token for ajax--%>
    <%--<sec:csrfMetaTags />--%>
    <%--<script src="${pageContext.request.contextPath}/resources/js/ajax-csrf.js"></script>--%>

    <%--favicon--%>
    <link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/resources/img/favicon.ico">
    <link rel="manifest" href="${pageContext.request.contextPath}/resources/img/manifest.json">
