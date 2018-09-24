$(document).ready(function(){
    $.ajax({
        url: `/orders/monitor`,
        type: 'GET',
        dataType: 'json',
        cache: false,
        success: function (data) {
            console.log(data);
            fillOrdersSection(data.orders);
            fillTripTicketsSection(data.tickets);
            addDragAndDropListening();
        },
        error: function (err) {
            console.log(err);
        }
    });
});

function fillOrdersSection(orders) {
    handlebarsHelpers();
    let source   = document.getElementById("order-list-template").innerHTML;
    let template = Handlebars.compile(source);
    $('#order-list').html(template(orders));

}

function fillTripTicketsSection(tickets) {
    handlebarsHelpers();
    let source   = document.getElementById("ticket-list-template").innerHTML;
    let template = Handlebars.compile(source);
    $('#ticket-list').html(template(tickets));
}

//todo вешает слушатели только на первый заказ и первый маршрутный лист в списке - исправить
function addDragAndDropListening() {
    const source = document.querySelector('.source');
    const target = document.querySelector('.target');
    processDragAndDrop([source, target]);
}

function processDragAndDrop(zones) {

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
                    let orderId = currentDrag.source.getAttribute('property');
                    let ticketId = e.currentTarget.getAttribute('property');
                    $.ajax({
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



