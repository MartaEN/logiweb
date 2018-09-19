<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../_fragments/page-template-before-main.jsp"/>


    <h3>Наши логистические центры</h3>
    <c:if test="${empty cities}">
          <p>На карте пока нет ни одного логистического центра</p>
    </c:if>
    <c:if test="${not empty cities}">
        <div class="row-wrapper">
            <div>
                <table id="filter-table">
                    <tr>
                        <th>Город</th>
                        <th></th>
                    </tr>
                    <tr class='table-filters'>
                        <td>
                            <input type="text"/>
                        </td>
                    </tr>
                    <c:forEach items="${cities}" var="city">
                        <tr class="table-data">
                            <td>${city.name}</td>
                            <td><a href="${contextPath}/destinations/${city.id}"><i class="fas fa-pencil-alt"></i></a></td>
                            <td><a href="${contextPath}/destinations/${city.id}/remove"><i class="fas fa-trash-alt"></i></a></td>
                        </tr>
                    </c:forEach>
                </table>
                <script src="${contextPath}/resources/js/filter-table.js"></script>
            </div>
            <div>
                <a class="btn btn-warning" href="${contextPath}/destinations/find-route" role="button">Поиск маршрута</a>
            </div>
        </div>
    </c:if>
    <br>
    <a class="btn btn-success" href="${contextPath}/destinations/add-city" role="button">Добавить новый</a><br>


<jsp:include page="../_fragments/page-template-after-main.jsp"/>