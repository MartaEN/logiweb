$(document).ready(function () {
    $('#findRouteBtn').on('click', (event) => {

        event.preventDefault();

        let params = $('#findRouteForm').serialize();

        $.ajax({
            url: `/destinations/find-route-result?${params}`,
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
                $searchResult.append('<br><p>Общее расстояние: <b>' + $distance + ' км</b></p>');
                $searchResult.show();
            },
            error: function (err) {
                console.log(err);
            }
        });
    })
});