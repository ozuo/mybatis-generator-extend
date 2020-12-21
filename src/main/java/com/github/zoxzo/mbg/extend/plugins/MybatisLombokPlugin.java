package com.github.zoxzo.mbg.extend.plugins;


import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
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

    /**
     * 是否使用Swagger2注解 默认false
     */
    private String useSwagger2Flag;

    /**
     * 验证参数是否有效
     *
     * @param list
     * @return
     */
    @Override
    public boolean validate(List<String> list) {
        useSwagger2Flag = properties.getProperty("useSwagger2Flag");
        if (useSwagger2Flag == null || useSwagger2Flag.trim().isEmpty()) {
            useSwagger2Flag = "false";
        }
        System.out.println("useSwagger2Flag = " + useSwagger2Flag);
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
        // 清除已有注释
        topLevelClass.getJavaDocLines().clear();

        // 添加lombok注解
        topLevelClass.addImportedType("lombok.Data");
        topLevelClass.addAnnotation("@Data");

        // 添加swagger注解
        if (StringUtility.isTrue(useSwagger2Flag)) {
            topLevelClass.addImportedType("io.swagger.annotations.ApiModel");
            topLevelClass.addImportedType("io.swagger.annotations.ApiModelProperty");
            String sa = "@ApiModel(description = \"" + introspectedTable.getRemarks() + "\")";
            topLevelClass.addAnnotation(sa);
        }

        // 添加类注释
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * " + introspectedTable.getRemarks() + "（" + introspectedTable.getFullyQualifiedTable() + "）");
        topLevelClass.addJavaDocLine(" *");
        topLevelClass.addJavaDocLine(" * @author " + System.getProperties().getProperty("user.name"));
        topLevelClass.addJavaDocLine(" * @date " + (new SimpleDateFormat("yyyy/MM/dd hh:mm")).format(new Date()));
        topLevelClass.addJavaDocLine(" */");
        topLevelClass.addSuperInterface(new FullyQualifiedJavaType("java.io.Serializable"));

        // 实现序列化接口
        topLevelClass.addImportedType("java.io.Serializable");
        final FullyQualifiedJavaType serializable = new FullyQualifiedJavaType("java.io.Serializable");
        topLevelClass.addImportedType(serializable);
        topLevelClass.addSuperInterface(serializable);

        // 添加序列化serialVersionUID  private static final long serialVersionUID = 1L
        Field field = new Field();
        field.setName("serialVersionUID");
        field.setType(new FullyQualifiedJavaType("long"));
        field.setInitializationString("1L");
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setFinal(true);
        field.setStatic(true);
        topLevelClass.addField(field);

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
        // 清除已有注释
        field.getJavaDocLines().clear();

        String remarks = introspectedColumn.getRemarks();
        field.addJavaDocLine("/**");
        if (StringUtility.stringHasValue(remarks)) {

            String[] remarkLines = remarks.split(System.getProperty("line.separator"));
            for (String remarkLine : remarkLines) {
                field.addJavaDocLine(" * " + remarkLine);
            }

        }
        boolean cannotNull = !introspectedColumn.isNullable();
        String defaultValue = introspectedColumn.getDefaultValue();
        String defaultVal = "默认值: " + defaultValue + ", ";
        if (StringUtility.stringHasValue(defaultValue)) {
            defaultVal = "默认值: " + defaultValue.trim().replaceAll("\r|\n", "") + ", ";
        }
        field.addJavaDocLine(" * " + defaultVal + "必填：" + cannotNull);
        field.addJavaDocLine(" */");
        if (StringUtility.isTrue(useSwagger2Flag)) {
            String sa = "@ApiModelProperty(value = \"" + remarks + "\", required = " + cannotNull + ")";
            field.addAnnotation(sa);
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
        // 清除已有注释
        interfaze.getJavaDocLines().clear();

        // 添加类注释
        interfaze.addJavaDocLine("/**");
        interfaze.addJavaDocLine(" * " + introspectedTable.getRemarks() + "（" + introspectedTable.getFullyQualifiedTable() + "）");
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

    /**
     * 自定义mapper.xml内容
     * 添加字段sql语句，效果例：
     * * <sql id="BaseColumns">
     * *     id, name, create_time, update_time
     * * </sql>
     * * <sql id="AliasColumns">
     * *     t.id, t.name, t.create_time, t.update_time
     * * </sql>
     *
     * @param document
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        System.out.println("mapper.xml add baseColumnsSql and aliasColumnsSql...");
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        XmlElement parentElement = document.getRootElement();

        // 添加基础字段sql语句
        XmlElement baseColumnsSql = new XmlElement("sql");
        baseColumnsSql.addAttribute(new Attribute("id", "BaseColumns")); // AliasColumns
        // 添加别名字段sql语句
        XmlElement aliasColumnsSql = new XmlElement("sql");
        aliasColumnsSql.addAttribute(new Attribute("id", "AliasColumns")); // AliasColumns

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        int max = 130; // 每行最大字符数
        int i = 1;
        int j = 1;
        for (IntrospectedColumn column : columns) {
            sb1.append(column.getActualColumnName()).append(", ");
            sb2.append("t.").append(column.getActualColumnName()).append(", ");
            // 内容超长换行处理
            if (sb1.length() > max * i) {
                i++;
                sb1.append("\r\n\t");
            }
            if (sb2.length() > max * j) {
                j++;
                sb2.append("\r\n\t");
            }
        }
        String baseColumns = sb1.toString();
        baseColumnsSql.addElement(new TextElement(baseColumns.substring(0, baseColumns.lastIndexOf(","))));
        parentElement.addElement(baseColumnsSql);

        String aliasColumns = sb2.toString();
        aliasColumnsSql.addElement(new TextElement(aliasColumns.substring(0, aliasColumns.lastIndexOf(","))));
        parentElement.addElement(aliasColumnsSql);

        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }
}


