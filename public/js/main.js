$(document).ready(function() {
    $("#ret-index").on("click", function () {
        location.href="/"
    });

    $("#aq-btn-a-favorite").on("click", function () {
        var btn = $("#aq-btn-a-favorite");
        var status = Number(btn.attr("fav-s"));
        switch (status) {
            case 0:
                $.ajax({
                    url: '/api/al/' + btn.attr("fav-aid"),
                    type: 'PATCH',
                    success: function(response, textStatus, jqXhr) {
                        if (response.code == '2711') {
                            btn.attr("fav-s", 1);
                            btn.addClass("btn-default glyphicon-heart");
                            btn.removeClass("btn-success glyphicon-heart-empty");
                            btn.attr("value", '已收藏');
                        }
                    },
                    error: function(jqXHR, textStatus, errorThrown) {

                    }
                });
                break;
            case 1:
                $.ajax({
                    url: '/api/aul/' + btn.attr("fav-aid"),
                    type: 'PATCH',
                    success: function(response, textStatus, jqXhr) {
                        if (response.code == '2761') {
                            btn.attr("fav-s", 0);
                            btn.removeClass("btn-default glyphicon-heart");
                            btn.addClass("btn-success glyphicon-heart-empty");
                            btn.attr("value", '收藏');
                        }
                    },
                    error: function(jqXHR, textStatus, errorThrown) {

                    }
                });
                break;
            case 4:
                location.href = '/login.now';
                break;
            default:
                break;
        }


    })
});
