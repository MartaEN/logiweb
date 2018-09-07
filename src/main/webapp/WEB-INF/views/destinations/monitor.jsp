<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set value="${pageContext.request.contextPath}" var="contextPath" />

<html>
<head>
    <title>Логистические центры</title>
</head>
<body>
    <h3>Перечень действующих логистических центров</h3>
    <table>
        <tr>
            <th>Город</th>
            <th></th>
        </tr>
        <c:if test="${not empty cities}">
            <c:forEach items="${cities}" var="city">
                <tr>
                    <td>${city.name}</td>
                    <td><a href="destinations/${city.id}">Редактировать</a></td>
                </tr>
            </c:forEach>
        </c:if>
    </table>
    <br>
    <a href="destinations/add-city">Добавить новый</a>
</body>
</html>
