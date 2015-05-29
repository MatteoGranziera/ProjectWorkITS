var mapeurope = $('map-europe ul li')
var provadati = $('#provadati')
$(function () {
    $("#map-europe ul li").each(function () {
        $(this).mouseenter(function () {

            provadati.text("href");
        });
    });
});
$(function () {
    $("#map-europe ul li").each(function () {
        $(this).mouseleave(function () {
            provadati.text('');
        });
    });
});
