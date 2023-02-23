# 特点
- 针对`springboot`的`spring-boot-starter-data-elasticsearch`,做了再次封装
- 对索引的操作 检查索引、创建索引、删除索引进行了封装
- 对数据的操作 添加数据、查询数据(包含查询分页)进行了封装
# 引入依赖

```xml
<groupId>com.example</groupId>
<artifactId>distribute-es</artifactId>
<version><version>0.0.1-SNAPSHOT</version></version>
```

# 使用

## 引入api

```java
@Autowired
private BusinessEsUtil businessEsUtil;
```

## 开关

```java
//使用开关，默认打开
@Value("${es.switch:true}")
```

```java
//是否需要type，默认打开(es6、7版本需要type es8不需要type)
@Value("${es.type.switch:true}")
```

## api使用

直接查看每个api的注释