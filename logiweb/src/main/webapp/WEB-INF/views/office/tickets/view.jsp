<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../../_fragments/page-template-before-main.jsp"/>


<div class="col-sm-6">
    <h3 class="modal-header">Маршрутный лист № ${ticket.id}</h3>
    <p>Статус: ${ticket.status}</p>
    <p>Назначена фура ${ticket.truck.regNumber}, грузоподъемность ${ticket.truck.capacity} кг</p>
    <p>Назначены водители:</p>
    <ul>
        <c:forEach items="${ticket.drivers}" var="driver">
            <li>${driver.personalId} ${driver.firstName} ${driver.lastName}</li>
        </c:forEach>
    </ul>
    <p>Плановая дата отправления: ${ticket.departureDateTime.substring(0,10)} ${ticket.departureDateTime.substring(11,16)}</p>

    <h5>Маршрутные точки</h5>
    <table>
        <tr>
            <th>№</th>
            <th>Город</th>
            <th>Загрузка, кг</th>
        </tr>
        <c:forEach items="${ticket.stopovers}" var="stopover">
            <tr>
                <td>${stopover.sequenceNo + 1}</td>
                <td>${stopover.city.name}</td>
                <td>${stopover.totalWeight}</td>
            </tr>
        </c:forEach>
    </table>
    <p></p>
    <h5>Заказы</h5>
    <c:if test="${empty orders}">
        В данном маршрутном листе нет ни одного заказа
    </c:if>
    <c:if test="${not empty orders}">
        <table>
            <tr>
                <th>№</th>
                <th>Дата</th>
                <th>Описание</th>
                <th>Из</th>
                <th>В</th>
                <th>Вес</th>
            </tr>
            <c:forEach items="${orders}" var="order">
                <tr>
                    <td>${order.id}</td>
                    <td>${order.creationDate.toString().substring(0,10)}</td>
                    <td>${order.description}</td>
                    <td>${order.fromCity.name}</td>
                    <td>${order.toCity.name}</td>
                    <td>${order.weight}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <p></p>


    <div class="modal-footer">
        <a class="btn btn-secondary" href="${contextPath}/${returnTo}" role="button">Закрыть</a>
    </div>

</div>

<jsp:include page="../../_fragments/page-template-after-main.jsp"/>

