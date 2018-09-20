<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../_fragments/page-template-before-main.jsp"/>

<div class="col-sm-4">
    <h3 class="modal-header"><spring:message code="destinations.pageTitle"/></h3>
    <c:if test="${empty cities}">
          <p><spring:message code="destinations.noYetCenters"/></p>
    </c:if>
    <c:if test="${not empty cities}">
            <div>
                <table id="filter-table">
                    <tr>
                        <th><spring:message code="destinations.cities.city"/></th>
                        <th></th>
                    </tr>
                    <tr class='table-filters'>
                        <td>
                            <input type="text"/>
                        </td>
                    </tr>
                    <c:forEach items="${cities}" var="city">
                        <tr class="table-data">
                            <td>${city.name}</td>
                            <td><a href="${contextPath}/destinations/${city.id}"><i class="fas fa-pencil-alt"></i></a></td>
                            <td><a href="${contextPath}/destinations/${city.id}/remove"><i class="fas fa-trash-alt"></i></a></td>
                        </tr>
                    </c:forEach>
                </table>
                <script src="${contextPath}/resources/js/filter-table.js"></script>
            </div>
    </c:if>
    <br>
    <div class="modal-footer">
        <c:if test="${not empty cities}">
            <a class="btn btn-warning" href="${contextPath}/destinations/find-route" role="button"><spring:message code="destinations.findRoute"/></a>
        </c:if>
        <a class="btn btn-success" href="${contextPath}/destinations/add-city" role="button"><spring:message code="buttons.addNew"/></a><br>
    </div>
</div>

<jsp:include page="../_fragments/page-template-after-main.jsp"/>