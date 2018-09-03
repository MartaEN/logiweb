<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resPath" />

<!DOCTYPE >
<html>
<head>
    <title>Logistika</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" href="${resPath}/style.css"/>
</head>
<body>
    <menu>
        <a href="/destinations">Логистические центры</a>
    </menu>
</body>
</html>
