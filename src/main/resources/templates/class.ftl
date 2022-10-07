package ${packageName};

// start imports
<#list imports as import>
import ${import};
</#list>
import lombok.Data;
//end of imports

//class annotations
<#list annotations as annotation>
${annotation}
</#list>
@Data
//end of class annotations
public class ${className} {
    // start of class properties
    <#list fields as field>
     <#list field.getAnnotations() as annotation>
     ${annotation}
     </#list>
     private ${field.getType()} ${field.getName() } ;
    </#list>
    // end of class properties
}



