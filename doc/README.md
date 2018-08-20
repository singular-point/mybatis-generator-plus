# mybatis-generator-plus开发记录（基于mybatis-generator 拓展）

## 0.eclipse开发环境下如何调试
入口为ShellRunner.java  
右键Run as 选择 run configurations - Java Application - ShellRunner-Arguments-Program arguments 中填写
-configfile mysqlGeneratorConfig.xml -overwrite
可向主函数传递参数

## 1.1 mapper接口增加selectAll()方法

类JavaMapperGenerator.java 
方法 getCompilationUnits()中增加addSelectAllMethod(interfaze);

    protected void addSelectAllMethod(Interface interfaze) {
        AbstractJavaMapperMethodGenerator methodGenerator = new SelectAllMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
SelectAllMethodGenerator.java 62,63行增加,添加参数

        FullyQualifiedJavaType parameterType= introspectedTable.getRules().calculateAllFieldsClass();
        method.addParameter(new Parameter(parameterType, "record")); 

## 1.2 XML文件增加selectAll()具体实现
类XMLMapperGenerator.java  
方法 getSqlMapElement()中增加addSelectAllElement(answer);
具体方式

    protected void addSelectAllElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new SelectAllElementWithSelectiveGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

## 2.实现service层及其实现层
org.mybatis.generator.plugins.MybatisServicePlugin

## 3.实现controller层
org.mybatis.generator.plugins.MybatisControllerPlugin

## 4.增加Po,mapper->dao
IntrospectedTable.java 857行添加Po  
IntrospectedTable.java 820行 ->sb.append("Dao");

## 5.实体类增加中文注释
增加类org.mybatis.generator.codegen.mybatis3.model.MyCommentGenerator  
xml文件中 commentGenerator标签改为如下  
<commentGenerator type="org.mybatis.generator.codegen.mybatis3.model.MyCommentGenerator"/>

## 6.获取数据库表名注释

1.需要单独在org.mybatis.generator.api.FullyQualifiedTable类中添加一个remarks字段，然后创建好get,set方法。 
2.修改org.mybatis.generator.internal.db.DatabaseIntrospector的calculateIntrospectedTables方法添加以下代码  （610-616行）

``` Java
Statement stmt = this.databaseMetaData.getConnection().createStatement();
ResultSet rs = stmt.executeQuery(new StringBuilder().append("SHOW TABLE STATUS LIKE '").append(atn.getTableName()).append("'").toString());
while (rs.next())
table.setRemark(rs.getString("COMMENT"));
closeResultSet(rs);
```
通过introspectedTable.getFullyQualifiedTable().getRemarks();获取表名

## 7.src文件夹不存在时生成文件夹
mybatis-generator-core默认是不给你生成文件夹的，修改为可以自动生成文件夹
org.mybatis.generator.internal.DefaultShellCallback
中注释掉51、52行，添加project.mkdirs();

## 8.生成文件编码格式修改为UTF-8
MyBatisGenerator.java   gjf.getFileEncoding()，修改为UTF-8

# 注意：所配置数据库的表必须设置主键，否则会报错