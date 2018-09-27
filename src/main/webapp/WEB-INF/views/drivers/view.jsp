<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="../_fragments/page-template-before-main.jsp"/>

<div class="table col-sm-4">
    <div>
        <c:if test="${not empty instruction.currentStop}">
            <h3>${instruction.currentStop.name}</h3>
        </c:if>
    </div>
    <div class="alert alert-warning">
        ${instruction.directiveMessage}
    </div>
    <div>
        <c:if test="${not empty instruction.orders}">
            <table >
                <thead>
                    <th>№</th>
                    <th>Наименование</th>
                    <th>Вес, кг</th>
                    <th>Пункт назначения</th>
                </thead>
                <tbody>
                <c:forEach items="${instruction.orders}" var="order">
                    <tr>
                    <td>${order.id}</td>
                    <td>${order.description}</td>
                    <td>${order.weight}</td>
                    <td>${order.toCity.name}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
    <div class="driver-ui-main-button">
        <c:if test="${not empty instruction.requestedActionMessage}">
            <form:form modelAttribute="instructionDetails" action="${pageContext.request.contextPath}/logiweb/${instruction.command.path}" method="POST">
                <button class="btn btn-success">${instruction.requestedActionMessage}</button>
                <form:input path="ticketId" type="hidden" value="${instruction.ticketId}"/>
                <form:input path="step" type="hidden" value="${instruction.step}"/>
            </form:form>
        </c:if>
    </div>

</div>



<jsp:include page="../_fragments/page-template-after-main.jsp"/>
