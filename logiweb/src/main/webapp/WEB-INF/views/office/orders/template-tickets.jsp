<%@ page contentType="text/html;charset=UTF-8"%>

<script id="ticket-list-template" type="text/x-handlebars-template">

<div class="ticket-list">

{{debug}}

{{#if this}}

    {{#each this}}

    <a href="/tickets/{{id}}/approve" class="ticket open-ticket target row-wrapper" property="ticketId={{id}}">
        <div class="ticket-truck-info">
            <p><i class="fas fa-truck-moving fa-flip-horizontal"></i> {{truck.regNumber}}</p>
            <p><i class="fas fa-truck-loading"></i> {{truck.capacity}} кг</p>
            <p><i class="fas fa-luggage-cart"></i> <b><big><big><big>{{avgLoad}}%</big></big></big></b></p>
        </div>
        <div class="ticket-departure">
            <i class="far fa-calendar-alt"></i> {{{trimDate departureDateTime}}} <i class="far fa-clock"></i> {{{trimTime departureDateTime}}}
        </div>
        <div class="ticket-route-info">
            {{#stopoversSorted}}
                {{city.name}}({{totalWeight}})
            {{/stopoversSorted}}
        </div>
    </a>

    {{/each}}

{{else}}

<p>Неутвержденных маршрутных листов нет</p>

{{/if}}

</div>


</script>