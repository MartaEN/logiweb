$(document).ready(function () {
    //todo как получить и дудочку, и кувшинчик?
    //то (1) есть сохранить binding к объекту RoadRecord и (2) сделать запрос ajax
    $('#findRouteBtn').on('click', (event) => {
        // todo почитать про стрелочные функции
        // todo add preventDefault???

        event.preventDefault();

        let fromCityId = document.querySelector('#fromCity').value;
        let toCityId = document.querySelector('#toCity').value;

        $.ajax({
            url: `/destinations/find-route-result?fromCityId=${fromCityId}\&toCityId=${toCityId}`,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                let $searchResult = $('#searchResult');
                $searchResult.html("");
                $searchResult.append('<p><b>Маршрут построен</b></p>');
                let $table = $('<table/>');
                let $distance = 0;
                $.each(data.route, function(index, road) {
                    $table.append('<tr><td>' + road.fromCity.name + '</td><td><i class="fas fa-arrow-right"></i></td><td>' + road.toCity.name + '</td><td>' + road.distance + ' км</td></tr>');
                    $distance += road.distance;
                });
                $searchResult.append($table);
                $searchResult.append('<br><p>Общее расстояние: <b>' + $distance + ' км</b></p>')
                $searchResult.show();
            },
            error: function (err) {
                console.log(err);
            }
        });
    })
});