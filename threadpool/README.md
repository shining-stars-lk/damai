# `threadpool`线程池
- 提供业务上常用的线程池参数配置形成的线程池
- 此线程池支持打印日志时，能够获取服务追踪链路id。只需引入此组件，调用服务时，请求头传入`requestId`即可。

#使用
## 引入依赖
```xml
<dependency>
  <groupId>com.example</groupId>
  <artifactId>threadpool</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## 提供的api
BusinessThreadPool:
```java
/**
 * 执行任务
 *
 * @param r 提交的任务
 * @return
 */
public static void execute(Runnable r);

/**
* 执行带返回值任务
*
* @param c 提交的任务
* @return
*/
public static <T> Future<T> submit(Callable<T> c);
```