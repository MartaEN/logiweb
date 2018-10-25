$(document).ready(function () {

    handlebarsHelpers();

    $('#search-order-btn')[0].addEventListener('click', (event) => {
        event.preventDefault();
        let id = $('#orderId')[0].value;
        ajaxRequestOrderData(id);
    });

    let links = $('.view-order-link');

    for (let i = 0; i < links.length; i++) {
        links[i].addEventListener('click', (event) => {
            event.preventDefault();
            let id = links[i].getAttribute("property");
            ajaxRequestOrderData(id);
        });
    }
});

function ajaxRequestOrderData(id) {
    $.ajax({
        url: `/orders/show-order?id=` + id,
        type: 'GET',
        dataType: 'json',
        cache: false,
        success: function (data) {
            showOrder(data);
        },
        error: function (err) {
            showOrder(null);
        }
    });
}

function showOrder(order) {
    let source   = document.getElementById("template-order").innerHTML;
    let template = Handlebars.compile(source);
    $('#viewOrderDataPlaceholder').html(template(order));
    $('#viewOrderModal').modal();
}