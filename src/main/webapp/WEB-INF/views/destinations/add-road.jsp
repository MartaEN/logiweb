<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set value="${pageContext.request.contextPath}" var="contextPath" />

<html>
<jsp:include page="../_fragments/head.jsp">
    <jsp:param name="title" value="Добавить"/>
</jsp:include>
<body>
<a href="/destinations/${fromCity.id}">Назад к центру: ${fromCity.name}</a>
<h3>Добавить новый прямой путь из города ${fromCity.name}</h3>
<form:form modelAttribute="road" method="POST" action="${contextPath}/destinations/${fromCity.id}/add-road">
    <form:select path="toCity">
        <form:option value="" disabled="true" label="Выберите направление"/>
        <form:options itemValue="id" itemLabel="name" items="${cities}" />
    </form:select>
    <input type="text" name="distance" placeholder="Расстояние, км" required>
    <input type="submit" value="Добавить">
</form:form>
</body>
</html>
