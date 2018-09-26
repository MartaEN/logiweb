<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page session="true"%>
<jsp:include page="../_fragments/page-template-before-main.jsp"/>

<div class="col-sm-4">

    <h3 class="modal-header"><spring:message code="security.pleaseLogIn"/></h3>

    <form action="${pageContext.request.contextPath}/authenticate" method="POST">

        <%--Failed login alert--%>
        <c:if test="${param.error != null}">
            <p class="alert alert-danger"><spring:message code='security.failedLogin'/></p>
        </c:if>
        <%--Logout alert--%>
        <c:if test="${param.logout != null}">
            <p class="alert alert-success"><spring:message code='security.loggedOut'/></p>
        </c:if>

        <%--Username and password entry--%>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text" id="addon1"><i class="fas fa-user"></i></span>
            </div>
            <input type="text" name="username" class="form-control" placeholder="<spring:message code='security.username'/>" aria-label="username" aria-describedby="addon1">
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text" id="addon2"><i class="fas fa-unlock-alt"></i></span>
            </div>
            <input type="password" name="password" class="form-control" placeholder="<spring:message code='security.password'/>" aria-label="password" aria-describedby="addon2">
        </div>
        <div class="modal-footer">
            <input type="submit" class="btn btn-success" value="<spring:message code='security.login'/>">
        </div>

        <input type="hidden"
               name="${_csrf.parameterName}"
               value="${_csrf.token}" />

    </form>

    <div>
        <p>Подсказка...</p>
        <p>username = <b>obender</b> password = <b>go2rio</b>  для роли LOGIST</p>
        <p>username = <b>akozlevich</b> password = <b>antilopa</b>  для роли DRIVER</p>
        <p>username = <b>abalaganov</b> password = <b>suhar34</b>  для роли DRIVER</p>
    </div>

</div>


<jsp:include page="../_fragments/page-template-after-main.jsp"/>
