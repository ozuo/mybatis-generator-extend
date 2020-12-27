package com.github.ozuo.mbg.extend.resolver;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.*;

/**
 * 生成javabean自定义类型转换
 *
 * @author zuoyx
 * @since 2020/12/10 11:33
 */
public class MyJavaTypeResolver implements JavaTypeResolver {

    protected List<String> warnings;

    protected Properties properties;

    protected Context context;

    protected boolean forceBigDecimals;

    protected Map<Integer, JdbcTypeInformation> typeMap;

    public MyJavaTypeResolver() {
        super();
        properties = new Properties();
        typeMap = new HashMap<>();

        typeMap.put(Types.ARRAY, new JdbcTypeInformation("ARRAY", //$NON-NLS-1$
                new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.BIGINT, new JdbcTypeInformation("BIGINT", //$NON-NLS-1$
                new FullyQualifiedJavaType(Long.class.getName())));
        typeMap.put(Types.BINARY, new JdbcTypeInformation("BINARY", //$NON-NLS-1$
                new FullyQualifiedJavaType("byte[]"))); //$NON-NLS-1$
        typeMap.put(Types.BIT, new JdbcTypeInformation("BIT", //$NON-NLS-1$
                new FullyQualifiedJavaType(Boolean.class.getName())));
        typeMap.put(Types.BLOB, new JdbcTypeInformation("BLOB", //$NON-NLS-1$
                new FullyQualifiedJavaType("byte[]"))); //$NON-NLS-1$
        typeMap.put(Types.BOOLEAN, new JdbcTypeInformation("BOOLEAN", //$NON-NLS-1$
                new FullyQualifiedJavaType(Boolean.class.getName())));
        typeMap.put(Types.CHAR, new JdbcTypeInformation("CHAR", //$NON-NLS-1$
                new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.CLOB, new JdbcTypeInformation("CLOB", //$NON-NLS-1$
                new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.DATALINK, new JdbcTypeInformation("DATALINK", //$NON-NLS-1$
                new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.DATE, new JdbcTypeInformation("DATE", //$NON-NLS-1$
                new FullyQualifiedJavaType(Date.class.getName())));
        typeMap.put(Types.DISTINCT, new JdbcTypeInformation("DISTINCT", //$NON-NLS-1$
                new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.DOUBLE, new JdbcTypeInformation("DOUBLE", //$NON-NLS-1$
                new FullyQualifiedJavaType(Double.class.getName())));
        typeMap.put(Types.FLOAT, new JdbcTypeInformation("FLOAT", //$NON-NLS-1$
                new FullyQualifiedJavaType(Double.class.getName())));
        typeMap.put(Types.INTEGER, new JdbcTypeInformation("INTEGER", //$NON-NLS-1$
                new FullyQualifiedJavaType(Integer.class.getName())));
        typeMap.put(Types.JAVA_OBJECT, new JdbcTypeInformation("JAVA_OBJECT", //$NON-NLS-1$
                new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.LONGNVARCHAR, new JdbcTypeInformation("LONGNVARCHAR", //$NON-NLS-1$
                new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.LONGVARBINARY, new JdbcTypeInformation(
                "LONGVARBINARY", //$NON-NLS-1$
                new FullyQualifiedJavaType("byte[]"))); //$NON-NLS-1$
        typeMap.put(Types.LONGVARCHAR, new JdbcTypeInformation("LONGVARCHAR", //$NON-NLS-1$
                new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.NCHAR, new JdbcTypeInformation("NCHAR", //$NON-NLS-1$
                new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.NCLOB, new JdbcTypeInformation("NCLOB", //$NON-NLS-1$
                new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.NVARCHAR, new JdbcTypeInformation("NVARCHAR", //$NON-NLS-1$
                new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.NULL, new JdbcTypeInformation("NULL", //$NON-NLS-1$
                new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.OTHER, new JdbcTypeInformation("OTHER", //$NON-NLS-1$
                new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.REAL, new JdbcTypeInformation("REAL", //$NON-NLS-1$
                new FullyQualifiedJavaType(Float.class.getName())));
        typeMap.put(Types.REF, new JdbcTypeInformation("REF", //$NON-NLS-1$
                new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.SMALLINT, new JdbcTypeInformation("SMALLINT", //$NON-NLS-1$
                new FullyQualifiedJavaType(Short.class.getName())));
        typeMap.put(Types.STRUCT, new JdbcTypeInformation("STRUCT", //$NON-NLS-1$
                new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.TIME, new JdbcTypeInformation("TIME", //$NON-NLS-1$
                new FullyQualifiedJavaType(Date.class.getName())));
        typeMap.put(Types.TIMESTAMP, new JdbcTypeInformation("TIMESTAMP", //$NON-NLS-1$
                new FullyQualifiedJavaType(Date.class.getName())));
        typeMap.put(Types.TINYINT, new JdbcTypeInformation("TINYINT", //$NON-NLS-1$
                new FullyQualifiedJavaType(Byte.class.getName())));
        typeMap.put(Types.VARBINARY, new JdbcTypeInformation("VARBINARY", //$NON-NLS-1$
                new FullyQualifiedJavaType("byte[]"))); //$NON-NLS-1$
        typeMap.put(Types.VARCHAR, new JdbcTypeInformation("VARCHAR", //$NON-NLS-1$
                new FullyQualifiedJavaType(String.class.getName())));

    }

    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);
        forceBigDecimals = StringUtility.isTrue(properties.getProperty(PropertyRegistry.TYPE_RESOLVER_FORCE_BIG_DECIMALS));
    }

    /**
     * 指定java类型
     *
     * @param introspectedColumn 表列数据
     * @return javaType
     */
    @Override
    public FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn) {
        FullyQualifiedJavaType answer;
        JdbcTypeInformation jdbcTypeInformation = typeMap.get(introspectedColumn.getJdbcType());

        if (jdbcTypeInformation == null) {
            switch (introspectedColumn.getJdbcType()) {
                case Types.DECIMAL:
                    //如果包含小数点且长度超过10则转换成BigDecimal
                    if (introspectedColumn.getLength() > 10 && introspectedColumn.getScale() > 0 || forceBigDecimals) {
                        answer = new FullyQualifiedJavaType(BigDecimal.class.getName());
                    } else {
                        if (introspectedColumn.getScale() > 0) {
                            answer = new FullyQualifiedJavaType(Double.class.getName());
                        } else if (introspectedColumn.getLength() > 10) {
                            answer = new FullyQualifiedJavaType(Long.class.getName());
                        } else if (introspectedColumn.getLength() > 0) {
                            answer = new FullyQualifiedJavaType(Integer.class.getName());
                        } else { // 未指定长度
                            answer = new FullyQualifiedJavaType(Long.class.getName());
                        }
                    }
                    break;
                case Types.NUMERIC:
                    if (introspectedColumn.getScale() > 0 || forceBigDecimals) {//如果包含小数点则转换成BigDecimal
                        answer = new FullyQualifiedJavaType(BigDecimal.class.getName());
                    } else {
                        if (introspectedColumn.getLength() > 10) {
                            answer = new FullyQualifiedJavaType(Long.class.getName());
                        } else if (introspectedColumn.getLength() > 0) {
                            answer = new FullyQualifiedJavaType(Integer.class.getName());
                        } else { // 未指定长度
                            answer = new FullyQualifiedJavaType(Long.class.getName());
                        }
                    }
                    break;

                default:
                    answer = null;
                    break;
            }
        } else {
            answer = jdbcTypeInformation.getFullyQualifiedJavaType();
        }

        return answer;
    }

    /**
     * 指定jdbc类型
     *
     * @param introspectedColumn 列数据
     * @return String
     */
    @Override
    public String calculateJdbcTypeName(IntrospectedColumn introspectedColumn) {
        String answer;
        JdbcTypeInformation jdbcTypeInformation = typeMap
                .get(introspectedColumn.getJdbcType());

        if (jdbcTypeInformation == null) {
            switch (introspectedColumn.getJdbcType()) {
                case Types.DECIMAL:
                    if (introspectedColumn.getScale() > 0) {//如果包含小数点则转换成BigDecimal
                        answer = "DECIMAL";
                    } else {
                        answer = "NUMERIC";
                    }
                    break;
                case Types.NUMERIC:
                    answer = "NUMERIC"; //$NON-NLS-1$
                    break;
                default:
                    answer = null;
                    break;
            }
        } else {
            answer = jdbcTypeInformation.getJdbcTypeName();
        }

        return answer;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static class JdbcTypeInformation {
        private String jdbcTypeName;

        private FullyQualifiedJavaType fullyQualifiedJavaType;

        public JdbcTypeInformation(String jdbcTypeName,
                                   FullyQualifiedJavaType fullyQualifiedJavaType) {
            this.jdbcTypeName = jdbcTypeName;
            this.fullyQualifiedJavaType = fullyQualifiedJavaType;
        }

        public String getJdbcTypeName() {
            return jdbcTypeName;
        }

        public FullyQualifiedJavaType getFullyQualifiedJavaType() {
            return fullyQualifiedJavaType;
        }
    }
}