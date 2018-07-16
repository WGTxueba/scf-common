<!DOCTYPE html>
<html>
<head>
    ${r'<#include "/common/head.ftl"/>'}
    <link rel="stylesheet" href="${r"${request.contextPath}"}/webjars/jqGrid/css/ui.jqgrid.css">
</head>
<body>
<section class="content-header">
    <h1>
        Dashboard
        <small>Control panel</small>
    </h1>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
        <li class="active">Dashboard</li>
    </ol>
</section>

<section class="content">
    <div class="box box-primary">
        <div class="box-header">
            <h3 class="box-title">查询</h3>
        </div>
        <div class="box-body">
            <form class="form-horizontal">
                <#list table.fields as field>
                <div class="col-sm-6">
                    <div class="form-group">
                        <label for="${field.propertyName}Element" class="col-sm-2 control-label">${field.comment}</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="${field.propertyName}Element" name="${field.propertyName}" placeholder="请输入${field.comment}">
                        </div>
                    </div>
                </div>
                </#list>
                <div class="text-center">
                    <button type="button" id="query" class="btn btn-primary">查询</button>
                    <button type="reset" class="btn btn-default">重置</button>
                </div>
            </form>
        </div>
    </div>
    <div class="box box-default color-palette-box">
        <div class="box-header">
            <a href="${r'${request.contextPath}'}/demo/add" class="btn btn-success">新增</a>
            <button type="button" id="update" class="btn btn-info">修改</button>
            <button type="button" id="delete" class="btn btn-danger">删除</button>
        </div>
        <div class="box-body">
            <table id="jqGrid"></table>
        </div>
    </div>
</section>
${r'<#include "/common/foot.ftl"/>'}
<script src="${r'${request.contextPath}'}/webjars/AdminLTE/plugins/jQueryUI/jquery-ui.min.js"></script>
<script src="${r'${request.contextPath}'}/webjars/jqGrid/i18n/min/grid.locale-cn.js"></script>
<script src="${r'${request.contextPath}'}/webjars/jqGrid/jquery.jqgrid.min.js"></script>
<script src="${r'${request.contextPath}'}/webjars/jqGrid/jquery.jqgrid.extend.js"></script>
<script>
    $(function () {
        var jqGrid = $("#jqGrid").jqGrid({
            url: basePath + '/api/${package.ModuleName}/${table.entityPath}',
            colModel: [
                {
                    label: '操作', sortable: false, frozen: true,width:80, formatter: function (cellvalue, options, rowObject) {
                        var html = '<a href="javascript:void(0);" data-row="' + rowObject.id + '" class="update">修改</a>';
                        html += '<span class="text-explode">|</span>';
                        html += '<a href="javascript:void(0);" data-row="' + rowObject.id + '" class="delete">删除</a>';
                        return html;
                    }
                },
                // {label: 'ID', name: 'id', key: true, frozen: true},
                <#list table.fields as field>
                {label: '${field.comment}', name: '${field.propertyName}'},
                </#list>
            ],
        })
        //         .jqGrid('setGroupHeaders', {
        //     useColSpanStyle: false,
        //     groupHeaders:[
        //         {startColumnName: 'name', numberOfColumns: 3, titleText: '<em>详情信息</em>'},
        //     ]
        // });
        // 更新操作
        $(document).on('click','button[id="update"],a.update',function () {
            var rowId = $(this).data("row");
            if (rowId) {
                rowId = [rowId];
            } else {
                rowId = jqGrid.getSelectedRowIds();
            }
            if (rowId.length === 0) {
                alert("请选择一条记录！");
                return;
            }
            location.href = basePath + "/${package.ModuleName}/${table.entityPath}/" + rowId;
        });

        // 删除操作
        $(document).on('click', 'button[id="delete"],a.delete', function () {
            var rowId = $(this).data("row");
            if (rowId) {
                rowId = [rowId];
            } else {
                rowId = jqGrid.getSelectedRowIds();
            }
            // 获取选中行的数据
            // var rowData = jqGrid.getSelectedRowData();
            if (rowId.length === 0) {
                alert("请至少选择一条记录！");
                return;
            }
            if (!confirm("是否要删除选中的条" + rowId.length + "记录？")) {
                return;
            }
            if (rowId.length === 1) {
                $.btDelete(basePath + "/api/${package.ModuleName}/${table.entityPath}/" + rowId, {}, function () {
                    //刷新表格
                    jqGrid.refresh();
                    //重置多选项
                    jqGrid.resetSelection();
                    alert("删除成功！");
                }, function (jqXHR, textStatus, errorThrown) {
                    alert("删除失败！")
                });
            } else {
                $.btPost(basePath + "/api/${package.ModuleName}/${table.entityPath}/batchDelete", rowId, function () {
                    jqGrid.refresh();
                    jqGrid.resetSelection();
                    alert("删除成功！");
                }, function (jqXHR, status, error) {
                    alert("删除失败！")
                });
            }
        });

        // 查询操作
        $('button[id="query"]').click(function(){
            var data = $('form').serializeObject();
            jqGrid.search(data);
        });
    });
</script>
</body>
</html>




