let token = $("meta[name='_csrf']")[0].content;
let header = $("meta[name='_csrf_header']")[0].content;
$(document).ajaxSend(function(e, xhr, options) {
    xhr.setRequestHeader(header, token);
});