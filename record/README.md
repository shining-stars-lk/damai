# 日志记录行为
为了记录用户的操作行为，将特定场景进行记录，参考美团文章[如何优雅地记录操作日志](https://tech.meituan.com/2021/09/16/operational-logbook.html)
进行了实现
## 使用
### 1引入依赖
```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>record</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
### 2开关
#### 所在的服务配置开关
```yml
record.enabled = true
```
#### request请求头中添加operatorFlag=operatorFlagYes，即可记录该请求
### 3配置es地址
由于最终是上传到es中，所以当启动记录功能后，要配置es的相关信息，详情查看`distribute-es`模块配置
```yml
#es开关
es.switch = true
#elasticsearch.userName=es用户名
elasticsearch.userName = 
#elasticsearch.passWord=es密码
elasticsearch.passWord = 
#es.type.switch=版本
es.type.switch = false
#elasticsearch.ip=es地址
elasticsearch.ip = 
```
### 4在要记录的方法上进行使用
**record注解:**
```java
@Target(value= {ElementType.TYPE, ElementType.METHOD})
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Record {
    
    /**
     * 操作名称
     * */
    String operatorName() default "";
    
    /**
     * 记录内容 
     * 例如：
     * 名字为{getDoctorUserName}的医生,将名字为{getPatientUserName}的患者的订单,添加了备注为(#dto.remark)
     * 
     * 说明1：
     * {}中的内容为方法名，
     * getDoctorUserName和getPatientUserName为方法调用，在使用@Record注解同一类中添加此方法，入参和使用@Record注解的方法相同，返回值必须为String类型
     * 
     * 说明2：
     * ()中的内容为参数名
     * #dto.remark #为固定前缀，dto为入参名，remark为参数中的字段名
     * @return name
     */
    String content() default "";
    
}
```
**举例**
```java
@Record(operatorName = "测试",content = "名字为{getDoctorUserName}的医生,将名字为{getPatientUserName}的患者的订单,添加了备注为(#dto.remark)")
public boolean test(TestDto dto) {
    //执行业务逻辑...
    return true;
}

public String getDoctorUserName(TestDto dto) {
    //查询医生名字逻辑...
    String doctorUserName = getDoctroUserName(dto);
    return doctorUserName;
}

public String getPatientUserName(TestDto dto) {
    //查询患者名字逻辑...
    String patientUserName = getpatientUserName(dto);
    return patientUserName;
}
```
- 说明1：{}中的内容为方法名，`getDoctorUserName`和`getPatientUserName`为方法调用，在使用`@Record`注解的方法所在的类中添加此方法，入参和使用`@Record`注解的方法相同，返回值必须为`String`类型
- 说明2：()中的内容为参数名，`#dto.remark` #为固定前缀，dto为入参名，remark为参数中的字段名
- 说明3：如果记录内容不需要方法和参数进行占位，则直接添加记录内容即可

**TestDto**
```java
public class TestDto {

    //其他字段...
    
    @ApiModelProperty(name = "remark", dataType = "String", value = "remark", required = true)
    private String remark;


    //get/set方法...
}
```
### 结果
```json
{
  "_index": "record",
  "_id": "rm8keIYBGBdmYLWOI1lg",
  "_version": 1,
  "_score": 0,
  "_source": {
    "method": "POST",
    "ip_address": "127.0.0.1",
    "request_uri": "/testcontroller/test",
    "content": "名字为张三的医生,将名字为李四的患者的订单,添加了备注为轻度",
    "result": "true",
    "operator_name": "",
    "@timestamp": 1677052874000,
    "content_type": "application/json",
    "request_body": "{\"serviceCode\":\"2222\",\"haveException\":\"0\",\"doctorUserName\":\"张三\",\"remark\":\"轻度\",\"plat\":\"2\",\"hospitalNo\":\"2222\",\"ocrAccountInfo\":\"1\",\"patientUserName\":\"李四\"}",
    "parameter": "{\"test\":[\"测试\"]}"
  }
}
```