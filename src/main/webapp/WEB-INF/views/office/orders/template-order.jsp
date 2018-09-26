<%@ page contentType="text/html;charset=UTF-8"%>
<script id="template-order" type="text/x-handlebars-template">

{{debug}}

{{#if this}}

<form>
    <div class="form-row">
        <div class="form-group col-md-3">
            <label for="id">Номер</label>
            <input disabled id="id" class="form-control" value="{{id}}"/>
        </div>
        <div class="form-group col-md-5">
            <label for="creationDate">Дата</label>
            <input disabled id="creationDate" class="form-control" value="{{{trimDate creationDate}}}"/>
        </div>
        <div class="form-group col-md-4">
            <label for="status">Статус</label>
            <input disabled id="status" class="form-control" value="{{status}}"/>
        </div>
    </div>
    <div class="form-row">
        <div class="form-group col-sm-6">
            <label for="fromCity">Пункт отправления</label>
            <input disabled id="fromCity" class="form-control" value="{{fromCity.name}}"/>
        </div>
        <div class="form-group col-sm-6">
            <label for="toCity">Пункт назначения</label>
            <input disabled id="toCity" class="form-control" value="{{toCity.name}}"/>
        </div>
    </div>
    <div class="form-group">
        <label for="description">Описание груза</label>
        <textarea id="description" rows="3" disabled class="form-control">{{description}}</textarea>
    </div>
    <div class="form-group">
        <label for="weight">Вес груза, кг</label>
        <input type="number" id="weight" disabled class="form-control" value="{{weight}}"/>
    </div>

</form>

{{else}}

<p>Заказ не найден</p>

{{/if}}

</script>