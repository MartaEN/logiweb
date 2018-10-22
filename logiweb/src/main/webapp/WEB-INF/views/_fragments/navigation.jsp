<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<nav id="mainNav" class="sideNav">
    <security:authorize access="hasRole('LOGIST')">
        <a href="/orders" data-toggle="tooltip" data-placement="right" title="<spring:message code='menu.core'/>"><i class="fas fa-luggage-cart"></i></a>
        <a data-toggle="collapse" href="#reports" role="button" aria-expanded="false" aria-controls="reports"><i class="fas fa-chart-line"></i></a>
        <div id="reports" class="collapse">
            <a href="/orders/view?page=1" data-toggle="tooltip" data-placement="right" title="<spring:message code='menu.orders'/>"><i class="fas fa-box"></i></a>
            <a href="/tickets/view" data-toggle="tooltip" data-placement="right" title="<spring:message code='menu.tickets'/>"><i class="fas fa-route"></i></a>
        </div>
        <a data-toggle="collapse" href="#settings" role="button" aria-expanded="false" aria-controls="settings"><i class="fas fa-cog"></i></a>
        <div id="settings" class="collapse">
            <a href="/destinations" data-toggle="tooltip" data-placement="right" title="<spring:message code='menu.cities'/>"><i class="fas fa-map-marked-alt"></i></a>
            <a href="/trucks" data-toggle="tooltip" data-placement="right" title="<spring:message code='menu.trucks'/>"><i class="fas fa-truck-moving"></i></a>
            <a href="/drivers" data-toggle="tooltip" data-placement="right" title="<spring:message code='menu.drivers'/>"><i class="fas fa-users"></i></a>
        </div>
    </security:authorize>
    <security:authorize access="hasRole('DRIVER')">
        <a href="/logiweb"><i class="fas fa-map-marked-alt"></i></a>
    </security:authorize>
</nav>
