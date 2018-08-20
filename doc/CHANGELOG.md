# 版本更新记录
## 2018-06-20 r1.0.0.7
[fix]分页接口返回结果缺失部分分页关键参数-----已修复;

## 2018-06-06 r1.0.0.6
[优化]1.代码生成发布新版本，优化XML文件配置
[优化]2.数据库中的字段为text或者blob这种大文本类型时，增加sql片段，作特殊处理
[调整]3.分页查询接口增加默认值设置，调整为@RequestParam(required=false,value="pageNum",defaultValue="1") int pageNum, @RequestParam(required=false,value="pageSize",defaultValue="10") int pageSize，即默认加载第1页，每页10条数据

## 2018-05-30 r1.0.0.5
[优化]重写实体类中的toString()方法，简化为 
```
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
```
以方便在自定义修改实体类中属性时，无需再修改toString()方法内容.
注：此方法需要引入fastjson包


## 2018-05-22 r1.0.0.4
[调整]service层、impl层、controller层中save、update、delete、get、list、page等方法前缀统一;
[调整]mapper和对应的dao文件，移除原insert(Record record)和原updateByPrimaryKey(Record record);
	  将原insertSelective(Record record)重命名为insert(Record record);
	  将原updateByPrimaryKeySelective(Record record)重命名为updateByPrimanyKey(Record record);

## 2018-05-14 r1.0.0.3
[fix]JDK8以下环境 分页查询接口实现方法中 匿名内部类使用所传递参数报错问题-已修复 第一个参数增加final关键字修饰，适应jdk7
**特别说明**	
分页插件pagehelper版本要求 5.1+
	pom依赖
	    <dependency>
			<groupId>com.github.pagehelper</groupId>
			<artifactId>pagehelper</artifactId>
			<version>5.1.2</version>
		</dependency>
	mybatis中插件配置
		<!-- com.github.pagehelper 5.1 以上版本 -->
	    <plugin interceptor="com.github.pagehelper.PageInterceptor">
	        <property name="helperDialect" value="mysql"/>
	        <property name="offsetAsPageNum" value="true"/>
	        <property name="rowBoundsWithCount" value="true"/>
	    </plugin>
	


## 2018-05-09 r1.0.0.2
[调整]分页查询接口调整为 jdk6,7方式为首选，jdk8方式为备选
[add]实现类中方法增加@Override注解
[add]列表查询、分页查询接口添加参数，可根据参数进行查询，不传参或参数为空则查询全部。相应mapper实现调整为如下
  <select id="selectAll" resultMap="BaseResultMap" parameterType="pojo.po.AccountPo" >
    select id, group_id, acc_username, acc_password, acc_showname, acc_type, scope_dept, 
    scope_video, valid_token, is_active, modify_time, modify_pwd_time, create_time, invalid_time, 
    access_time
    from t_account
    <where >
       1=1 
      <if test="groupId != null" >
         and group_id = #{groupId,jdbcType=INTEGER}
      </if>
      <if test="accUsername != null" >
         and acc_username = #{accUsername,jdbcType=VARCHAR}
      </if>
      <if test="accPassword != null" >
         and acc_password = #{accPassword,jdbcType=VARCHAR}
      </if>
      <if test="accShowname != null" >
         and acc_showname = #{accShowname,jdbcType=VARCHAR}
      </if>
      <if test="accType != null" >
         and acc_type = #{accType,jdbcType=TINYINT}
      </if>
      <if test="scopeDept != null" >
         and scope_dept = #{scopeDept,jdbcType=VARCHAR}
      </if>
      <if test="scopeVideo != null" >
         and scope_video = #{scopeVideo,jdbcType=VARCHAR}
      </if>
      <if test="validToken != null" >
         and valid_token = #{validToken,jdbcType=VARCHAR}
      </if>
      <if test="isActive != null" >
         and is_active = #{isActive,jdbcType=TINYINT}
      </if>
      <if test="modifyTime != null" >
         and modify_time = #{modifyTime,jdbcType=TIMESTAMP}
      </if>
      <if test="modifyPwdTime != null" >
         and modify_pwd_time = #{modifyPwdTime,jdbcType=TIMESTAMP}
      </if>
      <if test="createTime != null" >
         and create_time = #{createTime,jdbcType=TIMESTAMP}
      </if>
      <if test="invalidTime != null" >
         and invalid_time = #{invalidTime,jdbcType=TIMESTAMP}
      </if>
      <if test="accessTime != null" >
         and access_time = #{accessTime,jdbcType=TIMESTAMP}
      </if>
    </where>
  </select>

## 2018-04-26 r1.0.0.1
[add] 分页增加对jdk 6,7的支持，生成代码形式为
		Page<FilePo> page = PageHelper.startPage(pageNum,pageSize).doSelectPage(()->fileDao.selectAll()); // usage in jdk 8+
	   // Page<FilePo> page = PageHelper.startPage(pageNum,pageSize).doSelectPage(new ISelect() {public void 	doSelect(){fileDao.selectAll();}});//usage in jdk6 or jdk7 
[fix] 修复某些情况下update操作调用WithBLOBs的操作
[优化] 将生成文件导入IDE开发工具中中文注释乱码问题---统一编码格式为UTF-8

## 2018-04-25 r1.0.0.0
版本发布
