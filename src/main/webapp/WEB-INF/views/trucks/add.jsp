<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../_fragments/page-template-before-main.jsp"/>


    <h3>Новая фура</h3>
    <form:form modelAttribute="truck" action="${contextPath}/trucks/add" method="post">
        <div class="form-element">
            <label for="regNumber">Регистрационный номер: </label>
            <input type="text" id="regNumber" name="regNumber" placeholder="AA00000" pattern="^[a-zA-Z]{2}[0-9]{5}$" required>
        </div>
        <div class="form-element">
            <label for="capacity">Вместимость, кг: </label>
            <input type="number" id="capacity" name="capacity" min="1" required>
        </div>
        <div class="form-element">
            <label for="shiftSize">Водителей в смене: </label>
            <span id="shiftSize" class="custom-control custom-radio custom-control-inline">
                <input type="radio" value="1" id="shiftSize1" name="shiftSize" class="custom-control-input">
                <label class="custom-control-label" for="shiftSize1"><i class="fas fa-user"></i></label>
            </span>
            <span class="custom-control custom-radio custom-control-inline">
                <input type="radio" value="2" id="shiftSize2" name="shiftSize" class="custom-control-input">
                <label class="custom-control-label" for="shiftSize2"><i class="fas fa-user"></i><i class="fas fa-user"></i></label>
            </span>
            <%--<span id="shiftSize">--%>
                <%--<label>1<input type="radio" name="shiftSize" value="1" checked></label>--%>
                <%--<label>2<input type="radio" name="shiftSize" value="2"></label>--%>
            <%--</span>--%>
        </div>
        <div class="form-element">
            <label for="location">Дислокация: </label>
            <form:select path="location" id="location" name="location" required="required">
                <form:option value="" disabled="true" selected="selected" label="Выберите город"/>
                <form:options itemValue="id" itemLabel="name" items="${cities}" />
            </form:select>
        </div>
        <div class="form-element">
            <label for="serviceable">Исправна: </label>
            <input type="checkbox" id="serviceable" name="serviceable" checked="checked" />
        </div>
        <input type="submit" class="btn btn-warning" value="Сохранить">
        <a class="btn btn-secondary" href="${contextPath}/trucks" role="button">Вернуться</a>
    </form:form>


<jsp:include page="../_fragments/page-template-after-main.jsp"/>
