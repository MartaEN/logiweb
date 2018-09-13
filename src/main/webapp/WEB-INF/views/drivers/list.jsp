<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<c:set value="y" var="choiceYes" />
<c:set value="n" var="choiceNo" />
<jsp:include page="../_fragments/page-template-before-main.jsp"/>


<h3>Наши водители</h3>

<c:if test="${empty drivers}">
    <p>В штате пока нет ни одного водителя</p>
</c:if>

<c:if test="${not empty drivers}">
    <table id="filter-table">
        <tr>
            <th class="width160">Табельный номер</th>
            <th class="width160">Фамилия</th>
            <th class="width160">Имя</th>
            <th class="width160">Телефон</th>
        </tr>
        <tr class="table-filters">
            <td >
                <input type="text" class="width160"/>
            </td>
            <td>
                <input type="text" class="width160"/>
            </td>
            <td>
                <input type="text" class="width160"/>
            </td>
            <td>
                <input type="text" class="width160"/>
            </td>
        </tr>
        <c:forEach items="${drivers}" var="driver">
            <tr class="table-data">
                <td>${driver.personalId}</td>
                <td>${driver.lastName}</td>
                <td>${driver.firstName}</td>
                <td>${driver.phone}</td>
                <td><a href="${contextPath}/drivers/${driver.personalId}"><i class="fas fa-pencil-alt"></i></a></td>
                <td><a href="${contextPath}/drivers/remove?personalId=${driver.personalId}"><i class="fas fa-trash-alt"></i></a></td>
            </tr>
        </c:forEach>
    </table>
    <script src="${contextPath}/resources/js/filter-table.js"></script>
</c:if>

<br>
<a class="btn btn-success" href="${contextPath}/drivers/add" role="button">Зарегистрировать нового водителя</a>


<jsp:include page="../_fragments/page-template-after-main.jsp"/>