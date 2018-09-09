<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="select" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />

<html>

<jsp:include page="../_fragments/head.jsp">
    <jsp:param name="title" value="Логистические центры"/>
</jsp:include>

<body>
    <div class="wrapper-row">
        <section>
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
                            <td><a href="${contextPath}/destinations/${city.id}">Редактировать</a></td>
                            <td><a href="${contextPath}/destinations/${city.id}/remove">Удалить</a></td>
                        </tr>
                    </c:forEach>
                </c:if>
            </table>
            <br>
            <a href="${contextPath}/destinations/add-city">Добавить новый</a>
        </section>
        <section>
            <a href="${contextPath}/destinations/find-route">Поиск маршрута</a>
        </section>
    </div>
</body>
</html>
