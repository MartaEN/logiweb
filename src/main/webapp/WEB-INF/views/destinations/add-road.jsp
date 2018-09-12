<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../_fragments/page_template_before_main.jsp"/>


    <h3>Добавить новый прямой путь из города ${fromCity.name}</h3>
    <form:form modelAttribute="road" method="POST" action="${contextPath}/destinations/${fromCity.id}/add-road">
        <form:select path="toCity">
            <form:option value="" disabled="true" selected="selected" label="Выберите направление"/>
            <form:options itemValue="id" itemLabel="name" items="${cities}" />
        </form:select>
        <input type="text" name="distance" placeholder="Расстояние, км" required>
        <input type="submit" class="btn btn-warning" value="Добавить">
        <a class="btn btn-secondary" href="${contextPath}/destinations/${fromCity.id}" role="button">Вернуться</a>
    </form:form>


<jsp:include page="../_fragments/page_template_after_main.jsp"/>