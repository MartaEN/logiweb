$(document).ready(function () {
    $('#findDriversBtn').on('click', (event) => {

        let ticketId = $('#findDriversBtn').attr('property');
        event.preventDefault();

        $.ajax({
            url: `/drivers/find-drivers?ticketId=` + ticketId,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                showDriverList(data);
            },
            error: function (err) {
                console.log(err);
            }
        });
    })
});

function showDriverList(drivers) {
    let $approvalBlock = $('#approvalBlock');
    let $searchResult = $('#driversSearchResult');
    let ticketId = $('#findDriversBtn').attr('property');
    let url = `/tickets/` + ticketId + `/approve`;
    $searchResult.html("");
    let output = '';
    if(drivers.length === 0) {
        output +='<p><b>Не удалось найти свободных водителей в городе отправления</b></p>';
    } else {
        output += '<p>Найдено водителей: ' + drivers.length + '</p>';
        output += '<form method="post" action="' + url + '">'
        output += '<p>Выберите водителей на маршрут:</p>';
        output += '<select class="custom-select col-sm-6" name="personalId" size="4" multiple>';
        drivers.forEach(function (driver) {
            output += '<option value="' + driver.personalId +'">'+ driver.personalId +' ' + driver.firstName + ' ' + driver.lastName + '</option>'
        });
        output += '</select><br>';
        output += '<div class="modal-footer">';
        output += '<a class="btn btn-secondary" href="/orders" role="button">Отменить</a>';
        output += ' <input id="approveTicketBtn" type="submit" class="btn btn-success" value="Назначить водителей и утвердить маршрутный лист"></div>';
    }
    $searchResult.append(output);
    $approvalBlock.show();
}