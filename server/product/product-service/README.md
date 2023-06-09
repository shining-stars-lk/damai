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
    String content();
    
    /**
     * 失败记录内容
     * 例如：
     * 名字为{getDoctorUserName}的医生,添加备注为(#dto.remark)的操作失败，失败原因为(#errMsg)
     *
     * 说明：
     * (#errMsg)为固定写法，错误信息进行占位 内容为ResultMap.errCode !=0 的情况下 ResultMap.errMsg的信息
     * */
    String failContent() default "";

}
```
**IParseFunction**

当`Record`的注解中content的内容含有方法占位符时，需要来实现`IParseFunction`接口，然后使用`@Component`注解注入到spring中即可

```java
public interface IParseFunction<T> {

    /**
     * 是否在执行真正的方法前调用此占位符的方法
     * true为是
     * false为否 也就是在执行真正的方法后调用此占位符的方法
     * */
    default boolean executeBefore() {
        return true;
    }

    /**
     * 方法名称 和{}中的方法名字相同
     * */
    String functionName();

    /**
     * 执行的具体逻辑
     * T的类型要和记录的方法入参相同
     * */
    String apply(T t);
}
```
#### 举例

**TestDto**

```java
public class TestDto {

    //其他字段...

    @ApiModelProperty(name = "remark", dataType = "String", value = "remark", required = true)
    private String remark;


    //get/set方法...
}
```

**TestService**

- operatorName 操作名称

- content 操作记录

    - 说明1：{}中的内容为方法名，`getDoctorUserName`和`getPatientUserName`为方法调用，需要实现`IParseFunction`接口来实现`apply`方法，入参和使用`@Record`注解的方法相同，返回固定为`String`类型

    - 说明2：()中的内容为参数名，`#dto.remark` #为固定前缀，dto为入参名，remark为参数中的字段名

    - 说明3：如果记录内容不需要方法和参数进行占位，则直接添加记录内容即可

- failContent 操作失败记录

    - content中的说明在此同样适用，不再赘述
    - (#errMsg)为固定写法，错误信息进行占位，取异常信息。没有则不记录failContent

```java
@RequestMapping(value = "/testrecord", method = RequestMethod.POST)
@Record(operatorName = "测试",content = "名字为{getDoctorUserName}的医生,将名字为{getPatientUserName}的患者的订单,添加了备注为(#dto.remark)",
            failContent = "名字为{getDoctorUserName}的医生,添加备注为(#dto.remark)的操作失败，失败原因为(#errMsg)")
public boolean testRecord(@Valid @RequestBody TestDto dto) {
    return testService.testRecord(dto);
}
```

**DoctorUserName**

```java
@Component
public class DoctorUserName implements IParseFunction<TestDto> {

  @Override
  public boolean executeBefore() {
      return true;
  }

  @Override
  public String functionName() {
      return "getDoctorUserName";
  }

  @Override
  public String apply(TestDto dto) {
      //查询医生名字逻辑...
      String doctorUserName = getDoctroUserName(dto);
      return doctorUserName;
  }
}
```

**PatientUserName**

```java
@Component
public class PatientUserName implements IParseFunction<TestDto> {

  @Override
  public boolean executeBefore() {
      return false;
  }

  @Override
  public String functionName() {
      return "getPatientUserName";
  }

  @Override
  public String apply(TestDto dto) {
      //查询患者名字逻辑...
      String patientUserName = getpatientUserName(dto);
      return patientUserName;
  }
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