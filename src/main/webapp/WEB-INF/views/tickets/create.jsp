<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />

<jsp:include page="../_fragments/page-template-before-main.jsp"/>


<div class="col-sm-6">
    <h3 class="modal-header">Создание маршрутного листа</h3>

    <form:form id="findTruckForm" modelAttribute="filterForm" method="post" action="${contextPath}/tickets/create">
        <div class="form-group row">
            <label for="departureDate" class="col-sm-4 col-form-label">Дата и время отправления: </label>
            <div class="col-sm-8">
                <input type="datetime-local" id="departureDate" name="departureDate" class="form-control" >
            </div>
        </div>
        <div class="form-group row">
            <label for="fromCity" class="col-sm-4 col-form-label">Пункт отправления: </label>
            <div class="col-sm-8">
                <form:select class="form-control" path="fromCity" id="fromCity" name="fromCity" required="required">
                    <form:option value="" disabled="true" selected="selected" label="Выберите начало маршрута"/>
                    <form:options itemValue="id" itemLabel="name" items="${cities}" />
                </form:select>
            </div>
        </div>
        <div class="form-group row">
            <label for="fromCity" class="col-sm-4 col-form-label">Пункт назначения, если отличается: </label>
            <div class="col-sm-8">
                <form:select class="form-control" path="toCity" id="fromCity" name="toCity">
                    <form:option value="" disabled="true" selected="selected" label="Выберите конец маршрута"/>
                    <form:options itemValue="id" itemLabel="name" items="${cities}" />
                </form:select>
            </div>
        </div>
        <div class="form-row">
            <legend class="col-form-label col-sm-4 pt-0">Необходимая грузоподъемность, кг:</legend>
            <div class="form-group col-sm-4">
                <label for="minCapacity">от</label>
                <input id="minCapacity" name="minCapacity" type="number" min="0" value="0" required class="form-control" >
            </div>
            <div class="form-group col-sm-4">
                <label for="maxCapacity">до</label>
                <input id="maxCapacity" name="maxCapacity" type="number" min="0" value="30000" required class="form-control" >
            </div>
        </div>

        <div class="modal-footer modal-header">
            <input id="findTrucksBtn" type="submit" class="btn btn-success" value="Искать свободные фуры">
        </div>

        <div id="searchResult"></div>

    </form:form>

</div>


<script src="${resourcesPath}/js/find-trucks.js"></script>
<jsp:include page="../_fragments/page-template-after-main.jsp"/>
