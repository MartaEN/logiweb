<%@ page contentType="text/html;charset=UTF-8"%>
<script id="template-orders-drilldown" type="text/x-handlebars-template">

<div class="order-list">

{{debug}}

{{#if this}}

<div class="comment-line">
    <a href="#" id="cancel-dropdown" property="{{citiesFilterParams}}" class="badge badge-pill badge-success">{{citiesFilter}} <i class="fas fa-times-circle"></i></a>
</div>

<table id="orders filter-table" class="table table-hover">
<thead>
<tr>
    <th class="width120">Пункт отправления</th>
    <th class="width120">Пункт назначения</th>
    <th class="width120">Расстоя&shyние, км</th>
    <th class="width80">№ заказа</th>
    <th class="width120">Дата</th>
    <th class="width80">Вес, кг</th></th>
    <th class="width20"></th>
</tr>
<tr class="table-filters">
    <td class="width120"></td>
    <td class="width120"></td>
    <td class="width120"></td>
    <td class="width80"></td>
    <td class="width120">
        <select id="order-date-select" class="select-filter-input" name="orderDate">
            <option value="all">Все даты</option>
            {{#each openDates}}
            <option value="{{this}}" {{#ifEquals this ../dateFilter}}selected{{/ifEquals}}>{{this}}</option>
            {{/each}}
        </select>
    </td>
    <td class="width80"></td>
    <td class="width20"></td>
</tr>
</thead>

<tbody>

    {{#each orderLines}}

    <tr class="table-data draggable source" property="orderId={{orderId}}" draggable=true>
        <td class="width120">{{fromCity.name}}</td>
        <td class="width120">{{toCity.name}}</td>
        <td class="width120">{{distance}}</td>
        <td class="width80">{{orderId}}</td>
        <td class="width120">{{date}}</td>
        <td class="width80">{{totalWeight}}</td>
    </tr>

    {{/each}}

</tbody>

</table>

{{else}}

<p>Новых заказов пока нет</p>

{{/if}}

</div>


</script>