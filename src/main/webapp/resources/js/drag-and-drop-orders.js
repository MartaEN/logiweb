const source = document.querySelector('.source');
const target = document.querySelector('.target');

makeDnD([source, target]);

function makeDnD(zones) {

    let currentDrag;

    zones.forEach(zone => {
        zone.addEventListener('dragstart', (e) => {
            currentDrag = {source: zone, node: e.target};
        });

        zone.addEventListener('dragover', (e) => {
            e.preventDefault();
        });

        zone.addEventListener('drop', (e) => {
            if (currentDrag) {
                e.preventDefault();

                if (currentDrag.source !== zone) {
                    let orderId = currentDrag.attr('property');
                    let ticketId = zone.attr('property');
                    $.ajax({
                        //todo как собрать данные с вьюшки для отправки запроса
                        url: `/orders/add-order-to-ticket?orderId=` + orderId + `\&ticketId=` + ticketId,
                        type: 'GET',
                        dataType: 'json',
                        cache: false,
                        success: function (data) {
                            console.log(data);
                            fillOrdersSection(data.orders);
                            fillTripTicketsSection(data.tickets);
                        },
                        error: function (err) {
                            console.log(err);
                        }
                    });

                    currentDrag = null;
                }
            }
        });
    });
}

//todo и как корректно сгенерировать html код из js...
function fillOrdersSection(orders) {
    let section = $('#orders');
    section.html("");
    section.append('<h3>Заказы</h3>');
    if (orders == 0) section.append('<p>Новых заказов нет</p>');
    else {
        section.append('<table id="filter-table">');
        section.append('<tr>');
        section.append('<th class="width40">№</th>');
        section.append('<th class="width100">Дата</th>');
        section.append('<th class="width100">Погрузка</th>');
        section.append('<th class="width100">Выгрузка</th>');
        section.append('<th class="width80">Вес, кг</th>');
        section.append('</tr>');
        section.append('<tr class="table-filters">');
        section.append('<td ><input type="text" class="width40"/></td>');
        section.append('<td ><input type="text" class="width100"/></td>');
        section.append('<td ><input type="text" class="width100"/></td>');
        section.append('<td ><input type="text" class="width100"/></td>');
        section.append('<td ><input type="text" class="width80"/></td>');
        section.append('</tr>');
        orders.forEach(function (order) {
            section.append('<tr class="table-data draggable source" draggable=true>');
            section.append('<td>order["id"]</td>');
            section.append('<td class="width100">order["creationDate"]</td>');
            section.append('<td>order["fromCity"]["name"]</td>');
            section.append('<td>order["toCity"]["name"]</td>');
            section.append('<td>order["weight"]</td>');
            section.append('</tr>');
        });
        section.append('</table>');
        section.append('<br>');
        section.append('<a class="btn btn-success" href="/orders/add" role="button">Зарегистрировать новый заказ</a>');
    }
}

function fillTripTicketsSection(tickets) {
    let section = $('#tickets');
    section.html("");
    section.append("Trip tickets data will come here");
}

