<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../_fragments/page-template-before-main.jsp"/>


    <h3>Новая фура</h3>
    <form:form modelAttribute="truck" action="${contextPath}/trucks/add" method="post">
        <div class="form-group row">
            <label for="regNumber" class="col-sm-2 col-form-label">Регистрационный номер: </label>
            <div class="col-sm-4">
                <input type="text" id="regNumber" name="regNumber" required placeholder="AA00000" pattern="^[a-zA-Z]{2}[0-9]{5}$" class="form-control" >
            </div>
        </div>
        <div class="form-group row">
            <label for="capacity" class="col-sm-2 col-form-label">Вместимость, кг: </label>
            <div class="col-sm-4">
                <input type="number" id="capacity" name="capacity" required min="1" class="form-control">
            </div>
        </div>
        <div class="form-group row">
            <legend class="col-form-label col-sm-2 pt-0">Водителей в смене:</legend>
            <div class="col-sm-4">
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="shiftSize" id="shiftSize1" value="1" checked>
                    <label class="form-check-label" for="shiftSize1"><i class="fas fa-user"></i></label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="shiftSize" id="shiftSize2" value="2">
                    <label class="form-check-label" for="shiftSize2"><i class="fas fa-user"></i><i class="fas fa-user"></i></label>
                </div>
            </div>
        </div>
        <div class="form-group row">
            <label for="location" class="col-sm-2 col-form-label">Дислокация: </label>
            <div class="col-sm-4">
                <form:select class="form-control" path="location" id="location" name="location" required="required">
                    <form:option value="" disabled="true" selected="selected" label="Выберите город"/>
                    <form:options itemValue="id" itemLabel="name" items="${cities}" />
                </form:select>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-2">Исправна: </div>
            <div class="col-sm-4">
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="serviceable" name="serviceable" checked="checked">
                </div>
            </div>
        </div>
        <input type="submit" class="btn btn-success" value="Сохранить">
        <a class="btn btn-secondary" href="${contextPath}/trucks" role="button">Вернуться</a>
    </form:form>


<jsp:include page="../_fragments/page-template-after-main.jsp"/>
