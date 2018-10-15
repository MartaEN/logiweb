let stompClient = null;

$(document).ready(function(){
    handlebarsHelpers();
    fetchInitialData();
    connect();

});

function fetchInitialData() {
    $.ajax({
        url: `/logiweb/instruction`,
        type: 'GET',
        dataType: 'json',
        cache: false,
        success: function (data) {
            render(data);
        },
        error: function (err) {
            console.log(err);
        }
    });
}

function connect() {
    let socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/logiweb/updates', function (message) {
            console.log("received some update from logiweb!!!");
            render(JSON.parse(message.body));
        });
    });
}

function render(instruction) {
    console.log("Rendering instruction");
    let source   = document.getElementById("template-drivers-nav").innerHTML;
    let template = Handlebars.compile(source);
    $('#mainNav').html(template(instruction));
    source   = document.getElementById("template-drivers-view").innerHTML;
    template = Handlebars.compile(source);
    $('#driversView').html(template(instruction));
    addActionFormListener();
}

function addActionFormListener() {
    $('form[name=actionForm]').on('submit', function (e) {
        e.preventDefault();
    });
    $('form[name=changeStatusForm]').on('submit', function (e) {
        e.preventDefault();
    });
    $( ".action-button" ).click(function() {
        let actionForm = $('form[name=actionForm]');
        $.ajax({
            url: actionForm[0].action,
            type: actionForm[0].method,
            data : actionForm.serialize(),
            dataType: 'json',
            cache: false,
            success: function (data) {
                console.log(data);
                render(data);
            },
            error: function (err) {
                console.log(err);
            }
        });
    });
    $( ".change-status-button" ).click(function() {
        let actionForm = $('form[name=changeStatusForm]');
        $.ajax({
            url: actionForm[0].action,
            type: actionForm[0].method,
            data : actionForm.serialize(),
            dataType: 'json',
            cache: false,
            success: function (data) {
                console.log(data);
                render(data);
            },
            error: function (err) {
                console.log(err);
            }
        });
    });
}



