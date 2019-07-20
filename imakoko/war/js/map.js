var map
var lastDate;
var markersArray = [];
var tid;
/* HTMLが読み込まれたら実行される関数を準備する */
$(function() {
    /* 名古屋駅を示す位置オブジェクトを作成する */
    var latlng = new google.maps.LatLng(35.170914, 136.882052);
    /* 倍率、中心位置、地図の種類(ROADMAP)をオプションとして用意する */
    var myOptions = {
        zoom : 16,
        center : latlng,
        mapTypeId : google.maps.MapTypeId.ROADMAP
    };
    /* 地図オブジェクトを<div id="map">の中に作成する */
    map = new google.maps.Map(document.getElementById("map"), myOptions);

    /* 位置情報の一覧を取得する */
    refresh();

    /* 検索ボタンがクリックされたときの処理を登録する */
    $("#search").click(function() {
        if (tid != null) {
            clearTimeout(tid);
        }
        $('#places').empty();
        for (i in markersArray) {
            markersArray[i].setMap(null);
        }
        markersArray = [];
        lastDate = null;
        refresh()
    });
});

/* サーバーから位置情報を取得する */
function refresh() {
    var data = {};
    if (lastDate != null) {
        /* 前回更新日付があれば、その値を渡す */
        data.lastDate = lastDate.getTime();
    }
    data.tag = $('#tag').val();
    /* サーバーと通信する処理を登録する */
    $.ajax({
        type : 'GET',
        url : '/placesjson',
        data : data,
        cache : false,
        dataType : 'json',
        success : function(json) {
            /* 通信に成功した */
            $.each(json.reverse(), function(i, place) {
                addMarker(place);
            });
            lastDate = new Date();
        },
        complete : function() {
            /* 通信が終了した */
            tid = setTimeout(refresh, 10000);
        }
    });
}

/* マーカーを追加する */
function addMarker(place) {
    /* マーカーを追加する */
    var myLatlng = new google.maps.LatLng(place.lat, place.lng);
    var marker = new google.maps.Marker({
        map : map,
        position : myLatlng
    });
    markersArray.push(marker);

    /* 位置情報を追加する */
    div = $('<div class="place">' + '<a class="nickname" href="">nickname</a>'
            + ' <span class="message">message</span>'
            + '<div class="elapseTime">elapseTime</div>' + '</div>');
    $('a.nickname', div).attr('href',
            'javascript:setCenter(' + place.lat + ',' + place.lng + ');').text(
            place.nickname);
    $('span.message', div).text(place.message);
    $('div.elapseTime', div).text(place.elapseTime);
    $('#places').prepend(div);

    /* マーカーを地図の中心に移動する */
    setCenter(place.lat, place.lng);
}

/* 地図の中心を移動する */
function setCenter(lat, lng) {
    map.setCenter(new google.maps.LatLng(lat, lng));
}
