<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set value="${pageContext.request.contextPath}" var="contextPath" />

<!DOCTYPE >
<html>

<jsp:include page="../_fragments/head.jsp">
    <jsp:param name="title" value="LogiWeb"/>
</jsp:include>

<body>

<div class="content-wrapper">

    <jsp:include page="../_fragments/header.jsp"/>

    <div class="row-wrapper">

        <jsp:include page="../_fragments/navigation.jsp"/>

        <main>
            <a href="/destinations">Назад к списку</a>
            <h3>Редактирование логистического центра</h3>
            <h4>${city.name}</h4>
            <table>
                <thead>Связан прямыми дорогами с направлениями:</thead>
                <tr>
                    <th>Город</th>
                    <th>Расстояние, км</th>
                    <th></th>
                </tr>
                <c:if test="${not empty roads}">
                    <c:forEach items="${roads}" var="road">
                        <tr>
                            <td>${road.toCity.name}</td>
                            <td>${road.distance}</td>
                            <td><a href="${contextPath}/destinations/${city.id}/remove-road/${road.id}">Удалить направление</a></td>
                        </tr>
                    </c:forEach>
                </c:if>
            </table>
            <br>
            <a href="${city.id}/add-road">Добавить направление</a>
        </main>

    </div>

</div>

<jsp:include page="../_fragments/footer.jsp"/>

</body>
</html>
