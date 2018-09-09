<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set value="${pageContext.request.contextPath}" var="contextPath" />

<html>
<jsp:include page="../_fragments/head.jsp">
    <jsp:param name="title" value="Создать"/>
</jsp:include>
<body>
    <a href="/destinations">Назад к списку</a>
    <h3>Создание нового логистического узла</h3>
    <form method="POST" action="${contextPath}/destinations/add-city">
        <input type="text" name="name" placeholder="Название города" required>
        <input type="submit" value="Сохранить">
    </form>
</body>
</html>
