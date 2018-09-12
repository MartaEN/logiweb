<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../_fragments/page_template_before_main.jsp"/>


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
    <a href="${contextPath}/destinations/add-city">Добавить новый</a><br>
    <a href="${contextPath}/destinations/find-route">Поиск маршрута</a>


<jsp:include page="../_fragments/page_template_after_main.jsp"/>