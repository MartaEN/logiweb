<%@ page contentType="text/html;charset=UTF-8"%>
<script id="template-drivers-view" type="text/x-handlebars-template">

{{debug}}

{{#if this}}

<div class="tab-content" id="nav-tabContent">

    <%--instruction tab--%>
    <div class="tab-pane fade show active" id="driver-core-screen__instruction" role="tabpanel">
        <div class="card card-body">
            {{#if alert}}
                <div class="alert alert-danger" role="alert">{{alert}}</div>
            {{/if}}
            <div>
                {{#if currentStop}}
                <h3>{{currentStop.name}}</h3>
                {{/if}}
            </div>
            <div id="instruction" class="alert alert-dark">
                {{directiveMessage}}
            </div>
            <div id="additionalInfo">
                {{#if orders}}
                <table class="table">
                    <thead>
                    <th>№</th>
                    <th>Наимено&shyвание</th>
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
        </div>

        {{#if requestedActionMessage}}
            <form name="actionForm" action="logiweb/{{url}}" method="POST">
                <button class="action-button btn btn-success" type="submit">{{requestedActionMessage}}</button>
                <input name="ticketId" type="hidden" value="{{ticket.id}}"/>
                <input name="targetStep" type="hidden" value="{{targetStep}}"/>
            </form>
        {{/if}}

    </div>

    <%-- ticket info tab --%>
    <div class="tab-pane fade" id="driver-core-screen__ticket-info" role="tabpanel">
        <div class="card card-body">
            {{#if ticket}}
            <h5>Маршрутный лист №{{ticket.id}}</h5>
            <hr>
            <p><i class="far fa-calendar-alt"></i> {{{trimDate ticket.departureDateTime}}} <i class="far fa-clock"></i> {{{trimTime ticket.departureDateTime}}} <i class="fas fa-map-marker-alt"></i> {{ticket.stopovers.0.city.name}}</p>
            <p><i class="fas fa-truck-moving fa-flip-horizontal"></i> {{ticket.truck.regNumber}} {{ticket.truck.capacity}}кг</p>
            <hr>
            {{#each ticket.drivers}}
            <p><i class="fas fa-user"></i> {{firstName}} {{lastName}}</p>
            {{/each}}
            <hr>
            {{#each ticket.stopoversSorted}}
            <p><i class="fas fa-parking"></i> {{city.name}}</p>
            {{/each}}
            {{/if}}
        </div>
    </div>

    <%-- driver status tab --%>
    <div id="driver-core-screen__driver-status" class="tab-pane fade" role="tabpanel">
        <div class="card card-body">
            <p>Ваш текущий статус:</p>
            <h5>
            {{#ifEquals driverStatus "DRIVING"}} За рулём {{/ifEquals}}
            {{#ifEquals driverStatus "SECONDING"}} Второй водитель {{/ifEquals}}
            {{#ifEquals driverStatus "HANDLING"}} Погрузочные работы {{/ifEquals}}
            {{#ifEquals driverStatus "ROAD_BREAK"}} Стоянка {{/ifEquals}}
            {{#ifEquals driverStatus "STOPOVER_BREAK"}} Перерыв {{/ifEquals}}
            {{#ifEquals driverStatus "OFFLINE"}} Не в сети {{/ifEquals}}
            {{#ifEquals driverStatus "WAITING"}} Ожидание напарника {{/ifEquals}}
            </h5>
        </div>
        {{#ifEquals driverStatus "DRIVING"}}
            <form name="changeStatusForm" action="logiweb/road-break" method="POST">
                <button class="change-status-button btn btn-success" type="submit">Стоянка для отдыха</button>
                <input name="ticketId" type="hidden" value="{{ticket.id}}"/>
                <input name="targetStep" type="hidden" value="{{targetStep}}"/>
            </form>
        {{/ifEquals}}
        {{#ifEquals driverStatus "ROAD_BREAK"}}
        <form name="changeStatusForm" action="logiweb/road-break-over" method="POST">
            <button class="change-status-button btn btn-success" type="submit">Завершить стоянку</button>
            <input name="ticketId" type="hidden" value="{{ticket.id}}"/>
            <input name="targetStep" type="hidden" value="{{targetStep}}"/>
        </form>
        {{/ifEquals}}
        {{#ifEquals driverStatus "SECONDING"}}
            <form name="changeStatusForm" action="logiweb/first-driver" method="POST">
                <button class="change-status-button btn btn-success" type="submit">На место водителя</button>
                <input name="ticketId" type="hidden" value="{{ticket.id}}"/>
                <input name="targetStep" type="hidden" value="{{targetStep}}"/>
            </form>
        {{/ifEquals}}
        {{#ifEquals driverStatus "HANDLING"}}
            <form name="changeStatusForm" action="logiweb/stopover-break" method="POST">
                <button class="change-status-button btn btn-success" type="submit">Уйти на перерыв</button>
                <input name="ticketId" type="hidden" value="{{ticket.id}}"/>
                <input name="targetStep" type="hidden" value="{{targetStep}}"/>
            </form>
        {{/ifEquals}}
        {{#ifEquals driverStatus "STOPOVER_BREAK"}}
            <form name="changeStatusForm" action="logiweb/stopover-break-over" method="POST">
                <button class="change-status-button btn btn-success" type="submit">Завершить перерыв</button>
                <input name="ticketId" type="hidden" value="{{ticket.id}}"/>
                <input name="targetStep" type="hidden" value="{{targetStep}}"/>
            </form>
        {{/ifEquals}}
    </div>

</div>

{{else}}

<p>Ошибка - перезагрузите страницу</p>

{{/if}}

</script>