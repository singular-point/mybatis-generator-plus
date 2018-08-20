package org.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * 给dao增加注解
 * 
 * @author yuanjk
 * @date 2018-04-14 14:33:48
 */
public class MapperAnnotationPlugin extends PluginAdapter {
	private FullyQualifiedJavaType Repository;

	public MapperAnnotationPlugin() {
		super();
		Repository = new FullyQualifiedJavaType("org.springframework.stereotype.Repository"); 
	}

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		
        interfaze.addImportedType(Repository); 
        interfaze.addAnnotation("@Repository"); 
		return true;
	}


}
