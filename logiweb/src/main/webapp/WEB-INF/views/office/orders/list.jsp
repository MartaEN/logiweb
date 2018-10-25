<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<jsp:include page="../../_fragments/page-template-fixed-before-main.jsp"/>


<div class="col-md-8">

    <div class="row-wrapper modal-header">
        <h3 class="col-md-8"><spring:message code='orders.orders'/></h3>
        <c:if test="${not empty orders}">
            <div class="input-group mb-3">
                <input type="text" class="form-control" placeholder="<spring:message code='orders.number'/>" id="orderId" aria-describedby="showOrderId">
                <div class="input-group-append">
                    <button id="search-order-btn" class="btn btn-outline-secondary" type="button"><spring:message code='buttons.search'/></button>
                </div>
            </div>
        </c:if>
    </div>

    <c:if test="${empty orders}">
        <p><spring:message code='orders.list-empty'/></p>
    </c:if>

    <c:if test="${not empty orders}">

        <table class="table table-sm table-hover">

            <thead>
            <tr>
                <th><spring:message code='orders.no'/></th>
                <th><spring:message code='orders.date'/></th>
                <th><spring:message code='orders.load'/></th>
                <th><spring:message code='orders.unload'/></th>
                <th><spring:message code='orders.status'/></th>
                <th></th>
            </tr>
            </thead>

            <c:forEach items="${orders}" var="order">
                <tr class="table-data">
                    <td>${order.id}</td>
                    <td>${order.creationDate.substring(0, 10)}</td>
                    <td>${order.fromCity.name}</td>
                    <td>${order.toCity.name}</td>
                    <td>${order.status.name()}</td>
                    <td><a href="#" class="view-order-link" property="${order.id}"><i class="fas fa-search"></i></a></td>
                </tr>
            </c:forEach>

        </table>


        <div class="modal-footer justify-content-center">
            <nav>
                <ul class="pagination justify-content-center">
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
        </div>

        <%-- Modal window to view a selected order --%>
        <div class="modal fade" id="viewOrderModal" tabindex="-1" role="dialog" aria-labelledby="viewOrderTitle" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="viewOrderTitle"><spring:message code='orders.view'/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" id="viewOrderDataPlaceholder"></div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal"><spring:message code='buttons.close'/></button>
                    </div>
                </div>
            </div>
        </div>

        <%--Handlebars template for order view form--%>
        <jsp:include page="template-order.jsp"/>
        <script src="/webjars/handlebars/handlebars.min.js"></script>
        <script src="${contextPath}/resources/js/handelbars-helpers.js"></script>
        <script src="${contextPath}/resources/js/show-order.js"></script>

    </c:if>

</div>





<jsp:include page="../../_fragments/page-template-after-main.jsp"/>
