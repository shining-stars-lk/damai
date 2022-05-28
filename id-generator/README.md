# id生成器

**介绍**

- 采取著名的雪花算法思想
- 13位时间戳 + 本机IP（后3位） + DATACENTER_ID(后3位) + TID(后3位) + 单毫秒内的自增序列（2位）
- 引入了`distribute-cache`和`tool`模块，DATACENTER_ID存入redis做自增操作，此操作需要依靠分布式锁保证原子性

**使用**

- 引入依赖
```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>id-generator</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
- 按照`distribute-cache`和`tool`模块的配置方法，将redis和redisson配置好
- 调用`IdUtil.getId()`方法即可

