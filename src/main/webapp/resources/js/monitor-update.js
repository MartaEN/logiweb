$(document).ready(function(){
    $.ajax({
        url: `/orders/monitor`,
        type: 'GET',
        dataType: 'json',
        cache: false,
        success: function (data) {
            console.log(data);
            handlebarsHelpers();
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
    let source   = document.getElementById("order-list-template").innerHTML;
    let template = Handlebars.compile(source);
    $('#order-list').html(template(orders));

}

function fillTripTicketsSection(tickets) {
    let source   = document.getElementById("ticket-list-template").innerHTML;
    let template = Handlebars.compile(source);
    $('#ticket-list').html(template(tickets));
}

function showErrorMessage(error) {
    $('#errorMsgModal .modal-body').text(error);
    $('#errorMsgModal').modal();
}

function addDragAndDropListening() {

    let sources = $('.source');
    let targets = $('.target');
    let currentDrag;

    for (let i = 0; i < sources.length; i++) {
        sources[i].addEventListener('dragstart', (event) => {
            currentDrag = {source: sources[i], node: event.target};
        });
    }

    for (let i = 0; i < targets.length; i++) {
        targets[i].addEventListener('dragover', (event) => {
            event.preventDefault();
        });

        targets[i].addEventListener('drop', (event) => {
            if (currentDrag) {

                event.preventDefault();

                let orderId = currentDrag.source.getAttribute('property');
                let ticketId = event.currentTarget.getAttribute('property');

                $.ajax({
                    url: `/orders/add-order-to-ticket?orderId=` + orderId + `\&ticketId=` + ticketId,
                    type: 'GET',
                    dataType: 'json',
                    cache: false,
                    success: function (data) {
                        console.log(data);
                        fillOrdersSection(data.orders);
                        fillTripTicketsSection(data.tickets);
                        if(data.error !== null) showErrorMessage(data.error);
                        addDragAndDropListening();
                    },
                    error: function (err) {
                        console.log(err);
                    }
                });

                currentDrag = null;
            }
        });
    }
}



