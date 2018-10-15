function handlebarsHelpers(){
    Handlebars.registerHelper("debug", function(optionalValue) {
        console.log("=====Handelbars Context=====");
        console.log(this);
    });
    Handlebars.registerHelper("trimDate", function(passedString) {
        let theString = passedString.substring(0,10);
        return new Handlebars.SafeString(theString)
    });
    Handlebars.registerHelper("trimTime", function(passedString) {
        let theString = passedString.substring(11,16);
        return new Handlebars.SafeString(theString)
    });
    Handlebars.registerHelper("showDrivers", function(n) {
        let result = '';
        for (var i = n; i > 0; i--) {
            result += '<i class="fas fa-user"></i>';
        }
        return result;
    });
    Handlebars.registerHelper('ifEquals', function(arg1, arg2, options) {
        return (arg1 === arg2) ? options.fn(this) : options.inverse(this);
    });
}