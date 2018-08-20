package org.mybatis.generator.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;

/**generate controller class
 * 
 * @author yf-yuanjingkun
 *
 */
public class MybatisControllerPlugin extends PluginAdapter{
	
	private FullyQualifiedJavaType Page;
	private FullyQualifiedJavaType PageInfo;
	private FullyQualifiedJavaType slf4jLogger;
	private FullyQualifiedJavaType slf4jLoggerFactory;
	private FullyQualifiedJavaType APIRespJson;
	private FullyQualifiedJavaType APIObjectJson;
	private FullyQualifiedJavaType APIListJson;
	private FullyQualifiedJavaType serviceType;
	private FullyQualifiedJavaType daoType;
	private FullyQualifiedJavaType interfaceType;
	private FullyQualifiedJavaType pojoType;
	private FullyQualifiedJavaType pojoCriteriaType;
	private FullyQualifiedJavaType listType;
	private FullyQualifiedJavaType Autowired;
	private FullyQualifiedJavaType service;
	private FullyQualifiedJavaType RestController;
	private FullyQualifiedJavaType RequestMapping;
	private FullyQualifiedJavaType returnType;
	private FullyQualifiedJavaType ModelAttribute;
	private FullyQualifiedJavaType RequestParam;
	private String servicePack;
	private String controllerlPack;
	private String project;
	private String pojoUrl;
	private String tableRemark;//数据库表名注释
	private String dataBaseTableName;//数据库表名
	
	/**
	 * 所有的方法
	 */
	private List<Method> methods;
	/**
	 * 是否添加注解
	 */
	private boolean enableAnnotation = true;
	private boolean enableSave = true;
	private boolean enableDelete = true;
	private boolean enableUpdate = true;
	private boolean enableGet = true;
	private boolean enableList = true;
	private boolean enablePage = true;
	
	public MybatisControllerPlugin() {
		super();
		slf4jLogger = new FullyQualifiedJavaType("org.slf4j.Logger");
		slf4jLoggerFactory = new FullyQualifiedJavaType("org.slf4j.LoggerFactory");
		APIRespJson = new FullyQualifiedJavaType("cn.com.scooper.common.resp.APIRespJson");
		APIObjectJson = new FullyQualifiedJavaType("cn.com.scooper.common.resp.APIObjectJson");
		APIListJson = new FullyQualifiedJavaType("cn.com.scooper.common.resp.APIListJson");
		Page = new FullyQualifiedJavaType("com.github.pagehelper.Page");
		PageInfo = new FullyQualifiedJavaType("com.github.pagehelper.PageInfo");
		methods = new ArrayList<Method>();
	}

	/**
	 * 读取配置文件
	 */
	@Override
	public boolean validate(List<String> warnings) {
		
		String enableAnnotation = properties.getProperty("enableAnnotation");
		
		String enableSave = properties.getProperty("enableSave");
		
		String enableDelete = properties.getProperty("enableDelete");
		
		String enableUpdate = properties.getProperty("enableUpdate");
		
		String enableGet = properties.getProperty("enableUpdate");
		
		String enableList = properties.getProperty("enableList");
		
		String enablePage = properties.getProperty("enablePage");
		
		 
		if (StringUtility.stringHasValue(enableAnnotation)){
			this.enableAnnotation = StringUtility.isTrue(enableAnnotation);
		}
		
		if (StringUtility.stringHasValue(enableSave)){
			this.enableSave = StringUtility.isTrue(enableSave);
		}
		
		if (StringUtility.stringHasValue(enableDelete)){
			this.enableDelete = StringUtility.isTrue(enableDelete);
		}
		
		if (StringUtility.stringHasValue(enableUpdate)){
			this.enableUpdate = StringUtility.isTrue(enableUpdate);
		}
		
		if (StringUtility.stringHasValue(enableGet)){
			this.enableGet = StringUtility.isTrue(enableGet);
		}
		
		if (StringUtility.stringHasValue(enableList)){
			this.enableList = StringUtility.isTrue(enableList);
		}
		
		if (StringUtility.stringHasValue(enablePage)){
			this.enablePage = StringUtility.isTrue(enablePage);
		}
		
		this.servicePack = properties.getProperty("servicePackage");
		this.controllerlPack = properties.getProperty("controllerPackage");
		this.project = properties.getProperty("targetProject"); 
		this.pojoUrl = context.getJavaModelGeneratorConfiguration().getTargetPackage();

		if (this.enableAnnotation) {
			Autowired = new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired");
			RestController = new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RestController");
			ModelAttribute = new FullyQualifiedJavaType("org.springframework.web.bind.annotation.ModelAttribute");
			RequestParam = new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestParam");
			RequestMapping = new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestMapping");
		}
		return true;
	}

	/**
	 * 
	 */
	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
		
		List<GeneratedJavaFile> files = new ArrayList<GeneratedJavaFile>();
		
		String table = introspectedTable.getBaseRecordType();//com.rainyn.domain.Account
		
		String tableNameWithPo = table.replaceAll(this.pojoUrl + ".", "").trim();//AccountPo
		
//		String tableName = table.replaceAll(this.pojoUrl + ".", "").replaceAll("Po", "").trim();//Account
		
		String tableName = tableNameWithPo.substring(0, tableNameWithPo.length()-2 );//Account
	
		interfaceType = new FullyQualifiedJavaType(servicePack + "." + tableName + "Service");//com.rainyn.service.AccountService
		
		daoType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());//com.rainyn.mapper.AccountMapper
		
		serviceType = new FullyQualifiedJavaType(controllerlPack + "." + tableName + "Controller");//com.rainyn.service.impl.AccountServiceImpl
		
		pojoType = new FullyQualifiedJavaType(pojoUrl + "." + tableNameWithPo);//com.rainyn.service.impl.AccountServiceImpl
		
		pojoCriteriaType = new FullyQualifiedJavaType(pojoUrl + "."  + tableName + "Criteria");//com.rainyn.domain.AccountCriteria
		
		listType = new FullyQualifiedJavaType("java.util.List");
		
		Interface interface1 = new Interface(interfaceType);
		
		TopLevelClass topLevelClass = new TopLevelClass(serviceType);
		
		// 导入必须的类
		addImport(interface1, topLevelClass);

		// controller类生成
		addController(topLevelClass,introspectedTable, tableName,files);
		
		// 日志类
		addLogger(topLevelClass);

		return files;
	}

	/**
	 * add Controller class
	 * @param introspectedTable
	 * @param tableName
	 * @param files
	 */
	protected void addController(TopLevelClass topLevelClass,IntrospectedTable introspectedTable, String tableName, List<GeneratedJavaFile> files) {
		
	    tableRemark = introspectedTable.getFullyQualifiedTable().getRemarks().replaceAll("表","");
	    
		dataBaseTableName = introspectedTable.getFullyQualifiedTable().getIntrospectedTableName();
		
		topLevelClass.setVisibility(JavaVisibility.PUBLIC);
		
		addField(topLevelClass, tableName);
		
		topLevelClass.addAnnotation("@RestController");
		
		topLevelClass.addAnnotation("@RequestMapping(\"/data/"+toLowerCase(pojoType.getShortName().substring(0, pojoType.getShortName().length()-2))+"\")");
	
		topLevelClass.addMethod(getOtherGetboolean("get"+tableName, introspectedTable, tableName));
	
		topLevelClass.addMethod(getOtherSaveboolean("save"+tableName, introspectedTable, tableName));
	
		topLevelClass.addMethod(getOtherDeleteboolean("remove"+tableName, introspectedTable, tableName));
	
		topLevelClass.addMethod(getOtherUpdateboolean("update"+tableName, introspectedTable, tableName));
	
		topLevelClass.addMethod(getOtherListboolean("list"+tableName, introspectedTable, tableName));
	
		topLevelClass.addMethod(getOtherPageboolean("page"+tableName, introspectedTable, tableName));
		
		GeneratedJavaFile file = new GeneratedJavaFile(topLevelClass, project, context.getJavaFormatter());
		
		files.add(file);
	}

	/**
	 * 添加字段
	 * 
	 * @param topLevelClass
	 */
	protected void addField(TopLevelClass topLevelClass, String tableName) {
		// add service
		Field field = new Field();
		field.setName(toLowerCase(interfaceType.getShortName()));
		topLevelClass.addImportedType(interfaceType);
		field.setType(interfaceType); 
		field.setVisibility(JavaVisibility.PRIVATE);
		if (enableAnnotation) {
			field.addAnnotation("@Autowired");
		}
		topLevelClass.addField(field);
	}

	/**
	 *  method save
	 * @param methodName
	 * @param introspectedTable
	 * @param tableName
	 * @return
	 */
	protected Method getOtherSaveboolean(String methodName, IntrospectedTable introspectedTable, String tableName) {
		Method method = new Method();
		method.setName(methodName);
		method.setReturnType(APIRespJson);
		method.addParameter(new Parameter(pojoType, toLowerCase(pojoType.getShortName()),"@ModelAttribute"));
		method.setVisibility(JavaVisibility.PUBLIC);
		method.addAnnotation("@RequestMapping(\"/"+methodName+"\")");
		StringBuilder sb = new StringBuilder();
		sb.append(toLowerCase(interfaceType.getShortName())+".");
		sb.append(methodName);
		sb.append("(");
		sb.append(toLowerCase(pojoType.getShortName()));
		sb.append(");");
		sb.append("\r\n\t\t");
		sb.append("return new APIRespJson();");
		method.addBodyLine(sb.toString());
		addMethodComment(method,"插入单个"+(tableRemark.length() == 0 ? pojoType.getShortName():tableRemark));
		return method;
	}

	/** 
	 * method delete
	 * @param methodName
	 * @param introspectedTable
	 * @param tableName
	 * @return
	 */
	protected Method getOtherDeleteboolean(String methodName, IntrospectedTable introspectedTable, String tableName) {
		Method method = new Method();
		method.setName(methodName);
		method.setReturnType(APIRespJson);
		method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "id","@RequestParam"));
		method.setVisibility(JavaVisibility.PUBLIC);
		method.addAnnotation("@RequestMapping(\"/"+methodName+"\")");
		StringBuilder sb = new StringBuilder();
		sb.append(toLowerCase(interfaceType.getShortName())+".");
		sb.append(methodName);
		sb.append("(");
		sb.append("id");
		sb.append(");");
		sb.append("\r\n\t\t");
		sb.append("return new APIRespJson();");
		method.addBodyLine(sb.toString());
		addMethodComment(method,"根据id删除"+(tableRemark.length() == 0 ? pojoType.getShortName():tableRemark));
		return method;
	}
	
	/**
	 * method update
	 * @param methodName
	 * @param introspectedTable
	 * @param tableName
	 * @return
	 */
	protected Method getOtherUpdateboolean(String methodName, IntrospectedTable introspectedTable, String tableName) {
		Method method = new Method();
		method.setName(methodName);
		method.setReturnType(APIRespJson);
		method.addParameter(new Parameter(pojoType, toLowerCase(pojoType.getShortName()),"@ModelAttribute"));
		method.setVisibility(JavaVisibility.PUBLIC);
		method.addAnnotation("@RequestMapping(\"/"+methodName+"\")");
		StringBuilder sb = new StringBuilder();
		sb.append(toLowerCase(interfaceType.getShortName())+".");
		sb.append(methodName);
		sb.append("(");
		sb.append(toLowerCase(pojoType.getShortName()));
		sb.append(");");
		sb.append("\r\n\t\t");
		sb.append("return new APIRespJson();");
		method.addBodyLine(sb.toString());
		addMethodComment(method,"修改"+(tableRemark.length() == 0 ? pojoType.getShortName():tableRemark));
		return method;
	}
	
	protected Method getOtherGetboolean(String methodName, IntrospectedTable introspectedTable, String tableName) {
		Method method = new Method();
		method.setName(methodName);
		method.setReturnType(APIRespJson);
		method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "id","@RequestParam"));
		method.setVisibility(JavaVisibility.PUBLIC);
		method.addAnnotation("@RequestMapping(\"/"+methodName+"\")");
		StringBuilder sb = new StringBuilder();
		sb.append(pojoType.getShortName());
		sb.append(" "+toLowerCase(pojoType.getShortName()));
		sb.append(" = ");
		sb.append(toLowerCase(interfaceType.getShortName())+".");
		sb.append(methodName); 
		sb.append("(");
		sb.append("id");
		sb.append(");");
		sb.append("\r\n\t\t");
		sb.append("return new APIObjectJson("+toLowerCase(pojoType.getShortName())+");");
		method.addBodyLine(sb.toString());
		addMethodComment(method,"获取单个"+(tableRemark.length() == 0 ? pojoType.getShortName():tableRemark));
		return method;
	}
	
	
	protected Method getOtherListboolean(String methodName, IntrospectedTable introspectedTable, String tableName) {
		Method method = new Method();
		method.setName(methodName);
		method.setReturnType(APIRespJson);
		method.addParameter(new Parameter(pojoType, toLowerCase(pojoType.getShortName()),"@ModelAttribute"));
		method.setVisibility(JavaVisibility.PUBLIC);
		method.addAnnotation("@RequestMapping(\"/"+methodName+"\")");
		StringBuilder sb = new StringBuilder();
		sb.append("List<"+pojoType.getShortName()+">");
		sb.append(" "+toLowerCase(pojoType.getShortName())+"List");
		sb.append(" = ");
		sb.append(toLowerCase(interfaceType.getShortName())+".");
		sb.append("get"+pojoType.getShortName().substring(0, pojoType.getShortName().length()-2)+"List");
		sb.append("("+toLowerCase(pojoType.getShortName())+");");
		sb.append("\r\n\t\t");
		sb.append("return new APIListJson<>("+toLowerCase(pojoType.getShortName())+"List"+");");
		method.addBodyLine(sb.toString());
		addMethodComment(method,"获取多个"+(tableRemark.length() == 0 ? pojoType.getShortName():tableRemark));
		return method;
	}
	
	protected Method getOtherPageboolean(String methodName, IntrospectedTable introspectedTable, String tableName) {
		Method method = new Method();
		method.setName(methodName);
		method.setReturnType(APIRespJson);
		method.addParameter(new Parameter(pojoType, toLowerCase(pojoType.getShortName()),"@ModelAttribute"));
		method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "pageNum","@RequestParam(required=false,value=\"pageNum\",defaultValue=\"1\")"));
		method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "pageSize","@RequestParam(required=false,value=\"pageSize\",defaultValue=\"10\")"));
		method.setVisibility(JavaVisibility.PUBLIC);
		method.addAnnotation("@RequestMapping(\"/"+methodName+"\")");
		StringBuilder sb = new StringBuilder();
		sb.append("Page<"+pojoType.getShortName()+">");
		sb.append(" "+toLowerCase(pojoType.getShortName())+"Page");
		sb.append(" = ");
		sb.append(toLowerCase(interfaceType.getShortName())+".");
		sb.append("get"+pojoType.getShortName().substring(0, pojoType.getShortName().length()-2)+"Page");
		sb.append("("+toLowerCase(pojoType.getShortName())+",pageNum"+",pageSize"+");");
		sb.append("\r\n\t\t");
		sb.append("PageInfo<"+pojoType.getShortName()+">");
		sb.append(" "+"pageInfo");
		sb.append(" = ");
		sb.append("new PageInfo<>("+toLowerCase(pojoType.getShortName())+"Page);");
		sb.append("\r\n\t\t");
		sb.append("return new APIObjectJson(pageInfo);");
		method.addBodyLine(sb.toString());
		addMethodComment(method,"分页获取多个"+(tableRemark.length() == 0 ? pojoType.getShortName():tableRemark));
		return method;
	}
	
	/**
	 * type: pojo 1 key 2 example 3 pojo+example 4
	 */
	protected String addParams(IntrospectedTable introspectedTable, Method method, int type1) {
		switch (type1) {
		case 1:
			method.addParameter(new Parameter(pojoType, "record"));
			return "record";
		case 2:
			if (introspectedTable.getRules().generatePrimaryKeyClass()) {
				FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
				method.addParameter(new Parameter(type, "key"));
			} else {
				for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
					FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
					method.addParameter(new Parameter(type, introspectedColumn.getJavaProperty()));
				}
			}
			StringBuffer sb = new StringBuffer();
			for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
				sb.append(introspectedColumn.getJavaProperty());
				sb.append(",");
			}
			sb.setLength(sb.length() - 1);
			return sb.toString();
		case 3:
			method.addParameter(new Parameter(pojoCriteriaType, "example"));
			return "example";
		case 4:
			method.addParameter(0, new Parameter(pojoType, "record"));
			method.addParameter(1, new Parameter(pojoCriteriaType, "example"));
			return "record, example";
		default:
			break;
		}
		return null;
	}

	protected void addComment(JavaElement field, String comment) {
		StringBuilder sb = new StringBuilder();
		field.addJavaDocLine("/**");
		sb.append(" * ");
		comment = comment.replaceAll("\n", "<br>\n\t * ");
		sb.append(comment);
		field.addJavaDocLine(sb.toString());
		field.addJavaDocLine(" */");
	}

	/**
	 * add field
	 * 
	 * @param topLevelClass
	 */
	protected void addField(TopLevelClass topLevelClass) {
		// add success
		Field field = new Field();
		field.setName("success"); // set var name
		field.setType(FullyQualifiedJavaType.getBooleanPrimitiveInstance()); // type
		field.setVisibility(JavaVisibility.PRIVATE);
		addComment(field, "excute result");
		topLevelClass.addField(field);
		// set result
		field = new Field();
		field.setName("message"); // set result
		field.setType(FullyQualifiedJavaType.getStringInstance()); // type
		field.setVisibility(JavaVisibility.PRIVATE);
		addComment(field, "message result");
		topLevelClass.addField(field);
	}

	/**
	 * add method
	 * 
	 */
	protected void addMethod(TopLevelClass topLevelClass) {
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("setSuccess");
		method.addParameter(new Parameter(FullyQualifiedJavaType.getBooleanPrimitiveInstance(), "success"));
		method.addBodyLine("this.success = success;");
		topLevelClass.addMethod(method);

		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
		method.setName("isSuccess");
		method.addBodyLine("return success;");
		topLevelClass.addMethod(method);

		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("setMessage");
		method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "message"));
		method.addBodyLine("this.message = message;");
		topLevelClass.addMethod(method);

		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getStringInstance());
		method.setName("getMessage");
		method.addBodyLine("return message;");
		topLevelClass.addMethod(method);
	}

	/**
	 * add method
	 * 
	 */
	protected void addMethod(TopLevelClass topLevelClass, String tableName) {
		Method method2 = new Method();
		for (int i = 0; i < methods.size(); i++) {
			Method method = new Method();
			method2 = methods.get(i);
			method = method2;
			method.removeAllBodyLines();
			method.removeAnnotation();
			StringBuilder sb = new StringBuilder();
			sb.append("return this.");
			sb.append(getDaoShort());
			sb.append(method.getName());
			sb.append("(");
			List<Parameter> list = method.getParameters();
			for (int j = 0; j < list.size(); j++) {
				sb.append(list.get(j).getName());
				sb.append(",");
			}
			sb.setLength(sb.length() - 1);
			sb.append(");");
			method.addBodyLine(sb.toString());
			topLevelClass.addMethod(method);
		}
		methods.clear();
	}

	/**
	 * BaseUsers to baseUsers
	 * @param tableName
	 * @return
	 */
	protected String toLowerCase(String tableName) {
		StringBuilder sb = new StringBuilder(tableName);
		sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		return sb.toString();
	}

	/**
	 * BaseUsers to baseUsers
	 * 
	 * @param tableName
	 * @return
	 */
	protected String toUpperCase(String tableName){
		StringBuilder sb = new StringBuilder(tableName);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		return sb.toString();
	}

	/**
	 * import must class
	 */
	private void addImport(Interface interfaces, TopLevelClass topLevelClass) {
		interfaces.addImportedType(pojoType);
		interfaces.addImportedType(listType);
		topLevelClass.addImportedType(listType);
		topLevelClass.addImportedType(interfaceType);
		topLevelClass.addImportedType(pojoType);
		topLevelClass.addImportedType(Page);
		topLevelClass.addImportedType(PageInfo);
		topLevelClass.addImportedType(slf4jLogger);
		topLevelClass.addImportedType(slf4jLoggerFactory);
		topLevelClass.addImportedType(APIListJson);
		topLevelClass.addImportedType(APIObjectJson);
		topLevelClass.addImportedType(APIRespJson);
		if (enableAnnotation) {
			topLevelClass.addImportedType(RestController);
			topLevelClass.addImportedType(RequestMapping);
			topLevelClass.addImportedType(Autowired);
			topLevelClass.addImportedType(ModelAttribute);
			topLevelClass.addImportedType(RequestParam);
		}
	}

	/**
	 * import logger
	 */
	private void addLogger(TopLevelClass topLevelClass) {
		Field field = new Field();
		field.setFinal(true);
		field.setInitializationString("LoggerFactory.getLogger(" + topLevelClass.getType().getShortName() + ".class)"); // set value
		field.setName("logger"); 
		field.setStatic(true);
		field.setType(new FullyQualifiedJavaType("Logger")); 
		field.setVisibility(JavaVisibility.PRIVATE);
		topLevelClass.addField(field);
	}

	private String getDaoShort() {
		return toLowerCase(daoType.getShortName()) + ".";
	}
	
	public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		returnType = method.getReturnType();
		return true;
	}
	
    /**
     * 添加方法注释
     * @param method
     * @param methodComment
     * @return
     */
    private Method addMethodComment(Method method,String methodComment) {
		
        method.addJavaDocLine("/**");
        StringBuilder sb = new StringBuilder();
        sb.append(" * "+methodComment);
        sb.append("\r\n\t");
        List<Parameter> parametersList = method.getParameters();
        for(Parameter parm : parametersList){
        	sb.append(" * @param ");
        	sb.append(parm.getName());
        	sb.append("\r\n\t");
        }
        sb.append(" * @return");
        sb.append(" "+method.getReturnType().getShortName());
        method.addJavaDocLine(sb.toString());
        method.addJavaDocLine(" */");
        return method;
    }
	
}
