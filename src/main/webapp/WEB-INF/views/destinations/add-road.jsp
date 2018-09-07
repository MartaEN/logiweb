<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set value="${pageContext.request.contextPath}" var="contextPath" />

<html>
<head>
    <title>Добавить</title>
</head>
<body>
<a href="/destinations/${id}">Назад к центру: ${startCity.name}</a>
<h3>Добавить новый прямой путь из города ${startCity.name}</h3>
<form:form modelAttribute="road" method="POST" action="${contextPath}/destinations/${startCity.id}/add-road">
    <select name="toCityId" required>
        <option value="" disabled selected>Выберите направление</option>
        <c:if test="${not empty cities}">
            <c:forEach items="${cities}" var="city">
                <option value="${city.id}">${city.name}</option>
            </c:forEach>
        </c:if>
    </select>
    <input type="text" name="distance" placeholder="Расстояние, км" required>
    <input type="submit" value="Добавить">
</form:form>
</body>
</html>
