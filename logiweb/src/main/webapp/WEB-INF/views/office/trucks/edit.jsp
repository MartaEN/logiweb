<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../../_fragments/page-template-before-main.jsp"/>


    <h3>Редактирование</h3>
    <form method="POST" action="${contextPath}/trucks/edit">
        <div class="form-element">
            <label for="regNumber">Регистрационный номер: </label>
            <input type="text" id="regNumber" name="regNumber" value="${truck.regNumber}" readonly>
        </div>
        <div class="form-element">
            <label for="capacity">Вместимость, кг: </label>
            <input type="number" id="capacity" name="capacity" min="1" value="${truck.capacity}" required>
        </div>
        <div class="form-element">
            <label for="shiftSize">Водителей в смене: </label>
            <span id="shiftSize">
                <label class="width40">1<input type="radio" name="shiftSize" value="1" <c:if test="${truck.shiftSize==1}">checked </c:if></label>
                <label class="width40">2<input type="radio" name="shiftSize" value="2" <c:if test="${truck.shiftSize==2}">checked </c:if></label>
            </span>
        </div>
        <div class="form-element">
            <label for="serviceable">Исправна: </label>
            <input type="checkbox" id="serviceable" name="serviceable" <c:if test="${truck.serviceable==true}">checked="checked" </c:if> />
        </div>
        <input type="submit" class="btn btn-warning" value="Сохранить">
        <a class="btn btn-secondary" href="${contextPath}/trucks" role="button">Вернуться</a>
    </form>


<jsp:include page="../../_fragments/page-template-after-main.jsp"/>
