<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath"/>
<jsp:include page="../../_fragments/page-template-fixed-before-main.jsp"/>


<div class="col-sm-6">
    <h3 class="modal-header">Маршрутный лист № ${ticket.id}</h3>
    <c:if test="${not empty error}">
    <p class="alert alert-danger"><spring:message code="${error}"/></p>
    </c:if>
    <div>
        <div class="form-group row">
            <span class="col-sm-5">Плановая дата отправления:</span>
            <span class="col-sm-7"><i class="fas fa-calendar-alt"></i> ${ticket.departureDateTime.substring(0,10)}
                <i class="far fa-clock"></i> ${ticket.departureDateTime.substring(11,16)}
                <a data-toggle="collapse" href="#dateTimeEditCollapse" role="button" aria-expanded="false"
                   aria-controls="collapseExample"><i class="fas fa-pencil-alt"></i></a>
            </span>
        </div>
        <div class="collapse" id="dateTimeEditCollapse">
            <form:form modelAttribute="departureDateTime" method="post"
                       action="${contextPath}/tickets/${ticket.id}/update-departure">
            <div class="form-group row">
                <form:label path="departureDateTime"
                            cssClass="col-sm-5 col-form-label">Перенести на более позднее время: </form:label>
                <div class="input-group mb-3 col-sm-7">
                    <form:input path="departureDateTime" type="datetime-local" cssClass="form-control" required="true"
                                min="${minNewDateTime}"/>
                    <div class="input-group-append">
                        <button class="btn btn-outline-secondary" type="submit">Сохранить</button>
                    </div>
                </div>
                <div class="form-group-row">
                    <form:errors path="departureDateTime" cssClass="error-message"/>
                </div>
                </form:form>
            </div>
        </div>
        <div class="form-group">Назначена фура ${ticket.truck.regNumber}, грузоподъемность ${ticket.truck.capacity} кг
        </div>
        <div class="form-group">Водителей в смене: ${ticket.truck.shiftSize}</div>
        <br>
        <h5>Маршрутные точки</h5>
        <table class="table table-sm">
            <tr>
                <th>№</th>
                <th>Город</th>
                <th>Загрузка, кг</th>
            </tr>
            <c:forEach items="${ticket.stopoversSorted}" var="stopover">
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
            В данном маршрутном листе пока нет ни одного заказа
        </c:if>
        <c:if test="${not empty orders}">
            <table id="filter-table" class="table table-sm">
                <thead>
                <tr>
                    <th>№</th>
                    <th>Дата</th>
                    <th>Описание</th>
                    <th>Из</th>
                    <th>В</th>
                    <th>Вес</th>
                    <th></th>
                </tr>
                <tr class="table-filters">
                    <td><input type="text" /></td>
                    <td><input type="text" /></td>
                    <td><input type="text" /></td>
                    <td><input type="text" /></td>
                    <td><input type="text" /></td>
                    <td><input type="text" /></td>
                    <td></td>
                </tr>
                </thead>
                <c:forEach items="${orders}" var="order">
                    <tr class="table-data">
                        <td>${order.id}</td>
                        <td>${order.creationDate.toString().substring(0,10)}</td>
                        <td>${order.description}</td>
                        <td>${order.fromCity.name}</td>
                        <td>${order.toCity.name}</td>
                        <td>${order.weight}</td>
                        <td>
                            <form:form modelAttribute="ticketAndOrder" class="d-inline-flex"
                                       action="${pageContext.request.contextPath}/tickets/remove-order" method="POST">
                                <input type="submit" value="Удалить" class="btn btn-link"/>
                                <form:input path="ticketId" type="hidden" value="${ticket.id}"/>
                                <form:input path="orderId" type="hidden" value="${order.id}"/>
                            </form:form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <script src="${contextPath}/resources/js/filter-table.js"></script>
        </c:if>
        <p></p>

        <div class="modal-header modal-footer">
            <form action="/tickets/${ticket.id}/approve" method="post">
                <a class="btn btn-secondary" href="${contextPath}/orders" role="button">Назад</a>
                <input id="findDriversBtn" property="${ticket.id}" type="submit" class="btn btn-success" name="sign"
                       value="Утвердить маршрутный лист"/>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
            <%--Неудавшийся multiple select через ajax и обычную форму--%>
            <%--<button id="findDriversBtn" property="${ticket.id}" type="button" class="btn btn-success">Перейти к оформлению маршрутного листа</button>--%>
            <%--Неудавшийся multiple select через post spring формы--%>
            <%--<a class="btn btn-success" href="${contextPath}/tickets/${ticket.id}/select-drivers">Перейти к оформлению маршрутного листа</a>--%>
        </div>

        <div id="approvalBlock" style="display: none">
            <p>Требуемое количество водителей на маршрут: ${ticket.truck.shiftSize}</p>
            <div id="driversSearchResult"></div>
        </div>

    </div>


    <jsp:include page="../../_fragments/page-template-after-main.jsp"/>

