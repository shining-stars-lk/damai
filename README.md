# cook-frame
cook为烹饪的意思，高度定制和改造，本人又是一个吃货，本项目的特点又是高度定制改造化，所以取名为`cook-frame`
## 项目的优势
**本项目主打的是真实性和独特性，都是根据真实架构和实际遇到的问题而总结出来的**
- 现在大部分面试都需要背八股文，如果长时间不复习，就会忘记了，而`cook-frame`是让这些八股文真正的落地到项目中进行实战，并且真正的在生产中使用过
- 针对实际生产遇到的问题进行`深度定制化的改造`，`是生产中真正用的东西`。这些都是针对业务特点单独设计的，网上也没有现成完整的资料，也不是市面上的八股文宝典或者常见的后台管理或者商城项目，毕竟电商太多人写了
- 对要毕业的大学生、刚参加工作不久的同学、或者工作多年想对技术有提升的小伙伴都有适合的干货
  - 生产中真正的微服务结构到底什么样
  - 实际中项目的接口调用要注意什么
  - 入参和出参到底怎么进行加密，在哪里处理比较好
  - `亮点较多的业务` 例如：Redis、Lua、多种数据结构结合
  - `并发编程的高级玩法` 例如：线程池的定制化设计、ThreadLocal的深度设计
  - `框架深度的改造` 例如：Nacos重写快速服务推送、重写ribbon的负载均衡
  - `各种参数的精细化配置` 例如：Sentinel、Hystrix、Ribbon的配置
- 项目中主打的就是有`独特的亮点`、项目的`真实性`、真实生产中遇到的`重点和难点`把这些独特的东西体现到简历中和面试官来聊，保证对你印象非常的深刻。这不一下子就甩开了别人了吗
- 哪怕是常见的面试问题，也能从生产上遇到的故障来做`进一步的分析和完善`，比如常见的分布式锁，细节就很多。例如：在方法里还是方法外使用？直接Lock.lock就行了吗？事务存在的话需要去考虑吗？如果利用注解那么要考虑其他切面的关系吗？怎么设计锁的超时时间呢？提供哪些策略呢？
- 又比如说SpringBoot的自动装配原理，很多人都背过这个，但究竟用自动装配有什么好处呢？为什么不直接写个组件然后其他项目来依赖呢？两者的区别在哪里呢？在我的课程项目中会得到解答
- 这一道很常见的面试问题但在真实的生产应用中绝对不是那么简单的，要一步一步完善的，而我会把自己的`踩坑经历整理成精华`来分享给参加课程的同学
- 这些实实在在的真正在项目中遇到的问题和经验都会整理成文档和在这个项目中，来供同学们学习，我敢肯定哪怕有人干了很多年，依旧能学到很多很多的干货，也能帮着解决自己公司的项目中存在的问题顺便也能提高KPI了，哈哈。
- 项目配套的文档把结构和重难点都分层次的列举了出来，并且还配有流程图，方便小伙伴能更清晰的理解
- 后续还会继续的更新，把遇到的重点、难点更新到项目中

# 架构图
![](https://files.mdnice.com/user/12133/e4a4cc47-8faa-45a2-a776-8d199801ea27.jpg)
# 功能简介
![](https://files.mdnice.com/user/12133/932a3fb8-246d-41ff-bf90-606e5b45324c.png)
# 结构模块

# 技术选型
| 技术                 | 说明                | 官网                                           |
| ------------------- | ------------------- | ---------------------------------------------- |
| Spring-Boot         | Web服务框架   | https://spring.io/projects/spring-boot|
| Spring-Cloud        | 微服务框架     | https://spring.io/projects/spring-cloud |
| Spring-Cloud-alibaba| alibaba微服务框架 | https://github.com/alibaba/spring-cloud-alibaba |
| Spring-Cloud-Gateway| 微服务网关    | 	https://spring.io/projects/spring-cloud-gateway |
| Nacos               | 服务注册中心       | https://nacos.io/zh-cn/index.html |
| Sentinel            | 服务熔断       | https://sentinelguard.io/zh-cn/ |
| Log4j2              | 日志框架       | https://github.com/apache/logging-log4j2|
| MyBatis-Plus        | ORM框架             | https://baomidou.com |
| MyBatisGenerator    | 数据层代码生成器     | http://www.mybatis.org/generator/index.html|
| Kafka               | 消息队列            | https://github.com/apache/kafka/|
| Redis               | 分布式缓存         | https://redis.io/ |    
| Redisson            | 分布式Redis工具         | https://redisson.org |   
| Elasticsearch       | 搜索引擎            | https://github.com/elastic/elasticsearch |
| LogStash            | 日志收集工具        | https://github.com/elastic/logstash|
| Kibana              | 日志可视化查看工具  | https://github.com/elastic/kibana|
| Nginx               | 静态资源服务器      | https://www.nginx.com/ |
| Docker              | 应用容器引擎        | https://www.docker.com  |
| Jenkins             | 自动化部署工具      | https://github.com/jenkinsci/jenkins|
| Druid               | 数据库连接池        | https://github.com/alibaba/druid |
| MinIO               | 对象存储            | https://github.com/minio/minio |
| JWT                 | JWT登录支持         | https://github.com/jwtk/jjwt  |
| Lombok              | Java语言增强插件 | https://github.com/rzwitserloot/lombok |
| Hutool              | Java工具类库        | https://github.com/looly/hutool |
| Swagger-UI          | API文档生成工具      | https://github.com/swagger-api/swagger-ui |
| Knife4j             | Swagger 增强框架    | https://doc.xiaominfo.com |
| Hibernator-Validator| 验证框架           | http://hibernate.org/validator |
| XXL-Job| 分布式定时任务框架           | 	http://www.xuxueli.com/xxl-job |
# 总结的技术文档(公开 密码:nmue)
![](https://files.mdnice.com/user/12133/3f343fc9-5990-42ea-a04b-550dc3bc71ad.png)
# 项目和重点复杂问题总结文档(联系本人获取)
![](https://files.mdnice.com/user/12133/adacb4e2-161e-4fbf-b358-0ae686036a73.png)
# 联系
对项目有什么疑问解决或者获取项目文档或者关注后续的更新，可以联系星哥本人，大家也可以一起加群讨论交流。**扫二维码，备注cook**
![](https://files.mdnice.com/user/12133/eeef668c-3263-42a9-bd7a-363cf3142e4a.JPG =40%x)
