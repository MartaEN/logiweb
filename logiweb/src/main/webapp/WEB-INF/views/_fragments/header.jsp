<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<header class="d-flex bd-highlight align-items-center">

    <div class="logo p-2 flex-grow-1 bd-highlight">LogiWeb</div>

        <div class="p-2 bd-highlight">
            <security:authorize access="isAuthenticated()">
                <span><security:authentication property="principal.username" /></span>
            </security:authorize>
            <i class="fas fa-user d-inline-flex"></i>
            <security:authorize access="isAuthenticated()">
                <form:form class="d-inline-flex logout" action="${pageContext.request.contextPath}/logout" method="POST">
                    <input type="submit" value="<spring:message code='security.logout'/>" class="btn btn-link logout"/>
                </form:form>
            </security:authorize>
        </div>

    <div class="p-2 bd-highlight">
        <a href="${pageContext.request.contextPath}?locale=en">EN</a>
        <a href="${pageContext.request.contextPath}?locale=ru">RU</a>
    </div>

</header>
