$(document).ready(function () {
    $('#findTrucksBtn').on('click', (event) => {

        event.preventDefault();

        let params = $('#findTruckForm').serialize();

        $.ajax({
            url: `/trucks/find-truck-list?${params}`,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                showTruckList(data);
            },
            error: function (err) {
                console.log(err);
            }
        });
    })
});

function showTruckList (trucks) {
    let $searchResult = $('#trucksSearchResult');
    $searchResult.html("");
    let output = '';
    if(trucks.length === 0) {
        output +='<p><b>Не удалось найти фуры по Вашему запросу</b></p>';
    } else {
        output += '<p>Найдено фур: ' + trucks.length + '</p>';
        output += '<p>Выберите фуру для оформления маршрутного листа:</p>';
        output += '<select class="custom-select col-sm-6" name="truckRegNumber" size="4">';
        trucks.forEach(function (truck) {
            output += '<option value="' + truck.regNumber +'">'+ truck.regNumber +' ' + truck.capacity +' кг </option>'
        });
        output += '</select><br>';
        output += '<div class="modal-footer"><input id="createTicketBtn" type="submit" class="btn btn-success" value="Создать маршрутный лист"></div>';
    }
    $searchResult.append(output);
    $searchResult.show();
}
