<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<jsp:include page="../../_fragments/page-template-before-main.jsp"/>


<div class="col-sm-6">
    <h3 class="modal-header">Регистрация нового водителя</h3>
    <form:form modelAttribute="driver" action="${contextPath}/drivers/add" method="post">
        <div class="form-group">
            <form:label path="personalId" cssClass="col-form-label">Табельный номер: </form:label>
            <form:input path="personalId" cssClass="form-control" type="text" placeholder="123456" pattern="^[0-9]{6}$" required="true"/>
            <form:errors path="personalId" cssClass="error-message"/>
        </div>
        <div class="form-group">
            <form:label path="username" cssClass="col-form-label">Логин: </form:label>
            <form:input path="username" cssClass="form-control" type="text" pattern="^[a-z]+$" minlength="3" maxlength="50" required="true"/>
            <form:errors path="username" cssClass="error-message"/>
        </div>
        <div class="form-group">
            <form:label path="lastName" cssClass="col-form-label">Фамилия: </form:label>
            <form:input path="lastName" cssClass="form-control" type="text" pattern="^[А-Яа-яЁё]+[-]?[А-Яа-яЁё]+$" required="true"/>
            <form:errors path="lastName" cssClass="error-message"/>
        </div>
        <div class="form-group">
            <form:label path="firstName" cssClass="col-form-label">Имя: </form:label>
            <form:input path="firstName" cssClass="form-control" type="text"  pattern="^[А-Яа-яЁё]+[-]?[А-Яа-яЁё]+$" required="true"/>
            <form:errors path="firstName" cssClass="error-message"/>
        </div>
        <div class="form-group">
            <form:label path="location" cssClass="col-form-label">Дислокация: </form:label>
            <form:select path="location" cssClass="form-control">
                <c:if test="${empty driver.location}"><form:option value="" disabled="true" selected="true" label="Выберите город" /></c:if>
                <form:options items="${cities}" itemValue="id" itemLabel="name"/>
            </form:select>
            <form:errors path="location" cssClass="error-message"/>
        </div>
        <div class="modal-footer">
            <a class="btn btn-secondary" href="${contextPath}/drivers" role="button">Вернуться</a>
            <input type="submit" class="btn btn-success" value="Сохранить">
        </div>
    </form:form>
</div>


<jsp:include page="../../_fragments/page-template-after-main.jsp"/>
