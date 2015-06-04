var mapeurope = $('map-europe ul li')
var provadati = $('#provadati')
    
$(function () {
    $("#map-europe ul li").each(function () {
        $(this).mouseenter(function () {
           
            $.ajax({
                url: '/api/Finalscore?country='+$(this).children().text()+'&limit=4',
                type: 'GET',
                dataType: 'json'
            }).done(function (data) {
                var html = '';
                for (var i = 0; i < data.length; i++) {
                    var resultcountry = data[i];

                    html = html + resultcountry.namelanguage + " " + resultcountry.score + '<br/>';
                    provadati.text(html);
                }

                $('#provadati').html(html);
            }).error(function (e) {
                alert("errore")
            })
        });
    });
});

$(function () {
    $("#map-europe ul li").each(function () {
        $(this).mouseleave(function () {
            provadati.html('<h4>Drag your mouse to see previews or click one country to see the full graph</h4>');
        });
    });
});
