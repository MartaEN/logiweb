<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="../_fragments/page-template-before-main.jsp"/>

<div>
    <div id="instruction">
        ${instruction.directiveMessage}
    </div>
    <c:if test="${not empty instruction.requestedActionMessage}">
        <form:form modelAttribute="instructionDetails" action="${pageContext.request.contextPath}/logiweb/${instruction.command.path}" method="POST">
            <button class="btn btn-success">${instruction.requestedActionMessage}</button>
            <form:input path="ticketId" type="hidden" value="${instruction.ticketId}"/>
            <form:input path="step" type="hidden" value="${instruction.step}"/>
        </form:form>
    </c:if>

</div>



<jsp:include page="../_fragments/page-template-after-main.jsp"/>
