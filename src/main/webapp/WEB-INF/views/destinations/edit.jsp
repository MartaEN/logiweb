<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../_fragments/page_template_before_main.jsp"/>


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
                    <td><a href="${contextPath}/destinations/${city.id}/remove-road/${road.id}"><i class="fas fa-trash-alt"></i></a></td>
                </tr>
            </c:forEach>
        </c:if>
    </table>
    <br>
    <a class="btn btn-success" href="${contextPath}/destinations/${city.id}/add-road" role="button">Добавить направление</a>
    <a class="btn btn-secondary" href="${contextPath}/destinations" role="button">Назад к списку</a>


<jsp:include page="../_fragments/page_template_after_main.jsp"/>
