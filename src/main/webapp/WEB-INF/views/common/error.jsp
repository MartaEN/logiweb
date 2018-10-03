<jsp:useBean id="error" scope="request" type="com.marta.logistika.dto.ErrorMessage"/>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:include page="../_fragments/page-template-before-main.jsp"/>


<h3 class="modal-header"><spring:message code='${error.title}'/></h3>
<spring:message code='${error.message}'/>

<jsp:include page="../_fragments/page-template-after-main.jsp"/>
