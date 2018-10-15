<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<jsp:include page="../../_fragments/page-template-fixed-before-main.jsp"/>


<div class="col-sm-6">
    <h3 class="modal-header">Редактирование данных водителя</h3>
    <form:form modelAttribute="driver" action="${contextPath}/drivers/${driver.personalId}" method="post">
        <div class="form-group">
            <label for="personalId" class="col-form-label">Табельный номер: </label>
            <input type="text" id="personalId" name="personalId" value="${driver.personalId}" readonly class="form-control">
        </div>
        <div class="form-group">
            <label for="lastName" class="col-form-label">Фамилия: </label>
            <input type="text" id="lastName" name="lastName" value="${driver.lastName}" pattern="^[А-Яа-яЁё]+[-]?[А-Яа-яЁё]+$" required class="form-control">
        </div>
        <div class="form-group">
            <label for="firstName" class="col-form-label">Имя: </label>
            <input type="text" id="firstName" name="firstName" value="${driver.firstName}" pattern="^[А-Яа-яЁё]+[-]?[А-Яа-яЁё]+$" required class="form-control">
        </div>
        <div class="form-group">
            <label for="location" class="col-form-label">Дислокация: </label>
            <select id="location" name="location" class="form-control" >
                <c:forEach items="${cities}" var="city">
                    <c:if test="${city != driver.location}">
                        <option value="${city.id}">${city.name}</option>
                    </c:if>
                    <c:if test="${city == driver.location}">
                        <option value="${city.id}" selected>${city.name}</option>
                    </c:if>
                </c:forEach>
            </select>
        </div>
        <div class="modal-footer">
            <a class="btn btn-secondary" href="${contextPath}/drivers" role="button">Вернуться</a>
            <input type="submit" class="btn btn-success" value="Сохранить">
        </div>
    </form:form>
</div>


<jsp:include page="../../_fragments/page-template-after-main.jsp"/>
