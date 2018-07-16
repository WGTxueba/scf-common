(function (global, factory) {
    "use strict";
    if (typeof define === "function" && define.amd) {
        define([
            "jquery"
        ], function ($) {
            return factory($, global.document);
        });
    } else if (typeof module === "object" && module.exports) {
        module.exports = function (root, $) {
            if (!root) {
                root = window;
            }
            if ($ === undefined) {
                $ = typeof window !== "undefined" ?
                    require("jquery") :
                    require("jquery")(root);
            }
            factory($, root.document);
            return $;
        };
    } else {
        factory(jQuery, global.document);
    }
}(typeof window !== "undefined" ? window : this, function ($, document) {
    var jgrid = $.jgrid, _jqGrid = $.fn.jqGrid;
    jgrid.extend({
        refresh: function () {
            $(this).trigger("reloadGrid");
        },
        search: function (options, page) {
            // var newParams = $.extend({page: 1}, options);
            // console.log(newParams);
            $(this).jqGrid('setGridParam', {
                postData: options,
                page: page ? page : 1
            }).trigger("reloadGrid");
        },
        getSelectedRowIds:function(){
            return $(this).jqGrid('getGridParam','selarrrow');
        },
        getSelectedRowData:function(){
            var rowIds = $(this).jqGrid('getGridParam','selarrrow');
            if(rowIds||rowIds.length===0){
                return null;
            }
            var selectedRowData = [];
            for(var i=0;i<rowIds.length;i++){
                var rowData = $(this).jqGrid('getRowData',rowIds[i]);
                selectedRowData.push(rowData);
            }
            return null;
        },
    });
    hasFrozen = function (options) {
        if (options.colModel) {
            for (var i = 0; i < options.colModel.length; i++) {
                var model = options.colModel[i];
                if (model.frozen) {
                    return true;
                }
            }
        }
        return false;
    };
    $.fn.jqGrid = function (params) {
        $.fn.jqGrid = _jqGrid;
        var _this = this;

        var id = _this.attr("id");
        var jqGridPager = id + "_jqGridPager";
        if (!_this.parent().hasClass("tableGridWrapper")) {
            _this.wrap('<div class="tableGridWrapper">');
        }
        _this.after('<div id="' + jqGridPager + '"></div>');

        var globalOption = {
            mtype: "GET",
            datatype: "json",
            page: 1,
            rowNum: 15,
            rowList: [15, 50, 150, 300],
            iconSet: "fontAwesome",
            height: 480,
            //开启此项，引入jqueryUI，列才可以拖动
            sortable: true,
            autowidth: true,
            //是否显示行号
            rownumbers: true,
            //行号宽度
            rownumWidth: 60,
            // scrollPopUp:true,
            scrollLeftOffset: "83%",
            //是否显示多选列
            multiselect: true,
            multiPageSelection: true,
            //多选列的宽度
            multiselectWidth: 35,
            viewrecords: true,
            //默认启用斑马纹
            altRows: true,
            //右侧滚动条默认宽度
            scrollOffset: 10,
            shrinkToFit: params.shrinkToFit ? params.shrinkToFit : !hasFrozen(params),
            pager: "#" + jqGridPager,
            prmNames: {
                page: "current",
                rows: "size",
                sort: "orderByField",
                order: "asc",
                search: "_search",
                nd: "_btv",
                totalrows: "total"
            },
            jsonReader: {
                root: "records",
                page: "current",
                total: "pages",
                records: "total",
                repeatitems: true,
                cell: "cell",
                id: "id",
                userdata: "userdata",
                subgrid: {
                    root: "rows",
                    repeatitems: true,
                    cell: "cell"
                }
            },
            ajaxGridOptions : {
                headers: {
                    "Authorization": "Bearer " + getCookie("BTACTK")
                }
            },
            //修复结合mp插件无法排序的问题
            loadBeforeSend:function(xhr,settings){
                var orderStr = globalOption.prmNames.order + "=";
                settings.url = settings.url.replace(orderStr+"asc",orderStr+"1").replace(orderStr+"desc",orderStr+"0");
            }
        };
        var newParams = $.extend(globalOption, params);
        $(window).unbind("resize").bind("resize", function () {
            var width = $(".tableGridWrapper").width();
            if (width > 400) {
                _this.jqGrid("setGridWidth", width);
            }
        });
        return _this.jqGrid(newParams).jqGrid("setFrozenColumns");
    };
}));
