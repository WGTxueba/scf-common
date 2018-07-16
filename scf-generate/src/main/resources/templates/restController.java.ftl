package ${package.Controller};


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import ${package.Service}.${table.serviceName};
import ${package.Entity}.${table.entityName};

<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment} 前端接口控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@RestController
@RequestMapping("<#if package.ModuleName??>/api/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
 <#if superControllerClass??>
public class ${table.restControllerName} extends ${superControllerClass} {

    @Resource
    private ${table.serviceName} ${table.serviceName?uncap_first};

    @GetMapping
    public Page<${table.entityName}> query(${table.entityName} ${table.entityName?uncap_first}, @PageableDefault Page<${table.entityName}> page) {
        EntityWrapper<${table.entityName}> entityWrapper = new EntityWrapper<>(${table.entityName?uncap_first});
        Page<${table.entityName}> ${table.entityName?uncap_first}Page = ${table.serviceName?uncap_first}.selectPage(page, entityWrapper);
        logger.debug("result:{}", ${table.entityName?uncap_first}Page);
        return ${table.entityName?uncap_first}Page;
    }

    /**
     * 根据32位ID查询资源
     *
     * @param id 查询ID
     * @return
     */
    @GetMapping("/{id:[0-9a-z]{32}}")
    public ${table.entityName} get(@PathVariable String id) {
        ${table.entityName} ${table.entityName?uncap_first} = ${table.serviceName?uncap_first}.selectById(id);
        logger.debug("result:{}", ${table.entityName?uncap_first});
        return ${table.entityName?uncap_first};
    }

    /**
     * 新增资源
     *
     * @param ${table.entityName?uncap_first}
     */
    @PostMapping
    public ${table.entityName} insert(@RequestBody ${table.entityName} ${table.entityName?uncap_first}) {
        boolean result = ${table.serviceName?uncap_first}.insert(${table.entityName?uncap_first});
        logger.debug("result:{},insert:{}", result, ${table.entityName?uncap_first});
        return ${table.entityName?uncap_first};
    }

    /**
     * 修改资源
     *
     * @param ${table.entityName?uncap_first}
     * @return
     */
    @PutMapping
    public ${table.entityName} update(@RequestBody ${table.entityName} ${table.entityName?uncap_first}) {
        boolean result = ${table.serviceName?uncap_first}.updateById(${table.entityName?uncap_first});
        logger.debug("result:{},update:{}", result, ${table.entityName?uncap_first});
        return ${table.entityName?uncap_first};
    }

    /**
     * 根据32位ID删除资源
     *
     * @param id ${table.entityName?uncap_first}ID
     */
    @DeleteMapping("/{id:[0-9a-z]{32}}")
    public void delete(@PathVariable String id) {
        boolean result = ${table.serviceName?uncap_first}.deleteById(id);
        logger.debug("result:{},delete by id:{}", result, id);
    }

    /**
     * 批量删除
     *
     * @param ids 需要删除的ID集合
     */
    @PostMapping("/batchDelete")
    public void batchDelete(@RequestBody List<String> ids) {
        boolean result = ${table.serviceName?uncap_first}.deleteBatchIds(ids);
        logger.debug("delete by id:{},result:{}", ids, result);
    }
}
 <#else>
public class ${table.controllerName} {

}
 </#if>
</#if>
