<%@ page contentType="text/html;charset=UTF-8"%>
<script id="template-drivers-nav" type="text/x-handlebars-template">

{{#if this}}

<div class="list-group d-flex justify-content-between" id="list-tab" role="tablist">

    <a class="active" id="instruction-link" data-toggle="list" href="#driver-core-screen__instruction" role="tab"><i class="fas fa-map-marked-alt"></i></a>

    {{#if ticket}}
    <a class="" id="ticket-info-link" data-toggle="list" href="#driver-core-screen__ticket-info" role="tab"><i class="fas fa-file-invoice"></i></a>
    {{/if}}

    <div class="flex-grow-1"></div>

    <a class="align-self-end" id="driver-status-link" data-toggle="list" href="#driver-core-screen__driver-status" role="tab">
        {{#ifEquals driverStatus "DRIVING"}} <i class="fas fa-road"></i> {{/ifEquals}}
        {{#ifEquals driverStatus "SECONDING"}} <i class="fas fa-user-friends"></i> {{/ifEquals}}
        {{#ifEquals driverStatus "HANDLING"}} <i class="fas fa-truck-loading"></i> {{/ifEquals}}
        {{#ifEquals driverStatus "ROAD_BREAK"}} <i class="fas fa-tree"></i> {{/ifEquals}}
        {{#ifEquals driverStatus "STOPOVER_BREAK"}} <i class="fas fa-utensils"></i> {{/ifEquals}}
    </a>

</div>

{{/if}}

</script>