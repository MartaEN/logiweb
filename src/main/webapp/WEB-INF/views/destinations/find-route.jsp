<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../_fragments/page_template_before_main.jsp"/>


    <a href="${contextPath}/destinations">Назад к списку</a>
    <h3>Поиск маршрута</h3>
    <form:form id="findRouteForm" modelAttribute="road">
        <form:select path="fromCity">
            <form:option value="" selected="selected" disabled="true" label="Выберите город отправления"/>
            <form:options itemValue="id" itemLabel="name" items="${cities}"/>
        </form:select>
        <form:select path="toCity">
            <form:option value="" selected="selected" disabled="true" label="Выберите город назначения"/>
            <form:options itemValue="id" itemLabel="name" items="${cities}"/>
        </form:select>
        <input type="submit" id="findRouteBtn" value="Рассчитать маршрут">
    </form:form>
    <div id="searchResult" style="display: none;"></div>


<jsp:include page="../_fragments/page_template_after_main.jsp"/>