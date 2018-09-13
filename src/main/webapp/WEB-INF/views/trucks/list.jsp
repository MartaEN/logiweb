<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<c:set value="y" var="choiceYes" />
<c:set value="n" var="choiceNo" />
<jsp:include page="../_fragments/page-template-before-main.jsp"/>


<h3>Наш автопарк</h3>

<c:if test="${empty trucks}">
    <p>В автопарке пока нет ни одной фуры</p>
</c:if>

<c:if test="${not empty trucks}">
    <table id="filter-table">
        <tr>
            <th class="width160">Регистрационный знак</th>
            <th class="width160">Вместимость</th>
            <th class="width80">Смена</th>
            <th class="width160">Дислокация</th>
            <th class="width80">Исправность</th>
        </tr>
        <tr class="table-filters">
            <td >
                <input type="text" class="width160"/>
            </td>
            <td>
                <input type="text" class="width160"/>
            </td>
            <td>
                <input type="text" class="width80"/>
            </td>
            <td>
                <input type="text" class="width160"/>
            </td>
            <td>
                <input type="text" class="width80"/>
            </td>
        </tr>
        <c:forEach items="${trucks}" var="driver">
            <tr class="table-data">
                <td>${driver.regNumber}</td>
                <td>${driver.capacity} кг</td>
                <td><c:forEach begin="1" end="${driver.shiftSize}" varStatus="loop">
                        <i class="fas fa-user"></i>
                    </c:forEach>
                    <span class="hidden">${driver.shiftSize}</span>
                </td>
                <td>
                    <c:if test="${driver.parked}">
                        <i class="fas fa-parking"></i>
                    </c:if>
                    <c:if test="${!driver.parked}">
                        <i class="fas fa-road"></i>
                    </c:if>
                    ${driver.location.name}
                </td>
                <td>
                    <c:if test="${driver.serviceable}">
                        <i class="fas fa-check-circle icon-ok"></i>
                        <span class="hidden">${choiceYes}</span>
                    </c:if>
                    <c:if test="${!driver.serviceable}">
                        <i class="fas fa-exclamation-triangle icon-error"></i>
                        <span class="hidden">${choiceNo}</span>
                    </c:if>
                </td>
                <td><a href="${contextPath}/trucks/${driver.regNumber}"><i class="fas fa-pencil-alt"></i></a></td>
                <td><a href="${contextPath}/trucks/remove?regNumber=${driver.regNumber}"><i class="fas fa-trash-alt"></i></a></td>
            </tr>
        </c:forEach>
    </table>
    <script src="${contextPath}/resources/js/filter-table.js"></script>
</c:if>

<br>
<a class="btn btn-success" href="${contextPath}/trucks/add" role="button">Зарегистрировать новую фуру</a>


<jsp:include page="../_fragments/page-template-after-main.jsp"/>