<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../../_fragments/page-template-before-main.jsp"/>


<div class="row-wrapper">


    <%-- View unassigned orders and and create new orders --%>
    <section id="orders">

        <div class="row-wrapper modal-header">
            <h3>Заказы</h3>
            <a class="btn btn-success" href="${contextPath}/orders/add-no-ajax" role="button">Зарегистрировать новый заказ</a><br>
            <button type="button" class="btn btn-success" data-toggle="modal" data-target="#newOrderForm"  style="margin-left: 5px;">То же, с AJAX</button>
        </div>

        <div id="order-list"></div>

    </section>


    <%-- View trip tickets in progress and create new trip tickets --%>
    <section id="tickets">

        <div class="modal-header">
            <h3>Маршрутные листы</h3>
            <a class="btn btn-success" href="${contextPath}/tickets/create" role="button">Создать новый маршрутный лист</a>
        </div>

        <div id="ticket-list"></div>

        <a href="${contextPath}/tickets/create" id="new-ticket" class="ticket new-ticket target">Новый маршрутный лист</a>

    </section>

</div>


<%-- Handelbars templates for order list and ticket list--%>
<jsp:include page="template-orders.jsp"/>
<jsp:include page="template-tickets.jsp"/>

<%-- Modal window for new order entry form --%>
<jsp:include page="modal-add.jsp"/>

<%-- Scripts specific for the page --%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.js"></script>
<script src="${contextPath}/resources/js/handelbars-helpers.js"></script>
<script src="${contextPath}/resources/js/monitor-update.js"></script>
<script src="${contextPath}/resources/js/save-new-order.js"></script>
<script src="${contextPath}/resources/js/tooltips.js"></script>

<jsp:include page="../../_fragments/page-template-after-main.jsp"/>
