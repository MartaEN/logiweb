<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />

<html>
<head>
    <title>Find route</title>
    <script src="${resourcesPath}/js/jquery-3.3.1.min.js"></script>
    <script>
        $(document).ready(function() {

            //todo как получить и дудочку, и кувшинчик?
            //то (1) есть сохранить binding к объекту RoadRecord и (2) сделать запрос ajax-ным?
            $('#findRouteForm').submit(function (event) {

                // event.preventDefault();

                // let input = $("findRouteForm").serialize();
                // let ajaxDataDiv = $('.ajaxData');

                $.post({
                    url: '${contextPath}/destinations/find-route',
                    // data: input,
                    dataType: 'json',
                    success: function (data) {
                        // ajaxDataDiv.html("Some no-error response from server received");
                        ajaxDataDiv.html(JSON.stringify(data));
                        ajaxDataDiv.show();
                    },
                    error: function (err) {
                        console.log(err);
                    }
                });

            });
        });
    </script>
</head>
<body>
    <form:form id="findRouteForm" modelAttribute="road">
        <form:select path="fromCity">
            <form:option value="" selected="selected" disabled="true" label="Выберите город отправления"/>
            <form:options itemValue="id" itemLabel="name" items="${cities}" />
        </form:select>
        <form:select path="toCity">
            <form:option value="" selected="selected" disabled="true" label="Выберите город назначения"/>
            <form:options itemValue="id" itemLabel="name" items="${cities}" />
        </form:select>
        <input type="submit" id="ajaxBtn" value="Рассчитать маршрут">
    </form:form>
    <div class="ajaxData" style="display: none;"></div>
</body>
</html>
