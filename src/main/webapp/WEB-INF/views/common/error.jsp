<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../_fragments/page-template-before-main.jsp"/>



<h3 class="modal-header">Error Page</h3>
URL: ${url}
<BR />
Exception: ${exception.message}
<c:forEach items="${exception.stackTrace}" var="exceptionStackTrace">
    ${exceptionStackTrace}
</c:forEach>




<jsp:include page="../_fragments/page-template-after-main.jsp"/>
