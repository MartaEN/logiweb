<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../_fragments/page-template-before-main.jsp"/>


<div class="col-md-8">

    <div class="row-wrapper modal-header">
        <h3 class="col-md-8">Заказы</h3>
        <div class="input-group mb-3">
            <input type="number" class="form-control" placeholder="Номер заказа" aria-label="orderId" aria-describedby="showOrderId">
            <div class="input-group-append">
                <button class="btn btn-outline-secondary" type="button" id="showOrderId">Найти</button>
            </div>
        </div>
    </div>

    <c:if test="${empty orders}">
        <p>Список заказов пуст</p>
    </c:if>

    <c:if test="${not empty orders}">

        <table class="table table-hover">

            <thead>
            <tr>
                <th>№</th>
                <th>Дата</th>
                <th>Погрузка</th>
                <th>Выгрузка</th>
                <th>Статус</th>
            </tr>
            </thead>

            <c:forEach items="${orders}" var="order">
                <tr class="table-data">
                    <td>${order.id}</td>
                    <td>${order.creationDate.substring(0, 10)}</td>
                    <td>${order.fromCity.name}</td>
                    <td>${order.toCity.name}</td>
                    <td>${order.status.name()}</td>
                </tr>
            </c:forEach>

        </table>


        <nav>
            <ul class="pagination">
                <c:forEach begin="1" end="${totalPages}" var="i" varStatus="loop">
                    <c:if test="${i != page}">
                        <li class="page-item"><a class="page-link" href="/orders/view?page=${i}">${i}</a></li>
                    </c:if>
                    <c:if test="${i == page}">
                        <li class="page-item active">
                            <a class="page-link" href="#">${i}<span class="sr-only">(current)</span></a>
                        </li>
                    </c:if>
                </c:forEach>
            </ul>
        </nav>

        <%-- Modal window to view a selected order --%>
        <jsp:include page="modal-view.jsp"/>

    </c:if>


</div>





<jsp:include page="../_fragments/page-template-after-main.jsp"/>
