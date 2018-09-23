<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="../_fragments/page-template-before-main.jsp"/>

<div class="col-sm-6">

    <h3 class="modal-header">Маршрутный лист ${ticketId}</h3>
    <h5>Назначение водителей</h5>
    <p>Требуется водителей: ${shiftSize}</p>

    <c:if test="${not empty driverList}">
        <div>
            <h6>Выберите водителей</h6>
            <form:form modelAttribute="driverSelectForm" action="/tickets/${ticketId}/finalize" method="post">
                <div class="form-group">
                    <%--вылетает с ошибкой org.springframework.beans.NotReadablePropertyException: Invalid property '' of bean class [com.marta.logistika.dto.DriverRecord]: Bean property '' is not readable or has an invalid getter method: Does the return type of the getter match the parameter type of the setter?--%>
                    <form:label path="drivers" cssClass="form-label">Выберите водителей</form:label>
                    <form:select path="drivers" cssClass="form-control" multiple="true" items="${driverList}" var="driver" itemLabel="${driver.personalId} ${driver.firstName} ${driver.lastName}" itemValue="${driver.personalId}" />
                </div>
                <div class="modal-header modal-footer">
                    <a class="btn btn-secondary" href="/tickets/${ticketId}" role="button">Назад</a>
                    <input type="submit" class="btn btn-success" value="Назначить водителей">
                </div>
            </form:form>
        </div>
    </c:if>

    <c:if test="${empty driverList}">
        <p>В городе отправления на указанное время свободных водителей</p>
        <div class="modal-header modal-footer">
            <a class="btn btn-secondary" href="/tickets/${ticketId}" role="button">Назад</a>
        </div>
    </c:if>
</div>


<jsp:include page="../_fragments/page-template-after-main.jsp"/>
