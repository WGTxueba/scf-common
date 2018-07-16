package ${package.Controller};


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {

    @GetMapping("/list")
    public String list(){
        return "/${package.ModuleName}/${table.entityName?uncap_first}/list";
    }

    @GetMapping("/add")
    public String add(){
        return "/${package.ModuleName}/${table.entityName?uncap_first}/add";
    }

    @GetMapping("/update/{id:[0-9a-z]{32}}")
    public String update(@PathVariable String id, Model model) {
        logger.debug("update Page, id:{}",id);
        model.addAttribute("id", id);
        return "/${package.ModuleName}/${table.entityName?uncap_first}/update";
    }

}
<#else>
public class ${table.controllerName} {

}
</#if>
</#if>
