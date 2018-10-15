<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<jsp:include page="../../_fragments/page-template-fixed-before-main.jsp"/>

<div class="col-sm-6">

    <h3 class="modal-header"><spring:message code="destinations.roads.pageTitle"/> ${fromCity.name}</h3>

    <form:form modelAttribute="road" method="POST" action="${contextPath}/destinations/${fromCity.id}/add-road">
        <div class="form row">
            <div class="form-group col-sm-8">
                <label for="name" class="col-form-label"><spring:message code="destinations.cities.chooseDestination"/></label>
                <form:select path="toCity" class="form-control">
                    <form:option value="" selected="selected" disabled="true"><spring:message code="destinations.cities.chooseDestination"/></form:option>
                    <form:options itemValue="id" id="name" itemLabel="name" items="${cities}" />
                </form:select>
            </div>
            <div class="form-group col-sm-4">
                <label for="distance" class="col-form-label"><spring:message code="destinations.cities.distanceKm"/></label>
                <input type="text" class="form-control" id="distance" name="distance" required>
            </div>
        </div>

        <div class="modal-footer">
            <a class="btn btn-secondary" href="${contextPath}/destinations/${fromCity.id}" role="button"><spring:message code="buttons.return"/></a>
            <input type="submit" class="btn btn-success" value="<spring:message code='buttons.save'/>">
        </div>

    </form:form>
</div>

<jsp:include page="../../_fragments/page-template-after-main.jsp"/>