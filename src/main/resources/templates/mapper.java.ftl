package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};

/**
*
* @author ${author}
* @since ${date}
*/
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

}
</#if>