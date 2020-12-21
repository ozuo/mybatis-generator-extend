# mbg-plugins
### mybatis generator plugins extend, such as lombok, swagger2, ...


# 自定义Mybatis Generator插件步骤
    1. 编辑MybatisLombokPlugin.java
    
    2. idea->Build->Recompile 得到项目 target/classes/org/mybatis/generator/plugins/MybatisLombokPlugin.class
    
    3. 进入/Users/wanda/.m2/repository/org/mybatis/generator/mybatis-generator-core/1.4.0/
    
    4. 解压mybatis-generator-core-1.4.0.jar，然后将MybatisLombokPlugin.class放到org/mybatis/generator/plugins目录下
    
    5. 还原mybatis-generator-core-1.4.0.jar，执行命令：
        cd /Users/wanda/.m2/repository/org/mybatis/generator/mybatis-generator-core/1.4.0/mybatis-generator-core-1.4.0
        jar cvfM0 mybatis-generator-core-1.4.0.jar *
    
    6. 得到新的mybatis-generator-core-1.4.0.jar，放到.m2/repository/org/mybatis/generator/mybatis-generator-core/1.4.0/ 目录下，记得删除会覆盖原来的jar包
    
    7. generatorConfig.xml中增加<plugin type="org.mybatis.generator.plugins.MybatisLombokPlugin"></plugin>，完成


## 以上，如果嫌麻烦可以用已打包好的
    ```
    <dependency>
        <groupId>com.github.zoxzo</groupId>
        <artifactId>mybatis-generator-extend</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    ```

# 使用
    1. pom.xml
    ```
    <dependencies>
        <dependency>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-core</artifactId>
            <version>1.3.7</version>
        </dependency>
        <dependency>
            <groupId>com.github.zoxzo</groupId>
            <artifactId>mybatis-generator-extend</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-source-plugin</artifactId>
            </plugin>

            <!-- mybatis.generator -->
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.7</version>
                <configuration>
                    <!-- 配置文件路径 -->
                    <configurationFile>src/main/resources/mybatis-generator/generatorConfig.xml</configurationFile>
                    <!--允许移动生成的文件 -->
                    <verbose>true</verbose>
                    <!-- 是否覆盖 -->
                    <overwrite>true</overwrite>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>tk.mybatis</groupId>
                        <artifactId>mapper</artifactId>
                        <version>4.1.5</version>
                    </dependency>
                    <dependency>
                        <groupId>com.github.zoxzo</groupId>
                        <artifactId>mybatis-generator-extend</artifactId>
                        <version>1.0-SNAPSHOT</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
    ```
    
    2. mybatis_generator.properties
    ```
    mybatis.generator.datasource.url=jdbc:oracle:thin:@10.161.17.181:1521:cardb
    mybatis.generator.datasource.username=wise
    mybatis.generator.datasource.password=wisekyh
    mybatis.generator.datasource.driver-class-name=oracle.jdbc.driver.OracleDriver
    mybatis.mapper-locations = classpath:/mappers/*.xml
    mybatis.type-aliases-package = com.wandaloans.common.mapper
    classpath=/Users/xxxx/.m2/repository/com/oracle/ojdbc/ojdbc8/19.3.0.0/ojdbc8-19.3.0.0.jar
    ```
    
    3. generatorConfig.xml
    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE generatorConfiguration
            PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
            "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
    <generatorConfiguration>
        <!--导入属性配置 -->
        <properties resource="mybatis-generator/mybatis_generator.properties"></properties>
    
        <!-- 数据库驱动包路径 -->
        <classPathEntry location="${classpath}"/>
    
        <!--指定特定数据库的jdbc驱动jar包的位置 -->
        <context id="carddb" targetRuntime="MyBatis3Simple">
            <property name="javaFileEncoding" value="UTF-8"/>
            <!-- 格式化java代码 -->
            <property name="javaFormatter" value="org.mybatis.generator.api.dom.DefaultJavaFormatter"/>
            <!-- 格式化XML代码 -->
            <property name="xmlFormatter" value="org.mybatis.generator.api.dom.DefaultXmlFormatter"/>
    
    
            <!-- 配置 tk.mybatis 插件 -->
            <plugin type="tk.mybatis.mapper.generator.MapperPlugin">
                <property name="mappers" value="tk.mybatis.mapper.common.Mapper"/>
                <property name="caseSensitive" value="true"/>
            </plugin>
            <!-- 自定义lombok插件 java实体类会增加lombok注解 -->
            <plugin type="com.github.zoxzo.mbg.extend.plugins.MybatisLombokPlugin">
                <property name="useSwagger2Flag" value="false"/>
            </plugin>
            <!-- optional，旨在创建class时，对注释进行控制 -->
            <commentGenerator>
                <!-- 注释里不添加日期 -->
                <property name="suppressDate" value="true"/>
                <!-- 是否去除自动生成的注释 true：是 ： false:否 -->
                <property name="suppressAllComments" value="true"/>
                <!-- 将数据库中表的字段描述信息添加到注释 -->
                <property name="addRemarkComments" value="true"/>
            </commentGenerator>
    
            <!--jdbc的数据库连接 -->
            <jdbcConnection driverClass="${mybatis.generator.datasource.driver-class-name}"
                            connectionURL="${mybatis.generator.datasource.url}"
                            userId="${mybatis.generator.datasource.username}"
                            password="${mybatis.generator.datasource.password}">
                <property name="remarksReporting" value="true"></property>
            </jdbcConnection>
    
            <!-- 非必需，类型处理器，在数据库类型和java类型之间的转换控制-->
            <javaTypeResolver type="com.github.zoxzo.mbg.extend.resolver.MyJavaTypeResolver">
                <property name="forceBigDecimals" value="false"></property>
            </javaTypeResolver>
    
            <!-- Model模型生成器,用来生成含有主键key的类，记录类 以及查询Example类
                targetPackage     指定生成的model生成所在的包名
                targetProject     指定在该项目下所在的路径
            -->
            <javaModelGenerator targetPackage="com.wandaloans.common.model"
                                targetProject="src/main/java">
                <!-- 是否允许子包，即targetPackage.schemaName.tableName -->
                <property name="enableSubPackages" value="false"/>
                <!-- 是否对类CHAR类型的列的数据进行trim操作 -->
                <property name="trimStrings" value="true"/>
                <!--<property name="constructorBased" value="true"/>-->
            </javaModelGenerator>
    
            <!--Mapper映射文件生成所在的目录 为每一个数据库的表生成对应的SqlMap文件 -->
            <sqlMapGenerator targetPackage="mapper" targetProject="src/main/resources">
                <property name="enableSubPackages" value="false"/>
                <property name="isMergeable" value="false"/>
            </sqlMapGenerator>
    
            <!-- 客户端代码，生成易于使用的针对Model对象和XML配置文件 的代码
                    type="ANNOTATEDMAPPER",生成Java Model 和基于注解的Mapper对象
                    type="MIXEDMAPPER",生成基于注解的Java Model 和相应的Mapper对象
                    type="XMLMAPPER",生成SQLMap XML文件和独立的Mapper接口
            -->
            <javaClientGenerator targetPackage="com.wandaloans.common.mapper" targetProject="src/main/java"
                                 type="XMLMAPPER">
                <property name="enableSubPackages" value="false"/>
            </javaClientGenerator>
            
            <!-- 需要生成的表 -->
            <table schema='WISE' tableName='ts_installment_union_ext' domainObjectName='TsInstallmentUnionExt'
                   enableCountByExample='false' enableUpdateByExample='false' enableDeleteByExample='false'
                   enableSelectByExample='false' selectByExampleQueryId='false'>
            </table>
    
        </context>
    </generatorConfiguration>
    ```
    
    4. mvn clean site deploy
    
    
