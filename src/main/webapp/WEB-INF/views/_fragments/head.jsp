<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<head>

    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <title>LogiWeb</title>

    <%--Bootstrap--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/third-party/bootstrap/bootstrap.min.css">
    <%--fontawesome--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/third-party/fontawesome/css/all.min.css">
    <%--own stylesheet--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">

    <%--Bootstrap--%>
    <script src="${pageContext.request.contextPath}/resources/third-party/bootstrap/jquery-3.3.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/third-party/bootstrap/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
    <script src="${pageContext.request.contextPath}/resources/third-party/bootstrap/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>

    <%--csrf token for ajax--%>
    <%--<sec:csrfMetaTags />--%>
    <%--<script src="${pageContext.request.contextPath}/resources/js/ajax-csrf.js"></script>--%>

    <%--favicon--%>
    <link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/resources/img/favicon.ico">
    <link rel="manifest" href="${pageContext.request.contextPath}/resources/img/manifest.json">

</head>