$(document).ready(function () {

    $('#viewOrderModal').on('shown.bs.modal', function () {

        let id = $('#orderId')[0].value;

        $.ajax({
            url: `/orders/show-order?id=` + id,
            type: 'GET',
            dataType: 'json',
            cache: false,
            success: function (data) {
                console.log(data);
                showOrder(data);
            },
            error: function (err) {
                console.log(err);
                showOrder(null);
            }
        });

    })
});

function showOrder(order) {
    handlebarsHelpers();
    let source   = document.getElementById("template-order").innerHTML;
    let template = Handlebars.compile(source);
    $('#viewOrderDataPlaceholder').html(template(order));
}