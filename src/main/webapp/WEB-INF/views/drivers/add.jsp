<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../_fragments/page-template-before-main.jsp"/>


<div class="col-sm-6">
    <h3 class="modal-header">Регистрация нового водителя</h3>
    <form:form modelAttribute="driver" action="${contextPath}/drivers/add" method="post">
        <div class="form-group">
            <form:label path="personalId" cssClass="col-form-label">Табельный номер: </form:label>
            <form:input path="personalId" cssClass="form-control" type="text" placeholder="123456" pattern="^[0-9]{6}$" required="required"/>
            <form:errors path="personalId" />
        </div>
        <div class="form-group">
            <label for="lastName" class="col-form-label">Фамилия: </label>
            <input type="text" id="lastName" name="lastName" pattern="^[А-Яа-яЁё]+[-]?[А-Яа-яЁё]+$" required class="form-control">
        </div>
        <div class="form-group">
            <label for="firstName" class="col-form-label">Имя: </label>
            <input type="text" id="firstName" name="firstName" pattern="^[А-Яа-яЁё]+[-]?[А-Яа-яЁё]+$" required class="form-control">
        </div>
        <div class="form-group">
            <label for="location" class="col-form-label">Дислокация: </label>
            <div>
                <form:select class="form-control" path="location" id="location" name="location" required="required">
                    <form:option value="" disabled="true" selected="selected" label="Выберите город"/>
                    <form:options itemValue="id" itemLabel="name" items="${cities}" />
                </form:select>
            </div>
        </div>
        <div class="modal-footer">
            <a class="btn btn-secondary" href="${contextPath}/drivers" role="button">Вернуться</a>
            <input type="submit" class="btn btn-success" value="Сохранить">
        </div>
    </form:form>
</div>


<jsp:include page="../_fragments/page-template-after-main.jsp"/>
