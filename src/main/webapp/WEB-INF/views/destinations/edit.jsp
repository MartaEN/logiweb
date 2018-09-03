<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set value="${pageContext.request.contextPath}" var="contextPath" />

<html>
<head>
    <title>Редактировать</title>
</head>
<body>
    <h3>Редактирование логистического узла</h3>
    <h4>${city.name}</h4>
    <table>
        <thead>Связан прямыми дорогами с направлениями:</thead>
        <tr>
            <th>Город</th>
            <th>Расстояние, км</th>
            <th></th>
            <th></th>
        </tr>
        <c:if test="${not empty roads}">
            <c:forEach items="${roads}" var="road">
                <tr>
                    <td>${road.toCity.name}</td>
                    <td>${road.distance}</td>
                    <%--<td><a href="destinations/${city.id}/edit-road/${road.id}">Уточнить дистанцию</a></td>--%>
                    <%--<td><a href="destinations/${city.id}/remove-road/${road.id}">Удалить направление</a></td>--%>
                </tr>
            </c:forEach>
        </c:if>
    </table>
    <br>
    <a href="${city.id}/add-road">Добавить направление</a>
</body>
</html>
