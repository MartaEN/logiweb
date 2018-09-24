<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
                <%--@elvariable id="orderViewForm" type="com.marta.logistika.dto.OrderRecordFull"--%>
                <form:form id="orderEntryForm" name="orderEntryForm" modelAttribute="orderViewForm" method="get">
                    <div class="form-row">
                        <div class="form-group col-md-3">
                            <form:label path="id">Номер</form:label>
                            <form:input disabled="true" path="id" class="form-control"/>
                        </div>
                        <div class="form-group col-md-5">
                            <form:label path="creationDate">Дата</form:label>
                            <form:input disabled="true" path="creationDate" class="form-control"/>
                        </div>
                        <div class="form-group col-md-4">
                            <form:label path="status">Статус</form:label>
                            <form:input disabled="true" path="status" class="form-control"/>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <form:label path="fromCity.name">Пункт отправления</form:label>
                            <form:input disabled="true" path="fromCity.name" class="form-control"/>
                        </div>
                        <div class="form-group">
                            <form:label path="toCity.name">Пункт отправления</form:label>
                            <form:input disabled="true" path="toCity.name" class="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <form:label path="description">Описание груза</form:label>
                        <form:textarea rows="3" disabled="true" path="description" class="form-control"/>
                    </div>
                    <div class="form-group">
                        <form:label path="weight">Вес груза, кг</form:label>
                        <form:input type="number" path="weight" class="form-control"/>
                    </div>

                </form:form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>
            </div>
        </div>
    </div>
</div>