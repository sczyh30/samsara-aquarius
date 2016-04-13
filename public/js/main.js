$(document).ready(function() {
    var article_like_btn = $("#aq-btn-a-favorite");

    // fetch count
    function refresh_fc_count() {
        $.ajax({
            url: '/api/lcount/' + article_like_btn.attr("fav-aid"),
            type: 'GET',
            success: function(response, textStatus, jqXhr) {
                $("#aq-fv-count").html(response);
            }
        })
    }
    //setInterval(refresh_fc_count, 5000);

    $("#ret-index").on("click", function () {
        location.href="/"
    });

    article_like_btn.on("click", function () {

        var status = Number(article_like_btn.attr("fav-s"));
        switch (status) {
            case 0:
                $.ajax({
                    url: '/api/al/' + article_like_btn.attr("fav-aid"),
                    type: 'PATCH',
                    success: function(response, textStatus, jqXhr) {
                        if (response.code == '2711') {
                            article_like_btn.attr("fav-s", 1);
                            article_like_btn.addClass("btn-default");
                            article_like_btn.removeClass("btn-success");
                            article_like_btn.attr("value", '已收藏');
                        }
                        refresh_fc_count()
                    },
                    error: function(jqXHR, textStatus, errorThrown) {

                    }
                });
                break;
            case 1:
                $.ajax({
                    url: '/api/aul/' + article_like_btn.attr("fav-aid"),
                    type: 'PATCH',
                    success: function(response, textStatus, jqXhr) {
                        if (response.code == '2761') {
                            article_like_btn.attr("fav-s", 0);
                            article_like_btn.removeClass("btn-default");
                            article_like_btn.addClass("btn-success");
                            article_like_btn.attr("value", '收藏');
                        }
                        refresh_fc_count()
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
