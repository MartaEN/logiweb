<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:include page="../_fragments/page-template-flex-before-main.jsp"/>


<h3 class="modal-header"><spring:message code='security.accessDeniedTitle'/></h3>
<p><spring:message code='security.accessDeniedText'/></p>


<jsp:include page="../_fragments/page-template-after-main.jsp"/>