<%@ page contentType="text/html;charset=UTF-8"%>
<script id="order-list-template" type="text/x-handlebars-template">

<div class="order-list">

{{debug}}

{{#if this}}

<table id="filter-table" class="table table-hover">
<thead>
<tr>
    <th class="width40">№</th>
    <th class="width100">Дата</th>
    <th class="width100">Погрузка</th>
    <th class="width100">Выгрузка</th>
    <th class="width80">Вес, кг</th>
</tr>
<tr class="table-filters">
    <td><input type="text" class="width40"/></td>
    <td><input type="text" class="width100"/></td>
    <td><input type="text" class="width100"/></td>
    <td><input type="text" class="width100"/></td>
    <td><input type="text" class="width80"/></td>
</tr>
</thead>


    {{#each this}}

    <tr class="table-data draggable source" property="{{id}}" draggable=true>
        <td>{{id}}</td>
        <td class="width100">{{{trimDate creationDate}}}</td>
        <td>{{fromCity.name}}</td>
        <td>{{toCity.name}}</td>
        <td>{{weight}}</td>
    </tr>

    {{/each}}


    </table>
    <script src="resources/js/filter-table.js"/>


{{else}}

<p>Новых заказов пока нет</p>

{{/if}}

</div>


</script>