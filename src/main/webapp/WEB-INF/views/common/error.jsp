<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../_fragments/page-template-before-main.jsp"/>


<h3 class="modal-header">Error Page</h3>
URL: ${url}
<br/>
Exception: ${exception.message}
<c:forEach items="${exception.stackTrace}" var="exceptionStackTrace">
    ${exceptionStackTrace}
</c:forEach>


<jsp:include page="../_fragments/page-template-after-main.jsp"/>
