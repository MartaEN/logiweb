<%@ page contentType="text/html;charset=UTF-8"%>
<script id="template-orders-summary" type="text/x-handlebars-template">

<div class="order-list">

{{debug}}

{{#if this.orderLines}}

<table id="orders-table filter-table" class="table table-hover">
<thead>
<tr>
    <th class="width120">Пункт отправления</th>
    <th class="width120">Пункт назначения</th>
    <th class="width120">Расстоя&shyние, км</th>
    <th class="width80">Всего заказов</th>
    <th class="width120">Дата</th>
    <th class="width80">Вес, кг</th></th>
    <th class="width20"></th>
</tr>
<tr class="table-filters">
    <td class="width120"><input class="filter-input" type="text"/></td>
    <td class="width120"><input class="filter-input" type="text"/></td>
    <td class="width120"></td>
    <td class="width120"></td>
    <td class="width80">
        <select id="order-date-select" name="orderDate">
            <option value="all">Все даты</option>
            {{#each openDates}}
            <option value="{{this}}" {{#ifEquals this ../dateFilter}}selected{{/ifEquals}}>{{this}}</option>
            {{/each}}
        </select>
    </td>
    <td class="width20"></td>
</tr>
</thead>

<tbody>

    {{#each orderLines}}

    <tr class="table-data draggable source" property="fromCity={{fromCity.id}}&toCity={{toCity.id}}&date={{#if date}}{{date}}{{else}}all{{/if}}" draggable=true>
        <td class="width120">{{fromCity.name}}</td>
        <td class="width120">{{toCity.name}}</td>
        <td class="width120">{{distance}}</td>
        <td class="width80">{{numberOfOrders}}</td>
        <td class="width120">{{#if date}}{{date}}{{else}}все даты{{/if}}</td>
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