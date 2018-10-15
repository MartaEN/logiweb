<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<jsp:include page="../../_fragments/page-template-fixed-before-main.jsp"/>


<div class="width600">
    <h3 class="modal-header">Регистрация нового заказа</h3>
    <form:form modelAttribute="orderEntryForm" method="post" action="${contextPath}/orders/add-no-ajax">
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
        <div class="modal-footer">
            <a class="btn btn-secondary" href="${contextPath}/orders" role="button">Вернуться</a>
            <input type="submit" class="btn btn-success" value="Сохранить">
        </div>
    </form:form>
</div>

<jsp:include page="../../_fragments/page-template-after-main.jsp"/>