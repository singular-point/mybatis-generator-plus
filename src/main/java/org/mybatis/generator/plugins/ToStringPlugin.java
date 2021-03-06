/*
 *  Copyright 2012 MyBatis.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class ToStringPlugin extends PluginAdapter {
	 private FullyQualifiedJavaType fastjson;
	 
	public ToStringPlugin() {
        super();
        fastjson = new FullyQualifiedJavaType("com.alibaba.fastjson.JSON"); 
	} 
	 
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }
    
    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }

    
    /**
     * ????????????toString??????
     * @param introspectedTable????????????runtime????????????????????????context???????????????????????????????????????????????????????????????????????????
     * @param topLevelClass?????????????????????????????????????????????????????????DOM??????
     */
    private void generateToString(IntrospectedTable introspectedTable,
            TopLevelClass topLevelClass) {
    	 topLevelClass.addImportedType(fastjson);
        //??????????????????Method????????????????????????Method???org.mybatis.generator.api.dom.java.Method???
    	//??????Method???MBG????????????DOM????????????????????????????????????????????????????????????????????????
        Method method = new Method();
       //?????????????????????????????????public???????????????????????????????????????????????????
        method.setVisibility(JavaVisibility.PUBLIC);
      //????????????????????????????????????????????????????????????returnType?????????FullyQualifiedJavaType??? 
      //??????FullyQualifiedJavaType???MGB??????Java?????????????????????DOM???????????????????????????MBG??????????????????
      //FullyQualifiedJavaType???????????????????????????????????????getStringInstance??????????????????????????????String??????????????????
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        //??????????????????????????????????????????????????????????????????
        method.setName("toString"); //$NON-NLS-1$
        //????????????MBG???????????????????????????Java5???????????????????????????IntrospectedTable?????????????????????????????????????????????????????????
        if (introspectedTable.isJava5Targeted()) {
        	//????????????Java5?????????????????????????????????@Override?????????
            method.addAnnotation("@Override"); //$NON-NLS-1$
        }
        //??????????????????????????????context????????????PluginAdapter???????????????????????????setContext????????????????????????
        //??????getCommentGenerator???????????????????????????????????????addGeneralMethodComment???????????????????????????????????????
        //????????????????????????????????????????????????????????????????????????????????????????????????????????????MBG??????????????????????????????????????????
        //???????????????????????????????????????????????????????????????????????????????????????MBG??????????????????????????????????????????
        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);
      //OK?????????addBodyLine??????????????????????????????
      //??????????????????????????????????????????????????????????????????String????????????method???body????????????  
      //???????????????????????????????????????Servelt??????????????????MBG?????????????????????????????????????????????Servlet????????????String????????????
      //??????????????????????????????????????????Velocity?????????MBG?????????????????????????????????MBG????????????MVC???????????? 
/*      method.addBodyLine("StringBuilder sb = new StringBuilder();"); //$NON-NLS-1$
        method.addBodyLine("sb.append(getClass().getSimpleName());"); //$NON-NLS-1$
        method.addBodyLine("sb.append(\" [\");"); //$NON-NLS-1$
        method.addBodyLine("sb.append(\"Hash = \").append(hashCode());"); //$NON-NLS-1$
        //??????????????????????????????????????????
        StringBuilder sb = new StringBuilder();
        //??????topLevelClass???????????????????????????????????????????????????Field???org.mybatis.generator.api.dom.java.Field
        //??????Field???MBG??????????????????DOM??????
        for (Field field : topLevelClass.getFields()) {
        	//????????????????????????
            String property = field.getName();
            //??????StringBuilder???
            sb.setLength(0);
            //??????????????????????????????
            sb.append("sb.append(\"").append(", ").append(property) //$NON-NLS-1$ //$NON-NLS-2$
                    .append("=\")").append(".append(").append(property) //$NON-NLS-1$ //$NON-NLS-2$
                    .append(");"); //$NON-NLS-1$
            //??????????????????toString?????????????????????????????????????????????????????????????????????????????????????????????toString????????????????????????
            method.addBodyLine(sb.toString());
        }

        method.addBodyLine("sb.append(\"]\");"); //$NON-NLS-1$
*/        method.addBodyLine("return JSON.toJSONString(this);"); //$NON-NLS-1$
        //?????????????????????DOM?????????topLevelClass???????????????????????????
        topLevelClass.addMethod(method);
    }
}
