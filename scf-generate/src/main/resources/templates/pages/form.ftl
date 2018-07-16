<form class="form-horizontal">
    ${'<#if id??>'}
    <input type="hidden" id="id" name="id" value="${r'${id}'}">
    ${'</#if>'}
    <div class="box-body">
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
    </div>
    <div class="box-footer">
        <div class="pull-right">
            <div class="btn-group ">
                <button type="button" id="submit" class="btn btn-info">提交</button>
            </div>
            <div class="btn-group ">
                <button type="button" id="cancel" class="btn btn-default" onclick="history.back();">取消</button>
            </div>
        </div>
    </div>
</form>
