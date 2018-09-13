<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../_fragments/page-template-before-main.jsp"/>


    <h3>Регистрация нового водителя</h3>
    <form:form modelAttribute="driver" action="${contextPath}/drivers/add" method="post">
        <div class="form-element">
            <label for="personalId">Табельный номер: </label>
            <input type="text" id="personalId" name="personalId" placeholder="123456" pattern="^[0-9]{6}$" required>
        </div>
        <div class="form-element">
            <label for="lastName">Фамилия: </label>
            <input type="lastName" id="lastName" name="lastName" pattern="^[А-Яа-яЁё]+[-]?[А-Яа-яЁё]+$" required>
        </div>
        <div class="form-element">
            <label for="firstName">Имя: </label>
            <input type="firstName" id="firstName" name="firstName" pattern="^[А-Яа-яЁё]+[-]?[А-Яа-яЁё]+$" required>
        </div>
        <div class="form-element">
            <label for="phone">Телефон: </label>
            <input type="phone" id="phone" name="phone" required>
        </div>
        <input type="submit" class="btn btn-warning" value="Сохранить">
        <a class="btn btn-secondary" href="${contextPath}/drivers" role="button">Вернуться</a>
    </form:form>


<jsp:include page="../_fragments/page-template-after-main.jsp"/>
