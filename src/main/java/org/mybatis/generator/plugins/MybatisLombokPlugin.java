// package位置固定 不能修改
package org.mybatis.generator.plugins;


import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 自定义lombok插件 java实体类会增加lombok注解 并自定义注释
 *
 * @author zuoyx
 * @since 2020/10/22 14:07
 */
public class MybatisLombokPlugin extends PluginAdapter {

    public MybatisLombokPlugin() {
    }

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    /**
     * 实体类 entity.java
     * 增加@Data注解
     * 增加类注释
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.getJavaDocLines().clear();

        topLevelClass.addImportedType("lombok.Data");
        topLevelClass.addImportedType("java.io.Serializable");
        topLevelClass.addAnnotation("@Data");

        // 添加类注释
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * " + introspectedTable.getRemarks() + "（" + introspectedTable.getFullyQualifiedTable() + "）\r\n");
        topLevelClass.addJavaDocLine(" *");
        topLevelClass.addJavaDocLine(" * @author " + System.getProperties().getProperty("user.name"));
        topLevelClass.addJavaDocLine(" * @date " + (new SimpleDateFormat("yyyy/MM/dd hh:mm")).format(new Date()));
        topLevelClass.addJavaDocLine(" */");
        topLevelClass.addSuperInterface(new FullyQualifiedJavaType("java.io.Serializable"));
        return true;
    }

    /**
     * 实体类 entity.java 字段注释
     *
     * @param field
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @param modelClassType
     * @return
     */
    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        field.getJavaDocLines().clear();
        String remarks = introspectedColumn.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            field.addJavaDocLine("/**");
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));
            for (String remarkLine : remarkLines) {
                field.addJavaDocLine(" * " + remarkLine);
            }
            field.addJavaDocLine(" */");
        }
        return true;
    }

    /**
     * mapper.java mapper接口
     *
     * @param interfaze
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interfaze.getJavaDocLines().clear();
        // 添加Mapper的import
//        interfaze.addImportedType(new FullyQualifiedJavaType("tk.mybatis.mapper.common.Mapper"));
        // 添加Mapper的注解
//        interfaze.addAnnotation("@Mapper");
        // 添加类注释
        interfaze.addJavaDocLine("/**");
        interfaze.addJavaDocLine(" * " + introspectedTable.getRemarks() + "（" + introspectedTable.getFullyQualifiedTable() + "）\r\n");
        interfaze.addJavaDocLine(" *");
        interfaze.addJavaDocLine(" * @author " + System.getProperties().getProperty("user.name"));
        interfaze.addJavaDocLine(" * @date " + (new SimpleDateFormat("yyyy/MM/dd hh:mm")).format(new Date()));
        interfaze.addJavaDocLine(" */");
        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        // 不生成setter
        return false;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        // 不生成getter
        return false;
    }
}


