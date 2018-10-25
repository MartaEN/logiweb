$('.table-filters input').on('input', function () {
    filterTable($(this).parents('table'));
});

$('.table-filters .select-filter-input').on('input', function () {
    filterTable($(this).parents('table'));
});

function filterTable($table) {
    let $filters = $table.find('.table-filters td');
    let $rows = $table.find('.table-data');
    $rows.each(function (rowIndex) {
        let valid = true;
        $(this).find('td').each(function (colIndex) {
            if ($filters.eq(colIndex).find('input').val()) {
                if ($(this).html().toLowerCase().indexOf(
                    $filters.eq(colIndex).find('input').val().toLowerCase()) === -1) {
                    valid = valid && false;
                }
            }
            if ($filters.eq(colIndex).find('.select-filter-input').val() &&
                $filters.eq(colIndex).find('.select-filter-input').val() !== 'all' ) {
                if ($(this).html().toLowerCase().indexOf(
                    $filters.eq(colIndex).find('.select-filter-input').val().toLowerCase()) === -1) {
                    valid = valid && false;
                }
            }
        });
        if (valid === true) {
            $(this).css('display', '');
        } else {
            $(this).css('display', 'none');
        }
    });
}