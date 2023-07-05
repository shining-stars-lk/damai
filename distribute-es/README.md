# 特点
- 针对`springboot`的`spring-boot-starter-data-elasticsearch`,做了再次封装
- 对索引的操作 检查索引、创建索引、删除索引进行了封装
- 对数据的操作 添加数据、查询数据(包含查询分页)进行了封装
# 引入依赖

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>distribute-es</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

# 使用
## 添加配置信息
```yml
#es开关
elasticsearch.esSwitch = true
#elasticsearch.userName=es用户名
elasticsearch.userName = 
#elasticsearch.passWord=es密码
elasticsearch.passWord = 
#兼容低版本type
elasticsearch.esTypeSwitch = false
#elasticsearch.ip=es地址
elasticsearch.ip = 
```

## 引入api

```java
@Autowired
private BusinessEsUtil businessEsUtil;
```
## api使用

直接查看每个api的注释