<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="../_fragments/page-template-before-main.jsp"/>

<div class="table col-sm-4">
    <p>${instruction.driverStatus}</p>
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
            <form:form modelAttribute="instructionDetails" action="${pageContext.request.contextPath}/logiweb/${instruction.task.path}" method="POST">
                <button class="btn btn-success">${instruction.requestedActionMessage}</button>
                <form:input path="ticketId" type="hidden" value="${instruction.ticket.id}"/>
                <form:input path="targetStep" type="hidden" value="${instruction.targetStep}"/>
            </form:form>
        </c:if>
    </div>
    <br><br>
    <div>
        <c:if test="${not empty instruction.ticket}">
            <h5>Маршрутный лист №${instruction.ticket.id}</h5>
            <p><i class="far fa-calendar-alt"></i> ${instruction.ticket.departureDateTime.substring(0, 10)} <i class="far fa-clock"></i> ${instruction.ticket.departureDateTime.substring(11, 16)} <i class="fas fa-map-marker-alt"></i> ${instruction.ticket.stopovers.get(0).city.name}</p>
            <p><i class="fas fa-truck-moving fa-flip-horizontal"></i> ${instruction.ticket.truck.regNumber} ${instruction.ticket.truck.capacity}кг</p>
            <p>
                <c:forEach items="${instruction.ticket.drivers}" var="driver">
                    <p><i class="fas fa-user"></i> ${driver.firstName} ${driver.lastName}</p>
                </c:forEach>
            </p>
            <c:forEach items="${instruction.ticket.stopovers}" var="stopover">
                <p><i class="fas fa-parking"></i> ${stopover.city.name}</p>
            </c:forEach>
        </c:if>
    </div>

</div>



<jsp:include page="../_fragments/page-template-after-main.jsp"/>
