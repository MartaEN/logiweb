$(document).ready(function(){
    handlebarsHelpers();
    ajaxRequestOrdersSummaryList("all");
    ajaxRequestTicketsList();
});

function ajaxRequestOrdersSummaryList(date) {
    $.ajax({
        url: `/orders/view/unassigned?fromCity=all&toCity=all&date=` + date,
        type: 'GET',
        dataType: 'json',
        cache: false,
        success: function (data) {
            fillOrdersSummarySection(data);
        },
        error: function (err) {
            console.log(err);
        }
    });
}

function ajaxRequestOrdersDrilldownList(params) {
    $.ajax({
        url: `/orders/view/unassigned?` + params,
        type: 'GET',
        dataType: 'json',
        cache: false,
        success: function (data) {
            fillOrdersDrilldownSection(data);
        },
        error: function (err) {
            console.log(err);
        }
    });
}

function ajaxRequestTicketsList() {
    $.ajax({
        url: `/tickets/list/unapproved`,
        type: 'GET',
        dataType: 'json',
        cache: false,
        success: function (data) {
            fillTripTicketsSection(data);
        },
        error: function (err) {
            console.log(err);
        }
    });
}

function ajaxRequestAddOneOrderToTicket(orderParams, ticketParams) {
    $.ajax({
        url: `/orders/add-single-order-to-ticket?` + orderParams + `\&` + ticketParams,
        type: 'GET',
        async: false,
        dataType: 'json',
        cache: false,
        success: function (data) {
            console.log(data);
            showOperationResultMessage(data);
        },
        error: function (err) {
            console.log(err);
        }
    });
}

function ajaxRequestAddMultipleOrdersToTicket(orderParams, ticketParams) {
    $.ajax({
        url: `/orders/add-multiple-orders-to-ticket?` + orderParams + `\&` + ticketParams,
        type: 'GET',
        async: false,
        dataType: 'json',
        cache: false,
        success: function (data) {
            console.log(data);
            showOperationResultMessage(data);
        },
        error: function (err) {
            console.log(err);
        }
    });
}

function fillOrdersSummarySection(orders) {
    let source   = document.getElementById("template-orders-summary").innerHTML;
    let template = Handlebars.compile(source);
    $('#order-list').html(template(orders));
    addFilterTableListening();
    addDragAndDropListening();
    addDrillDownByDoubleClickListening();
    addSelectDateListening();
}

function fillOrdersDrilldownSection(orders) {
    let source   = document.getElementById("template-orders-drilldown").innerHTML;
    let template = Handlebars.compile(source);
    $('#order-list').html(template(orders));
    addFilterTableListening();
    addDragAndDropListening();
    addCancelDropdownListening();
}

function fillTripTicketsSection(tickets) {
    let source   = document.getElementById("ticket-list-template").innerHTML;
    let template = Handlebars.compile(source);
    $('#ticket-list').html(template(tickets));
    addDragAndDropListening();
}

function showOperationResultMessage(message) {
    $('#modalMessage .modal-title').text(message.title);
    $('#modalMessage .modal-body').text(message.body);
    $('#modalMessage').modal();
}

function addFilterTableListening() {
    $('.table-filters input').on('input', function () {
        filterTable($(this).parents('table'));
    });
    $('.table-filters .select-filter-input').on('input', function () {
        filterTable($(this).parents('table'));
    });
}

function addDrillDownByDoubleClickListening() {
    let items = $('.draggable');
    for(let i = 0; i < items.length; i++) {
        items[i].addEventListener('dblclick', (event) => {
            let params = event.currentTarget.getAttribute('property');
            ajaxRequestOrdersDrilldownList(params)
        });
    }
}

function addCancelDropdownListening() {
    $('#cancel-dropdown').on('click', (event) => {
        event.preventDefault();
        ajaxRequestOrdersSummaryList("all");
    });
}

function addSelectDateListening() {
    $('#order-date-select').on('input', (event) => {
        let selectedDate = $('#order-date-select')[0].value;
        ajaxRequestOrdersSummaryList(selectedDate);
    });
}

function addDragAndDropListening() {

    let sources = $('.source');
    let targets = $('.target');
    let currentDrag;

    for (let i = 0; i < sources.length; i++) {
        if(sources[i].ondragstart == null) {
            sources[i].addEventListener('dragstart', (event) => {
                currentDrag = {source: sources[i], node: event.target};
            });
        }
    }

    for (let i = 0; i < targets.length; i++) {

        if(targets[i].ondragstart == null) {
            targets[i].addEventListener('dragover', (event) => {
                event.preventDefault();
            });
        }

        if(targets[i].ondrop == null) {
            targets[i].addEventListener('drop', (event) => {
                if (currentDrag) {

                    event.preventDefault();

                    let orderParam = currentDrag.source.getAttribute('property');
                    let ticketParam = event.currentTarget.getAttribute('property');

                    if(orderParam.startsWith("orderId")) {
                        ajaxRequestAddOneOrderToTicket(orderParam, ticketParam);
                        let currentCityFilterParam = $('#cancel-dropdown')[0].getAttribute("property");
                        let currentDateFilterParam = $('#order-date-select')[0].value;
                        ajaxRequestOrdersDrilldownList(currentCityFilterParam + '&date=' + currentDateFilterParam);
                        ajaxRequestTicketsList();
                    } else {
                        ajaxRequestAddMultipleOrdersToTicket(orderParam, ticketParam);
                        ajaxRequestOrdersSummaryList("all");
                        ajaxRequestTicketsList();
                    }

                    currentDrag = null;
                }
            });
        }
    }
}