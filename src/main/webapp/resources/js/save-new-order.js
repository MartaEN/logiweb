$(document).ready(function () {

    $('#saveNewOrderBtn').on('click', (event) => {

        $.ajax({
            url: "/orders/add",
            type: "POST",
            data : $('form[name=orderEntryForm]').serialize(),
            dataType: 'json',
            success: function (data) {
                console.log("succes so far");
                console.log(data);
                alert("saved");
            },
            error: function (err) {
                console.log(err);
            }
        });
    })
});