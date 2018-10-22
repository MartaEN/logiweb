<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<jsp:include page="../../_fragments/page-template-fixed-before-main.jsp"/>

<div class="col-sm-4">

    <h3 class="modal-header"><spring:message code="destinations.cities.addNew.pageTitle"/></h3>

    <form:form modelAttribute="city" action="${contextPath}/destinations/add-city" method="post">
        <div class="form-group">
            <form:label path="name" cssClass="col-form-label"><spring:message code="destinations.cities.cityName"/> </form:label>
            <form:input path="name" cssClass="form-control" type="text" pattern="(^[А-ЯЁ][А-Яа-яЁё0-9\-\s]+$)|(^[A-Z][A-Za-z0-9\-\s]+$)" required="true" cssStyle="text-transform: capitalize"/>
            <form:errors path="name" cssClass="error-message"/>
        </div>
        <div class="modal-footer">
            <a class="btn btn-secondary" href="${contextPath}/destinations" role="button"><spring:message code="buttons.back"/></a>
            <input type="submit" class="btn btn-success" value="<spring:message code='buttons.save'/>">
        </div>
    </form:form>

</div>

<jsp:include page="../../_fragments/page-template-after-main.jsp"/>
