<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../../_fragments/page-template-before-main.jsp"/>

<div class="col-sm-6">
    <h3 class="modal-header"><spring:message code="destinations.cities.edit.pageTitle"/></h3>
    <h4>${city.name}</h4>
    <table class="table">
        <thead><spring:message code="destinations.cities.edit.isLinkedWith"/></thead>
        <tr>
            <th><spring:message code="destinations.cities.city"/></th>
            <th><spring:message code="destinations.cities.distanceKm"/></th>
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

    <div class="modal-footer">
        <a class="btn btn-secondary" href="${contextPath}/destinations" role="button"><spring:message code="buttons.return"/></a>
        <a class="btn btn-success" href="${contextPath}/destinations/${city.id}/add-road" role="button"><spring:message code="destinations.cities.edit.addDestination"/></a>
    </div>
</div>


<jsp:include page="../../_fragments/page-template-after-main.jsp"/>
