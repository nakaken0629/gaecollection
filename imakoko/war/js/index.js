$(function() {
    $("#regist").click(function() {
        /* 位置情報取得用のオブジェクトを取得する */
        try {
            var geo = navigator.geolocation
                    || google.gears.factory.create('beta.geolocation');
        } catch (e) {
            /* nop */
        }
        if (geo == null) {
            alert("この端末では位置情報が取得できません");
            return;
        }

        /* 位置情報を取得する */
        var count = 0;
        var watchId = geo.watchPosition(function(position) {
            if (position.coords.accuracy < 300) {
                geo.clearWatch(watchId);
                $("#lat").val(position.coords.latitude);
                $("#lng").val(position.coords.longitude);
                $("form").submit();
                return;
            }
            if (count++ >= 5) {
                geo.clearWatch(watchId);
                alert("位置情報の精度が低いため失敗しました");
                return;
            }
        }, function(e) {
            geo.clearWatch(watchId);
            alert("位置情報の取得に失敗しました");
        }, {
            enableHighAccuracy : true
        });
    });


    /* 地図表示ボタンがクリックされた */
    $("#map").click(function() {
        var href = "/map";
        var tag = $("#tag").val();
        if (tag != "") {
            href += ("?tag=" + encodeURIComponent(tag));
        }
        location.href = href;
    })
});
