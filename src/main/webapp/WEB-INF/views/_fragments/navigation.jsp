<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<nav id="mainNav" class="sideNav">
    <security:authorize access="hasRole('LOGIST')">
        <a href="/orders"><i class="fas fa-luggage-cart"></i></a>
        <a href="/orders/view?page=1"><i class="fas fa-chart-line"></i></a>
        <a href="/trucks"><i class="fas fa-truck-moving"></i></a>
        <a href="/drivers"><i class="fas fa-users"></i></a>
        <a href="/destinations"><i class="fas fa-map-marked-alt"></i></a>
    </security:authorize>
</nav>
