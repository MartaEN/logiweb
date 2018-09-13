<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../_fragments/page-template-before-main.jsp"/>


    <h3>Редактирование</h3>
    <form method="POST" action="${contextPath}/drivers/edit">
        <div class="form-element">
            <label for="personalId">Табельный номер: </label>
            <input type="text" id="personalId" name="personalId" value="${driver.personalId}" readonly>
        </div>
        <div class="form-element">
            <label for="lastName">Фамилия: </label>
            <input type="lastName" id="lastName" name="lastName" value="${driver.lastName}" pattern="^[А-Яа-яЁё]+[-]?[А-Яа-яЁё]+$" required>
        </div>
        <div class="form-element">
            <label for="firstName">Имя: </label>
            <input type="firstName" id="firstName" name="firstName"  value="${driver.firstName}" pattern="^[А-Яа-яЁё]+[-]?[А-Яа-яЁё]+$" required>
        </div>
        <div class="form-element">
            <label for="phone">Телефон: </label>
            <input type="phone" id="phone" name="phone" value="${driver.phone}" required>
        </div>
        <input type="submit" class="btn btn-warning" value="Сохранить">
        <a class="btn btn-secondary" href="${contextPath}/drivers" role="button">Вернуться</a>
    </form>


<jsp:include page="../_fragments/page-template-after-main.jsp"/>
