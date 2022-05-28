# 使用的前置准备工作
## 1 引入依赖
```xml
<dependency>
  <groupId>com.example</groupId>
  <artifactId>tool</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```
## 2 在项目上添加配置
`具体参数与项目中redis相关的配置一致即可`
```yml
redisson:
  # 连接的超时时间
  timeout : 3000
  # 地址
  address : 192.168.2.102
  # 端口号
  port : 6379
  # 密码
  password : 7ygvUJM
  # 选择的redis数据库
  database : 2
  # 连接池大小
  connectionPoolSize : 64
  # 最小空闲连接大小
  connectionMinimumIdleSize : 10
```
# 分布式锁组件的使用

## 注解方式，在相应的方法上加上@DistributedLock注解

### 示例
```java
@DistributedLock(name = "clinic_pay",keys = {"#dto.userId"})
public void testAnnotation(Test1Dto dto) {
    //逻辑省略。。。
}
```
### @DistributedLock注解属性说明

| 属性值        | 类型       |可否为空|      含义     |  备注 |
| :---------   | :--:      |:--:   |:-----------: |:--:|
| lockType     |  LockType |Y      |  锁的类型     |LockType.Reentrant 可重入锁(默认) </br>LockType.Fair 公平锁|
| name     |  String |Y      |  锁的业务名     |如：门诊缴费 clinic_pay|
| keys     |  String[] |N     |  锁的唯一标识  |可指定多个，并支持SpEL表达式|
| waitTime |  long |Y     |  尝试加锁最多等待时间  |默认60s|
| timeUnit |  TimeUnit |Y     |  时间单位  |默认秒|
| customLockTimeoutStrategy |  String |Y     |  自定义加锁超时的处理策略  |此属性填写自定义处理策略方法名(入参和出参保持和加锁方法一致)，如果为空则为快速拒绝策略|
### 补充

- 目前是在方法执行完后将锁释放，没有显示提供锁存活时间配置项，如确定需要的话后续会升级
- 当没有获得锁的请求等待waitTime时长后仍获取不到锁，就会抛出<font color="#FF0000">请求频繁</font>异常
- 为了防止业务名重复可在com.bjgoodwill.msa.common.base.DistributedLockConstants常量类中设置业务名

## 方法级别方式
DistributedLockUtil:
```java
/**
 * 没有返回值的加锁执行
 * @param taskRun 要执行的任务
 * @param name 锁的业务名
 * @param keys 锁的标识
 * */
public void execute(TaskRun taskRun,String name,String [] keys);

/**
* 有返回值的加锁执行
* @param taskCall 要执行的任务
* @param name 锁的业务名
* @param keys 锁的标识
* @return 要执行的任务的返回值
* */
public <T> T submit(TaskCall<T> taskCall,String name,String [] keys)
```
# 防重复提交组件的使用
## 在相应的方法上加上@RepeatLimit注解
### 示例
```java
@RepeatLimit(name = "clinic_pay",keys = {"#id"})
public ResultMap<String> updateUserName(String name, Integer id){
    int i = userMapper.updateName(name,id);
    return ResultMap.success(i > 0 ? "success":"fail");
}
```
**注意**

<font color="#FF0000">如果加此注解的方法返回值是基本数据类型的，要换成对应的包装类型。</font>
### @RepeatLimit注解属性说明
| 属性值        | 类型       |可否为空|      含义     |  备注 |
| :---------   | :--:      |:--:   |:-----------: |:--:|
| name     |  String |Y      |  业务名     |如门诊缴费：clinic_pay|
| keys     |  String[] |Y     |  唯一标识  |可指定多个，并支持SpEL表达式|
| generatorKey |  GenerateKeyStrategy |Y     |  如果不指定key，则执行现有的生成key策略  |GenerateKeyStrategy.PARAMETER_GENERATE_KEY_STRATEGY (唯一标识:类名+方法名+参数名+userId 默认)<font color="#FF0000">(!!!==目前此功能还需完善需要显式指定唯一标识keys==!!!)</font>|
| timeout|  long |Y     |  在设置的时间内返回第一次调用结果  |默认10s,最大允许设置15s|
| repeatRejected|  RepeatRejectedStrategy |Y     |  提交重复时，执行的策略  |RepeatRejectedStrategy.ABORT_STRATEGY(快速拒绝 默认)</br>RepeatRejectedStrategy.SAME_RESULT(返回相同结果)|


### 补充
- `快速拒绝`的策略为抛出<font color="#FF0000">请不要重复提交</font>异常。
- `timeout`参数是在`返回相同结果`的重复执行策略时生效，如果选择了`快速拒绝`策略，`timeout`参数不会生效。而是在方法整个执行期间，再次重复提交都会执行快速拒绝。
- 尽量能够自己来设置唯一标识，如订单id、就诊号、缴费单号
- 如果不能自己来确定唯一标识的话，系统会使用 `类名+方法名+参数名+userId`的方式来生成唯一标识，如果采用返回相同结果的重复执行策略的话，在某些场景会有问题需要使用者考虑清楚。如:
  <font color="#FF0000">某个用户进行提交订单操作，执行的逻辑较长。这时同一个用户再提交一个订单的话，虽然是两个订单，但是在设置的不允许重复访问时间内，第2个订单返回的结果仍然是第1个订单的结果。需要注意。(!!!==目前此功能还需完善需要显式指定唯一标识==!!!)</font>
- 为了防止业务名重复可在com.bjgoodwill.msa.common.base.RepeatLimitConstants常量类中设置业务名