<html>
    <head>
        ${'<#include "/common/head.ftl"/>'}
    </head>
<body>
<section class="content-header">
    <h1>演示 <small>新增</small></h1>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="#">演示</a></li>
        <li class="active">新增</li>
    </ol>
</section>
<section class="content">
    <div class="box box-default color-palette-box">
        <#--引入公共表单页面-->
        ${'<#include "./form.ftl"/>'}
    </div>
</section>
${'<#include "/common/foot.ftl"/>'}
<script type="text/javascript">
    $(function () {
        $('button[id="submit"]').click(function () {
            var data = $('form').serializeObject();
            $.btPost(basePath + "/api/${package.ModuleName}/${table.entityPath}", data,function(data){
                alert("新增数据成功");
            },function(jqXHR, status, error){
                alert("新增数据失败");
            });
        });
    });
</script>
</body>
</html>
