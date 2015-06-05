var mapeurope = $('map-europe ul li')
var provadati = $('#provadati')

$(function () {
    $("#map-europe ul li").each(function () {
        $(this).mouseenter(function () {

            $.ajax({
                url: '/api/Finalscore?country=' + $(this).children().text() + '&order=score&desc=true&limit=4',
                type: 'GET',
                dataType: 'json'
            }).done(function (data) {
                var html = '';
                if (data != undefined||data.length()>0) {
                    html = '<h3 id="statetitle">' + data[0].namecountry + '</h3><table border="1" id="table"><tr><td id="titolotab">Language</td><td id="titolotab">Score</td></tr>';
                    for (var i = 0; i < data.length; i++) {
                        var resultcountry = data[i];

                        html = html + '<tr><td>' + resultcountry.namelanguage + '</td><td>' + resultcountry.score + '</td></tr>';

                    }

                    html = html + '</table>';
                }
                else {
                    html = 'no results';
                }
                provadati.html(html);
            }).error(function (e) {
                alert("errore")
            })
        });
    });
});

$(function () {
    $("#map-europe ul li").each(function () {
        $(this).mouseleave(function () {
            provadati.html('<h4>Drag your mouse to see previews or click one Europe country to see the full graph</h4>');
        });
    });
});
