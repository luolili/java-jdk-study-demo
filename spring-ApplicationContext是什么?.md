1. ApplicationContext 是一个提供应用程序的配置的中心接口。
和BeanFactory的区别
- ApplicationContext 额外实现了 MessageSource ，ApplicationEventPublisher， ListableBeanFactory， HierarchicalBeanFactory

主要方法：
1.String getId(); 每个应用都有一个 id  属性，这个 id 是唯一的，可以为 null.

2. 	String getApplicationName(); 应用的名字，默认是 empty string

3. String getDisplayName(); 一个友好的上下文的名字。

4. long getStartupDate();  第一次加载上下文的时间戳

5. 	ApplicationContext getParent();  获取本上下文的父亲。

6. 	AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException;
- 目的是初始化生活在上下文之外的实例对象。全部或者部分的把 bean lifecycle 应用到它们身上。
- 当上下文关闭的时候，抛出异常。

#ConfigurableApplicationContext ：SPI interface, 被大部分的上下文实现。继承了 ApplicationContext，Lifecycle
他除了可以配置 ApplicationContext 里面定义的内容，还有生命周期的方法：start/isRunning/stop.

属性：
- String CONFIG_LOCATION_DELIMITERS = ",; \t\n"; 在一个字符串里面有多个上下文的配置，
那么逗号，分号，空格， 换行都可以作为2个配置之间的分割符。
- 	String LOAD_TIME_WEAVER_BEAN_NAME = "loadTimeWeaver"; LoadTimeWeaver 的名字
- String ENVIRONMENT_BEAN_NAME = "environment"; 工厂的环境名字；String SYSTEM_PROPERTIES_BEAN_NAME = "systemProperties";
String SYSTEM_ENVIRONMENT_BEAN_NAME = "systemEnvironment";

方法：
- void refresh() throws BeansException, IllegalStateException; 加载或者刷新配置：persistent representation:持久化的展示方法：
xml, 属性文件：properties file, 关系型数据库的schema. 如果这个方法失败了，那么他会销毁已经创建的单例对象，目的：
避免资源的悬挂/来回摆动。调用这个方法之后，要么所有的单例都被实例化了，要么没有任何单例被实例化。

若工厂没有被初始化抛出BeansException； 已经初始化了，但是不支持多次refreah 抛出 IllegalStateException。
-void registerShutdownHook(); 目的是注册一个关闭上下文的钩子，在 jvm 关闭的时候。

- void close(); 销毁所有的单例 bean, 可以调用多次，只是后面的调用会被忽略。
- boolean isActive(); 激活的标志是 上下文至少 refresh 一次，并且没有被关闭。
