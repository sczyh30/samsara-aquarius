var handler = function (captchaObj) {
    captchaObj.appendTo("#geetest-captcha");
};

$.ajax({
    url: "/geetest.new",
    type: "get",
    dataType: "json", // jsonp
    success: function (data) {
        initGeetest({
            gt: data.gt,
            challenge: data.challenge,
            product: "float",
            offline: !data.success
        }, handler);
    }
});
