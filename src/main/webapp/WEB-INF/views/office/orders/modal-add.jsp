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