<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../_fragments/page-template-before-main.jsp"/>


<div class="row-wrapper">

    <section id="orders">

        <div class="d-inline-flex">
            <h3>Заказы</h3>
            <button type="button" class="btn btn-success round-button" data-toggle="modal" data-target="#newOrderForm"><i class="fas fa-parachute-box"></i></button>
            <%--<a class="btn btn-success round-button" href="${contextPath}/orders/add" role="button" data-toggle="tooltip modal" data-target="#newOrderForm" data-placement="right" title="Зарегистрировать новый заказ"><i class="fas fa-parachute-box"></i></a>--%>
        </div>

        <c:if test="${empty orders}">
            <p>Новых заказов нет</p>
        </c:if>

        <c:if test="${not empty orders}">
            <table id="filter-table">
                <tr>
                    <th class="width40">№</th>
                    <th class="width100">Дата</th>
                    <th class="width100">Погрузка</th>
                    <th class="width100">Выгрузка</th>
                    <th class="width80">Вес, кг</th>
                </tr>
                <tr class="table-filters">
                    <td><input type="text" class="width40"/></td>
                    <td><input type="text" class="width100"/></td>
                    <td><input type="text" class="width100"/></td>
                    <td><input type="text" class="width100"/></td>
                    <td><input type="text" class="width80"/></td>
                </tr>
                <c:forEach items="${orders}" var="truck">
                    <tr class="table-data draggable source" draggable=true>
                        <td>${truck.id}</td>
                        <td class="width100">${truck.creationDate.toString().substring(0,10)}</td>
                        <td>${truck.fromCity.name}</td>
                        <td>${truck.toCity.name}</td>
                        <td>${truck.weight}</td>
                    </tr>
                </c:forEach>
            </table>
            <script src="${contextPath}/resources/js/filter-table.js"></script>
        </c:if>

    </section>

    <section id="tickets">

        <h3>Маршрутные листы</h3>

        <c:if test="${empty tickets}">
            <p>Несохраненных маршрутных листов нет</p>
        </c:if>

        <c:if test="${not empty tickets}">
            <c:forEach items="${tickets}" var="ticket">
                <div class="ticket target row-wrapper">
                    <div class="ticket-truck-info">
                        <p><i class="fas fa-truck-moving fa-flip-horizontal"></i> ${ticket.truck.regNumber}</p>
                        <p><i class="fas fa-truck-loading"></i> ${ticket.truck.capacity} кг</p>
                        <p>
                            <c:forEach begin="1" end="${ticket.truck.shiftSize}" varStatus="loop">
                                <i class="fas fa-user"></i>
                            </c:forEach>
                        </p>
                    </div>
                    <div class="ticket-departure">
                        <p><i class="far fa-calendar-alt"></i></p>
                        <p>${ticket.departureDate.substring(5,10)}</p>
                        <p>${ticket.departureDate.substring(11,16)}</p>
                    </div>
                    <div class="ticket-route-info">
                        <c:forEach items="${ticket.stopovers}" var="stopover">
                            ${stopover.city.name}(${stopover.totalWeight})
                        </c:forEach>
                    </div>
                    <div class="ticket-actions">
                        <i class="fas fa-search-plus"></i>
                        <i class="fas fa-clipboard-check"></i>
                    </div>
                </div>
            </c:forEach>
        </c:if>

        <div id="new-ticket" class="ticket target">Новый маршрутный лист</div>

    </section>

</div>




<%-- Modal window for new order entry form --%>
<div class="modal fade" id="newOrderForm" tabindex="-1" role="dialog" aria-labelledby="newOrderFormTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="newOrderFormTitle">Регистрация нового заказа</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <%--@elvariable id="orderEntryForm" type="com.marta.logistika.dto.OrderEntryForm"--%>
                <form:form id="orderEntryForm" name="orderEntryForm" modelAttribute="orderEntryForm" method="post">
                    <div class="form-group">
                        <form:label path="description">Описание груза</form:label>
                        <form:textarea rows="3" path="description" class="form-control"/>
                    </div>
                    <div class="form-group">
                        <form:label path="weight">Вес груза, кг</form:label>
                        <form:input type="number" path="weight" class="form-control"/>
                    </div>
                    <div class="form-group">
                        <form:label path="fromCity">Пункт отправления</form:label>
                        <form:select path="fromCity" class="form-control">
                            <form:option value="" selected="selected" disabled="true" label="Выберите пункт отправления"/>
                            <form:options itemValue="id" itemLabel="name" items="${cities}"/>
                        </form:select>
                    </div>
                    <div class="form-group">
                        <form:label path="toCity">Пункт назначения</form:label>
                        <form:select path="toCity" class="form-control">
                            <form:option value="" selected="selected" disabled="true" label="Выберите пункт назначения"/>
                            <form:options itemValue="id" itemLabel="name" items="${cities}"/>
                        </form:select>
                    </div>
                </form:form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>
                <button type="button" class="btn btn-success" id="saveNewOrderBtn">Сохранить</button>
            </div>
        </div>
    </div>
</div>




<script src="${contextPath}/resources/js/drag-and-drop-orders.js"></script>
<script src="${contextPath}/resources/js/save-new-order.js"></script>
<script src="${contextPath}/resources/js/tooltips.js"></script>

<jsp:include page="../_fragments/page-template-after-main.jsp"/>
