<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../../_fragments/page-template-before-main.jsp"/>


<h3>Sandbox page so far...</h3>

<spring:message code="test.stub"/>
<spring:message code="com.marta.logistika.validator.UniquePersonalIdValidator.message"/>


<jsp:include page="../../_fragments/page-template-after-main.jsp"/>
