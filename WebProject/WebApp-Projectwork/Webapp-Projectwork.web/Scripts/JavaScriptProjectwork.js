var provadati = $('#provadati')
$(function () {
    $("#map-europe ul li").each(function () {
        $(this).mouseenter(function () {
            provadati.text('cieo');
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
