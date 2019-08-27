#ConfigurableBeanFactory 
intro: 继承了HierarchicalBeanFactory接口和SingletonBeanRegistry。

HierarchicalBeanFactory的作用
- BeanFactory getParentBeanFactory(); 获取他的父亲facotry类，如果没有就返回null
- boolean containsLocalBean(String name);根据传入的bean名字，在当前的工厂里面找是否有这bean，
不会在他的父亲工厂类里面去找

SingletonBeanRegistry的作用
- void registerSingleton(String beanName, Object singletonObject); singletonObject表示的是已经完全初始化
好的一个单例对象；把这个对象放到beanName的名下。他的实现类在调用这个方法的时候应该加上同步访问的控制。
这里要求名字和对象都不能为空。

DefaultSingletonBeanRegistry：
- singletonObjects： bean name --> obj的ConcurrentHashMap结构。
在向map加入一条记录之前，先判断加入的记录在这个map里面是否已经存在。
在访问singletonObjects 的时候需要同步。

然后调用protected void addSingleton(String beanName, Object singletonObject)；
- 在加入新的记录的时候也需要同步singletonObjects。防止多个线程都来加入相同的bean.
