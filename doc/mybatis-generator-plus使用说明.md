##  1.JDK要求
### JDK 1.7+(含1.7)

##  2.配置项
###  1.1 jdbcConnection标签中配置connectionURL，进行数据库连接配置
###  1.2 table标签配置映射数据表，可配置多个表

##  3.运行方式
###  双击"自动生成代码.bat",在src文件夹下会生成controller、dao、implement、pojo、resources、service等文件夹，相应层的代码位于其下

##  4.注意
###   4.1 分页插件pagehelper版本要求 5.1+ 
	pom依赖
	    <dependency>
			<groupId>com.github.pagehelper</groupId>
			<artifactId>pagehelper</artifactId>
			<version>5.1.2</version>
		</dependency>
	mybatis插件配置
		<!-- com.github.pagehelper 5.1 以上版本 -->
	    <plugin interceptor="com.github.pagehelper.PageInterceptor">
	        <property name="helperDialect" value="mysql"/>
	        <property name="offsetAsPageNum" value="true"/>
	        <property name="rowBoundsWithCount" value="true"/>
	    </plugin>
###   4.2 数据库表必须设置主键，否则会报错
###   4.3 如果数据库中的字段为text或者blob这种大文本类型，有两种处理方式：
	  
	  1).mysqlGeneratorConfig.xml 映射表数据<table>标签中不使用<columnOverride> 标签：
	    工具自动生成代码的时候会在XXXmapper.xml中生成一个新的sql片段，如
	```
	 <resultMap id="ResultMapWithBLOBs" type="cn.com.scooper.pojo.po.base.NotePo" extends="BaseResultMap" >
       <result column="note_data" property="noteData" jdbcType="LONGVARCHAR" />
     </resultMap>
    ```
	
	  2).mysqlGeneratorConfig.xml 映射表数据<table>标签中使用<columnOverride> 标签:
	  在table标签中添加<columnOverride column="note_data" jdbcType="VARCHAR"/>，column为你需要覆盖的数据库字段，jdbcType为指定的类型
