<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../_fragments/page-template-before-main.jsp"/>

<div class="col-sm-4">

    <h3 class="modal-header"><spring:message code="destinations.cities.addNew.pageTitle"/></h3>

    <form method="POST" action="${contextPath}/destinations/add-city">

        <div class="form-group">
            <label for="name" class="col-form-label"><spring:message code="destinations.cities.cityName"/></label>
            <input id="name" type="text" class="form-control" name="name" required>
        </div>

        <div class="modal-footer">
            <a class="btn btn-secondary" href="${contextPath}/destinations" role="button"><spring:message code="buttons.back"/></a>
            <%--//todo как загнать spring message в атрибуты другого тега ?--%>
            <input type="submit" class="btn btn-success" value="Сохранить">
        </div>

    </form>
</div>

<jsp:include page="../_fragments/page-template-after-main.jsp"/>
