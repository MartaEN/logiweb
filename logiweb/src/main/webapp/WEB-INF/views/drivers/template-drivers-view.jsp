<%@ page contentType="text/html;charset=UTF-8"%>
<script id="template-drivers-view" type="text/x-handlebars-template">

<div class="driver-view">

{{debug}}

{{#if this}}

    <p>{{driverStatus}}</p>
    <div>
        {{#if currentStop}}
            <h3>{{currentStop.name}}</h3>
        {{/if}}
    </div>
    <div class="alert alert-warning">
        {{directiveMessage}}
    </div>
    <div>
        {{#if orders}}
            <table >
                <thead>
                <th>№</th>
                <th>Наименование</th>
                <th>Вес, кг</th>
                <th>Пункт назначения</th>
                </thead>
                <tbody>

                {{#each orders}}
                    <tr>
                        <td>{{id}}</td>
                        <td>{{description}}</td>
                        <td>{{weight}}</td>
                        <td>{{toCity.name}}</td>
                    </tr>
                {{/each}}
                </tbody>
            </table>
        {{/if}}
    </div>
    <div class="driver-ui-main-button">
        {{#if requestedActionMessage}}
            <form name="actionForm" action="logiweb/{{url}}" method="POST">
                <button id="actionBtn" class="btn btn-success" type="submit">{{requestedActionMessage}}</button>
                <input name="ticketId" type="hidden" value="{{ticket.id}}"/>
                <input name="targetStep" type="hidden" value="{{targetStep}}"/>
            </form>
        {{/if}}
    </div>
    <br><br>
    <div>
        {{#if ticket}}
            <h5>Маршрутный лист №{{ticket.id}}</h5>
            <p><i class="far fa-calendar-alt"></i> {{{trimDate ticket.departureDateTime}}} <i class="far fa-clock"></i> {{{trimTime ticket.departureDateTime}}} <i class="fas fa-map-marker-alt"></i> {{ticket.stopovers.0.city.name}}</p>
            <p><i class="fas fa-truck-moving fa-flip-horizontal"></i> {{ticket.truck.regNumber}} {{ticket.truck.capacity}}кг</p>
            {{#each ticket.drivers}}
                <p><i class="fas fa-user"></i> {{firstName}} {{lastName}}</p>
            {{/each}}
            {{#each ticket.stopovers}}
                <p><i class="fas fa-parking"></i> {{city.name}}</p>
            {{/each}}
        {{/if}}
    </div>

{{else}}

<p>Ошибка - перезагрузите страницу</p>

{{/if}}

</div>


</script>