# 概述
1. 现在市场绝对多数使用的服务架构体系是微服务，大概是`Netflix`和`alibaba`两种。在服务发布新版本时基本需要选择用户量很少的情况下，比如晚上或者半夜，这样是因为在发布服务时，会重新构建服务，服务会发生不可用情况。
2. 因为在这个过程中，由于`ribbon`负载均衡的缓存服务机制每隔`30s`去拉取注册中心的服务列表。而服务注册中心`eureka`或`nacos`的缓存机制。`eurka`是每隔15s去服务端更新自己的服务列表，`nacos`是每隔6s。在这段时间内当服务进行上下线或者扩缩容时，由于没有及时的更新，就会导致服务不可用的情况。本文就是为了解决此问题。

# 版本
1. `spring-boot`:2.3.12.RELEASE
2. `spring-cloud`:Hoxton.SR12
3. `spring-cloud-starter-alibaba-nacos-discovery`:2.2.7.RELEASE
4. `nacos-common`:2.0.3
5. `nacos服务端`:2.0.3

# 流程
- 客户端
    - 客户端的容器启动后，使用`nacos`的订阅机制
    - 客户端向`nacos`注册服务
- 服务端
    - 服务端接收到客户端的服务注册/注销请求后，循环所有的服务列表，包装成一个个`PushDelayTask`任务放入`blockingQueue`队列
    - 一个线程中一直循环不断的从`blockingQueue`队列取出任务，然后一个个的执行通知每个客户端(利用了nacos本身的长连接机制，1.x版本为http伪长连接 2.x版本为grpc真正的长连接)
    - 这时，客户端的订阅机制就会接收到相应，进行刷新`nacos`和`ribbon`缓存
- 客户端刷新`nacos`和`ribbon`缓存机制
    - 将`nacos`缓存进行清空
    - 循环更新每个`fegin`服务的`ribbon`服务缓存
        - 每个`fegin`服务会重新的调用`nacos`服务然后更新到`ribbon`自己的缓存中

通过以上的流程，就可以实现当有服务上线或者下线后，其他的服务能立刻的更新到最近的服务列表。

**但在使用Jenkins构建服务时，还会存在问题。**
当新的docker服务容器服务上线后，这时旧的docker容器会下线，服务会从`nacos`注销。这时如果之前已经有流量请求在旧的docker容器服务中还在执行中的话，这些请求就会失败。

# 改进
**为了应对上述存在的旧请求执行失败的问题，进行如下的改进：**
1. 在容器销毁前加入生命周期的插件，在调用容器销毁前先调用一个方法，此方法作用是从nacos上注销自己服务。这时由于其他的服务通过订阅机制会收到响应，然后更新自己的服务列表，新的请求就不会再进入到自己的服务。
2. 然后等待30s，这期间就是为了将之前已经进行的请求执行完。
3. 30s过后进行容器的销毁

# 流程图
![在这里插入图片描述](https://img-blog.csdnimg.cn/0668223f07274ecb831fedbd7760dbda.png#pic_center)


# 关键部分源码
这里只有贴出本人额外增加的部分，关于`ribbon`和`nacos`本身的源码可参考下面的文章系列
## 客户端
### 配置类
```java
@Configuration
public class ServiceConfig {

    @Bean
    public NacosHandle nacosHandle(NacosDiscoveryProperties discoveryProperties, NacosAutoServiceRegistration nacosAutoServiceRegistration){
        return new NacosHandle(discoveryProperties,nacosAutoServiceRegistration);
    }

    @Bean
    public RibbonHandle ribbonHandle(){
        return new RibbonHandle();
    }

    @Bean
    public ServiceHandle serviceHandle(NacosHandle nacosHandle, RibbonHandle ribbonHandle){
        return new ServiceHandle(nacosHandle, ribbonHandle);
    }

    @Bean
    public ServiceRefresher serviceRefresher(ServiceHandle serviceHandle,NacosDiscoveryProperties properties){
        return new ServiceRefresher(serviceHandle,properties);
    }
}
```
### 订阅机制
```java
public class ServiceRefresher implements SmartLifecycle {
    private static final Logger logger = LoggerFactory.getLogger(ServiceRefresher.class);

    private final AtomicBoolean running = new AtomicBoolean(false);

    private ServiceHandle serviceHandle;

    private NacosDiscoveryProperties properties;

    public ServiceRefresher(ServiceHandle serviceHandle,NacosDiscoveryProperties properties){
        this.serviceHandle = serviceHandle;
        this.properties = properties;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        this.stop();
        callback.run();
    }

    @Override
    public void start(){
        if (this.running.compareAndSet(false, true)) {
            try {
                //开启订阅机制
                NamingService naming = NamingFactory.createNamingService(properties.getNacosProperties());
                naming.subscribe(properties.getService(),event -> {
                    new Thread(() -> {
                        //收到服务端的响应后，进行刷新nacos和ribbon列表
                        serviceHandle.updateNacosAndRibbonCache();
                        logger.warn("updateNacosAndRibbonCache completed ...");
                    },"service-refresher-thread").start();
                });
            }catch (Exception e) {
                logger.error("ServiceRefresher subscribe failed, properties:{}", properties, e);
            }
        }
    }

    @Override
    public void stop() {
        if (this.running.compareAndSet(true, false)) {
            try {
                NamingService naming = NamingFactory.createNamingService(properties.getNacosProperties());
                naming.unsubscribe(properties.getService(),event -> {
                    new Thread(() -> {
                        serviceHandle.updateNacosAndRibbonCache();
                        logger.warn("updateNacosAndRibbonCache completed ...");
                    },"service-refresher-thread").start();
                });
            }catch (Exception e) {
                logger.error("ServiceRefresher unsubscribe failed, properties:{}", properties, e);
            }
        }
    }

    @Override
    public boolean isRunning() {
        return this.running.get();
    }

    @Override
    public int getPhase() {
    	//此为1的作用是要在服务注册前完成订阅
        return 1;
    }
}
```
### 刷新nacos和ribbon服务列表
**ServiceHandle**
```java
public class ServiceHandle {

    private NacosHandle nacosHandle;

    private RibbonHandle ribbonHandle;

    public ServiceHandle(NacosHandle nacosHandle,RibbonHandle ribbonHandle){
        this.nacosHandle = nacosHandle;
        this.ribbonHandle = ribbonHandle;
    }

    public boolean updateNacosAndRibbonCache(){
        nacosHandle.clearNacosCache();
        ribbonHandle.updateRibbonCache();
        return true;
    }

    public Map getNacosAndRibbonCache() {
        Map nacosCache = nacosHandle.getNacosCache();
        Map ribbonCache = ribbonHandle.getRibbonCache();
        Map map = new HashMap();
        map.put("nacosCache",nacosCache);
        map.put("ribbonCache",ribbonCache);
        return map;
    }
}
```
**NacosHandle**
```java
public class NacosHandle implements ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(NacosHandle.class);

    private static final String CLIENT_PROXY_FIELD = "clientProxy";

    private static final String SERVICE_INFO_HOLDER_FIELD = "serviceInfoHolder";

    private static final String FUTURE_MAP_FIELD = "futureMap";

    private static final String SERVICE_INFO_UPDATE_SERVICE_FIELD = "serviceInfoUpdateService";

    /**
     * nacos配置项
     *
     * */
    private NacosDiscoveryProperties discoveryProperties;

    /**
     * nacos服务操作管理
     * */
    private NacosAutoServiceRegistration nacosAutoServiceRegistration;

    private ApplicationContext applicationContext;

    public NacosHandle(NacosDiscoveryProperties discoveryProperties,NacosAutoServiceRegistration nacosAutoServiceRegistration){
        this.discoveryProperties = discoveryProperties;
        this.nacosAutoServiceRegistration = nacosAutoServiceRegistration;
    }

    private Map nacosCache(){
        Map map = new HashMap();
        try {
            //获得nacos的操作约定接口NamingService
            NamingService namingService = discoveryProperties.namingServiceInstance();
            //NamingService的唯一实现类NacosNamingService
            if (namingService instanceof NacosNamingService) {
                NacosNamingService nacosNamingService = (NacosNamingService)namingService;
                Class<? extends NacosNamingService> aClass1 = nacosNamingService.getClass();
                //反射拿到clientProxy属性，此属性是调取服务作用分为三种NamingClientProxyDelegate、NamingGrpcClientProxy、NamingHttpClientProxy
                //这里为NamingClientProxyDelegate
                Field clientProxyField = aClass1.getDeclaredField(CLIENT_PROXY_FIELD);
                clientProxyField.setAccessible(true);
                NamingClientProxyDelegate namingClientProxyDelegate = (NamingClientProxyDelegate)clientProxyField.get(nacosNamingService);

                Class<? extends NamingClientProxyDelegate> namingClientProxyDelegateClass = namingClientProxyDelegate.getClass();
                //1,由于nacos是通过NamingClientProxyDelegate通过subscribe实现订阅里面通过定时任务拉取nacos服务缓存到serviceInfoHolder中
                //2,所以这里要通过反射拿到serviceInfoHolder，将里面的缓存清除，
                //3,这样当ribbon更新自己的缓存时会拉取nacos服务，这时nacos已经没有缓存就会重新调用grpc真正从nacos注册中心拉取最新的服务，再存入serviceInfoHolder中。
                Field serviceInfoHolderField = namingClientProxyDelegateClass.getDeclaredField(SERVICE_INFO_HOLDER_FIELD);
                serviceInfoHolderField.setAccessible(true);
                ServiceInfoHolder serviceInfoHolder = (ServiceInfoHolder)serviceInfoHolderField.get(namingClientProxyDelegate);

                //反射拿到serviceInfoUpdateService属性
                Field serviceInfoUpdateServiceField = namingClientProxyDelegateClass.getDeclaredField(SERVICE_INFO_UPDATE_SERVICE_FIELD);
                serviceInfoUpdateServiceField.setAccessible(true);
                ServiceInfoUpdateService serviceInfoUpdateService =
                        (ServiceInfoUpdateService)serviceInfoUpdateServiceField.get(namingClientProxyDelegate);
                //反射拿到serviceInfoMap属性
                Class<? extends ServiceInfoUpdateService> serviceInfoUpdateServiceClass = serviceInfoUpdateService.getClass();
                Field futureMapField = serviceInfoUpdateServiceClass.getDeclaredField(FUTURE_MAP_FIELD);
                futureMapField.setAccessible(true);
                Map<String, ScheduledFuture<?>> futureMap = (Map<String, ScheduledFuture<?>>)futureMapField.get(serviceInfoUpdateService);

                Map<String, ServiceInfo> serviceInfoMap = serviceInfoHolder.getServiceInfoMap();
                map.put("serviceInfoMap",serviceInfoMap);
                map.put(FUTURE_MAP_FIELD,futureMap);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    /**
     * 将nacos缓存清空
     * */
    public void clearNacosCache(){
        Map map = nacosCache();
        Map<String, ScheduledFuture<?>> futureMap = (Map<String, ScheduledFuture<?>>)map.get(FUTURE_MAP_FIELD);
        Map<String, ServiceInfo> serviceInfoMap = (Map<String, ServiceInfo>)map.get("serviceInfoMap");
        //这里加锁是为nacos本身的定时任务做线程安全，定时任务中的锁也是用的synchronized (futureMap)
        synchronized (futureMap) {
            serviceInfoMap.clear();
        }
    }

    /**
     * 获得nacos缓存
     * */
    public Map getNacosCache(){
        Map map = nacosCache();
        return (Map<String, ServiceInfo>)map.get("serviceInfoMap");
    }

    /**
     * 停止服务
     * */
    public Boolean stopService(){
        try {
            logger.info("Ready to stop service");
            nacosAutoServiceRegistration.stop();
            logger.info("Nacos instance has been de-registered");
            return true;
        }catch (Exception e) {
            logger.error("stopService error ", e);
            return false;
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
```
**RibbonHandle**
```java
public class RibbonHandle {

    private static final String CONTEXTS_FIELD = "contexts";

    private static final String RIBBON_LOAD_BALANCER = "ribbonLoadBalancer";

    private static final String UPDATE_ACTION_FIELD = "updateAction";

    private static final String ALL_SERVER_LIST_FIELD = "allServerList";

    private static final String UP_SERVER_LIST_FIELD = "upServerList";

    private Map<String,ZoneAwareLoadBalancer> zoneAwareLoadBalancerMap(){
        Map<String,ZoneAwareLoadBalancer> zoneAwareLoadBalancerMap = new HashMap<>();
        try{
            //该类是 Spring 创建 Ribbon 客户端、负载均衡器、客户端配置实例的工厂，并且为每个 client name 创建对应的 Spring ApplicationContext。
            SpringClientFactory springClientFactory = SpringManageUtil.getBean(SpringClientFactory.class);
            if (springClientFactory != null) {
                Class<? extends SpringClientFactory> aClass = springClientFactory.getClass();
                Field contextsField = aClass.getSuperclass().getDeclaredField(CONTEXTS_FIELD);
                contextsField.setAccessible(true);
                //1,反射获得到contexts，此属性为Map<String, AnnotationConfigApplicationContext>，
                //2,Key为每个fegin的服务名，Value为每个服务自己的spring容器(此容器有个父容器也就是此项目中的spring容器)
                Map<String, AnnotationConfigApplicationContext> map = (Map<String, AnnotationConfigApplicationContext>)contextsField.get(springClientFactory);
                //每个fegin服务的zoneAwareLoadBalancer
                for (Map.Entry<String, AnnotationConfigApplicationContext> entry : map.entrySet()) {
                    String serverName = entry.getKey();
                    AnnotationConfigApplicationContext context = entry.getValue();
                    ILoadBalancer iLoadBalancer = context.getBean(RIBBON_LOAD_BALANCER, ILoadBalancer.class);
                    if (iLoadBalancer instanceof ZoneAwareLoadBalancer) {
                        //ZoneAwareLoadBalancer为负载均衡器
                        ZoneAwareLoadBalancer zoneAwareLoadBalancer = (ZoneAwareLoadBalancer)iLoadBalancer;
                        zoneAwareLoadBalancerMap.put(serverName,zoneAwareLoadBalancer);
                    }
                }
            }
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
        return zoneAwareLoadBalancerMap;
    }

    /**
     * 更新ribbon缓存(此操作会调取nacos服务，nacos里面的逻辑又会把服务放进nacos自己的缓存中)
     * */
    public void updateRibbonCache(){
        try {
            Map<String, ZoneAwareLoadBalancer> zoneAwareLoadBalancerMap = zoneAwareLoadBalancerMap();
            //循环更新每个fegin服务的ribbon服务缓存
            for (Map.Entry<String, ZoneAwareLoadBalancer> zoneAwareLoadBalancerEntry : zoneAwareLoadBalancerMap.entrySet()) {
                ZoneAwareLoadBalancer zoneAwareLoadBalancer = zoneAwareLoadBalancerEntry.getValue();
                Class<? extends ZoneAwareLoadBalancer> aClass1 = zoneAwareLoadBalancer.getClass();
                Field updateActionField = aClass1.getSuperclass().getDeclaredField(UPDATE_ACTION_FIELD);
                updateActionField.setAccessible(true);
                //1,反射拿到ZoneAwareLoadBalancer的父类DynamicServerListLoadBalancer中updateAction属性
                //2,updateAction为接口，调用doUpDate()方法实际调用updateListOfServers()
                //3,updateListOfServers()就是调用nacos服务并更新到ribbon自己的缓存中
                ServerListUpdater.UpdateAction updateAction = (ServerListUpdater.UpdateAction)updateActionField.get(zoneAwareLoadBalancer);
                //ribbon更新自己缓存的逻辑中，
                //用的BaseLoadBalancer中的ReadWriteLock allServerLock = new ReentrantReadWriteLock()
                //和ReadWriteLock upServerLock = new ReentrantReadWriteLock()
                //保证了线程安全所以不用重写线程安全的逻辑
                updateAction.doUpdate();
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public Map getRibbonCache(){
        Map taotalMap = new HashMap();
        try {
            Map<String, ZoneAwareLoadBalancer> zoneAwareLoadBalancerMap = zoneAwareLoadBalancerMap();
            for (Map.Entry<String, ZoneAwareLoadBalancer> zoneAwareLoadBalancerEntry : zoneAwareLoadBalancerMap.entrySet()) {
                String serverName = zoneAwareLoadBalancerEntry.getKey();
                ZoneAwareLoadBalancer zoneAwareLoadBalancer = zoneAwareLoadBalancerEntry.getValue();
                Class<? extends ZoneAwareLoadBalancer> aClass1 = zoneAwareLoadBalancer.getClass();
                Field allServerListField = aClass1.getSuperclass().getSuperclass().getDeclaredField(ALL_SERVER_LIST_FIELD);
                allServerListField.setAccessible(true);
                List<Server> allServerList = (List<Server>)allServerListField.get(zoneAwareLoadBalancer);
                Field upServerListField = aClass1.getSuperclass().getSuperclass().getDeclaredField(UP_SERVER_LIST_FIELD);
                upServerListField.setAccessible(true);
                List<Server> upServerList = (List<Server>)upServerListField.get(zoneAwareLoadBalancer);
                Map mapServerList = new HashMap();
                mapServerList.put(ALL_SERVER_LIST_FIELD,allServerList);
                mapServerList.put(UP_SERVER_LIST_FIELD,upServerList);
                taotalMap.put(serverName,mapServerList);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return taotalMap;
    }
}
```
## nacos服务端
### 接收到服务注册或注销的请求后进行任务的添加和执行
**EphemeralClientOperationServiceImpl**
```java
@Component("ephemeralClientOperationService")
public class EphemeralClientOperationServiceImpl implements ClientOperationService, CommandLineRunner {


    /**
     * 省略
     * */
    private BlockingQueue<PushDelayTask> blockingQueue;

    @Autowired
    private NotifyServiceThreadPool notifyServiceThreadPool;

    /**
     * 收到服务注册
     * */
    public void registerInstance(Service service, Instance instance, String clientId) {
        //确保Service单例存在
        Service singleton = ServiceManager.getInstance().getSingleton(service);
        //根据客户端id，找到客户端
        Client client = clientManager.getClient(clientId);
        if (!clientIsLegal(client, clientId)) {
            return;
        }
        LOGGER.info("registerInstance execute client:{}",JSON.toJSONString(client));
        //当收到服务注册请求后，调用线程异步向blockingQueue添加任务
        new Thread(() -> {
            //nacos本身通知
            addQueueNotifyServiceTask();
        },"registerInstance-thread").start();

        //客户端Instance模型，转换为服务端Instance模型
        InstancePublishInfo instanceInfo = getPublishInfo(instance);
        client.addServiceInstance(singleton, instanceInfo);
        client.setLastUpdatedTime();
        //建立Service与ClientId的关系
        NotifyCenter.publishEvent(new ClientOperationEvent.ClientRegisterServiceEvent(singleton, clientId));
        NotifyCenter
                .publishEvent(new MetadataEvent.InstanceMetadataEvent(singleton, instanceInfo.getMetadataId(), false));
    }


    /**
     * 收到服务注销
     * */
    public void deregisterInstance(Service service, Instance instance, String clientId) {
        if (!ServiceManager.getInstance().containSingleton(service)) {
            Loggers.SRV_LOG.warn("remove instance from non-exist service: {}", service);
            return;
        }
        Service singleton = ServiceManager.getInstance().getSingleton(service);
        Client client = clientManager.getClient(clientId);
        if (!clientIsLegal(client, clientId)) {
            return;
        }
        LOGGER.info("deregisterInstance execute client:{}",JSON.toJSONString(client));
        //当收到服务注册请求后，调用线程异步向blockingQueue添加任务
        new Thread(() -> {
            //nacos本身通知
            addQueueNotifyServiceTask();
        },"deregisterInstance-thread").start();

        InstancePublishInfo removedInstance = client.removeServiceInstance(singleton);
        client.setLastUpdatedTime();
        if (null != removedInstance) {
            NotifyCenter.publishEvent(new ClientOperationEvent.ClientDeregisterServiceEvent(singleton, clientId));
            NotifyCenter.publishEvent(
                    new MetadataEvent.InstanceMetadataEvent(singleton, removedInstance.getMetadataId(), true));
        }
    }

    /**
     * 向blockingQueue添加任务
     *
     * */
    public void addQueueNotifyServiceTask(){
        //拿到服务端中所有的服务列表
        ConcurrentHashMap<String, Set<Service>> namespaceSingletonMaps = ServiceManager.getInstance().getNamespaceSingletonMaps();
        //循环服务列表，包装成PushDelayTask任务放进blockingQueue队列中
        for (Map.Entry<String, Set<Service>> stringSetEntry : namespaceSingletonMaps.entrySet()) {
            for (Service service : stringSetEntry.getValue()) {
                try {
                    blockingQueue.offer(new PushDelayTask(service, PushConfig.getInstance().getPushTaskDelay()),5000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOGGER.error("addQueueNotifyServiceTask error {}",e.getMessage());
                }
            }
        }
    }


    /**
     * 当容器启动后，开启一个线程，不断的循环从blockingQueue队列中取出任务，然后异步执行任务
     * 任务的逻辑就是通知客户端的订阅
     * */
    @Override
    public void run(String... args) throws Exception {
        new Thread(() -> {
            try {
                Class<? extends NamingSubscriberServiceV2Impl> aClass = namingSubscriberServiceV2.getClass();
                Field delayTaskEngineField = aClass.getDeclaredField("delayTaskEngine");
                delayTaskEngineField.setAccessible(true);
                PushDelayTaskExecuteEngine pushDelayTaskExecuteEngine = (PushDelayTaskExecuteEngine)delayTaskEngineField.get(namingSubscriberServiceV2);
                for (;;) {
                    PushDelayTask pushDelayTask = blockingQueue.take();
                    PushExecuteTask pushExecuteTask = new PushExecuteTask(pushDelayTask.getService(), pushDelayTaskExecuteEngine, pushDelayTask);
                    LOGGER.info("nacos notifyService execute pushExecuteTask:{}",JSON.toJSONString(pushExecuteTask));
                    notifyServiceThreadPool.execute(pushExecuteTask);
                }
            }catch (Exception e) {
                LOGGER.error("consumeQueueNotifyServiceTask error",e);
            }
        },"consume-queue-notify-service-task-thread").start();

    }


    /**
     * 省略
     * */
} 
```
# 讲解
## 关于ribbon和nacos的原理可参考本人的博客系列
**nacos系列**
https://blog.csdn.net/guntun8987/category_11866826.html
**ribbon系列**
https://blog.csdn.net/guntun8987/category_11865144.html

# 实现了微服务之间的feign调用时，请求头重要参数能够传递的功能
# 实现了灰度和生产环境共用一个注册中心，并实现服务隔离的功能