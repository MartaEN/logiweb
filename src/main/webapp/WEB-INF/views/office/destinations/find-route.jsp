<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../../_fragments/page-template-before-main.jsp"/>
<script src="${contextPath}/resources/js/find-route.js"></script>

<div class="col-sm-6">

    <h3 class="modal-header"><spring:message code="destinations.routeSearch.pageTitle"/></h3>

    <form:form id="findRouteForm" modelAttribute="road" class="form row">

        <form:select class="form-control col-sm-6" path="fromCity">
            <form:option value="" selected="selected" disabled="true"><spring:message code="destinations.cities.chooseDepartureCity"/></form:option>
            <form:options itemValue="id" itemLabel="name" items="${cities}"/>
        </form:select>
        <form:select class="form-control col-sm-6" path="toCity">
            <form:option value="" selected="selected" disabled="true"><spring:message code="destinations.cities.chooseDestinationCity"/></form:option>
            <form:options itemValue="id" itemLabel="name" items="${cities}"/>
        </form:select>

        <div class="modal-footer">
            <a href="${contextPath}/destinations" class="btn btn-secondary" role="button"><spring:message code="buttons.back"/></a>
            <input type="submit" class="btn btn-success" id="findRouteBtn" value='<spring:message code="destinations.routeSearch.calculateRoute"/>'>
        </div>

    </form:form>
    <div id="searchResult" style="display: none;"></div>

</div>

<jsp:include page="../../_fragments/page-template-after-main.jsp"/>