jQuery.each(["btGet", "btPost", "btPut", "btDelete"], function (i, method) {
    jQuery[method] = function (url, data, callback, errorCallback, type) {
        if (jQuery.isFunction(data)) {
            type = type || errorCallback || callback;
            callback = data;
            data = undefined;
        }
        var options = {
            url: url,
            type: method.replace("bt", ""),
            dataType: type,
            data: data,
            success: callback,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Bearer " + getCookie("BTACTK"));
            }
        };

        if (method === "btPost" || method === "btPut") {
            options = jQuery.extend(options, {
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(data)
            });
        }

        if (jQuery.isFunction(errorCallback)) {
            options = jQuery.extend(options, {error: errorCallback});
        }
        return jQuery.ajax(jQuery.extend(options, jQuery.isPlainObject(url) && url));
    };
});
function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");

    if (arr = document.cookie.match(reg)) {
        return unescape(arr[2]);
    } else {
        return null;
    }
}

jQuery.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    jQuery.each(a, function () {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};
