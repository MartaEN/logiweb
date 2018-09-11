<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set value="${pageContext.request.contextPath}" var="contextPath"/>
<c:set value="${contextPath}/resources" var="resourcesPath"/>

<!DOCTYPE >
<html>

<jsp:include page="../_fragments/head.jsp">
    <jsp:param name="title" value="LogiWeb"/>
</jsp:include>

<body>

<div class="content-wrapper">

    <jsp:include page="../_fragments/header.jsp"/>

    <div class="row-wrapper">

        <jsp:include page="../_fragments/navigation.jsp"/>

        <main>
            <a href="/destinations">Назад к списку</a>
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
        </main>

    </div>

</div>

<jsp:include page="../_fragments/footer.jsp"/>

<script src="${contextPath}/resources/js/find-route.js"></script>

</body>
</html>
