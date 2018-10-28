<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<jsp:include page="../../_fragments/page-template-fixed-before-main.jsp"/>


<div class="col-md-8">


    <h3 class="col-md-8">Маршрутные листы</h3>

    <c:if test="${empty tickets}">
        <p>Маршрутных листов не зарегистрировано</p>
    </c:if>

    <c:if test="${not empty tickets}">

        <table id="filter-table" class="table table-sm table-hover">
            <thead>
            <tr>
                <th>№</th>
                <th>Из</th>
                <th>Отправление</th>
                <th>Фура</th>
                <th>Статус</th>
            </tr>
            <tr class="table-filters">
                <td ><input type="text" class="width80"/></td>
                <td><input type="text" class="width160"/></td>
                <td><input type="text" class="width160"/></td>
                <td><input type="text" class="width160"/></td>
                <td><input type="text" class="width160"/></td>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${tickets}" var="ticket">
                <tr class="table-data">
                    <td>${ticket.id}</td>
                    <td>${ticket.stopoversSorted.get(0).city.name}</td>
                    <td><i class="far fa-calendar-alt"></i> ${ticket.departureDateTime.substring(0, 10)} <i class="far fa-clock"></i> ${ticket.departureDateTime.substring(11, 16)}</td>
                    <td>${ticket.truck.regNumber}</td>
                    <td>${ticket.status.toString()}</td>
                    <td><a href="${contextPath}/tickets/${ticket.id}"><i class="fas fa-search"></i></a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <script src="${contextPath}/resources/js/filter-table.js"></script>
    </c:if>

</div>





<jsp:include page="../../_fragments/page-template-after-main.jsp"/>
