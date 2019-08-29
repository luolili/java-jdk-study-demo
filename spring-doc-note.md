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

1. FactoryBean与ObjectFactory2个接口的区别？
- ObjectFactory的getObject方法通常返回的是一个多例的对象,也会返回单例的对象； FactoryBean是一个SPI实例对象
- ObjectFactory捕获的是BeansException;FactoryBean是捕获Exception.
singletonFactories 里面装的就是ObjectFacory,用他来创建单例对象，并把创建出来的对象放进earlySingletonObjects。

2. spring获取单例对象的步骤？
方法： protected Object getSingleton(String beanName, boolean allowEarlyReference) 
- 从singletonObjects中根据名字获取单例对象，如果获得的对象不是空，则返回；
- 如果返回的是空，那么从ObjectFactory创建的对象的缓存earlySingletonObjects里面获取；
- 如果结果是空，那么根据名字从 singletonFactories 获取到生产这bean的 工厂，
- 如果工厂不是空，那么调用过程的getObject方法 获得单例对象， 把他赋值给返回值对象，把他放入 earlySingletonObjects
- 删除掉生产这bean的过程，因为下一次的bean可能会增加货减少一些属性，所以要删除原来的工厂；需要新的工厂来生产新的bean，

3. 在什么时候创建bean，放入缓存里面？
方法： public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) 
属性：singletonsCurrentlyInDestruction： 标识当前时间是否是在销毁单例的时间范围之内。
不能再销毁对象的时候，来创建货请求一个bean。
- 从 singletonObjects 根据名字获取单例对象；如果获得的对象是空，当前正在销毁对象销毁，就报错BeanCreationNotAllowedException。
- 打印日志：Creating shared instance of singleton bean
- 前置处理： beforeSingletonCreation(beanName);
- boolean newSingleton = false; 是否是新创建的单例。从传的参数 singletonFactory 里面获取单例对象，newSingleton 变为true.
- 在catch里面再次从 singletonObjects 获取单例对象
- 后置处理： 	afterSingletonCreation(beanName);
- addSingleton


# spring如何获取bean? 
方法： protected <T> T doGetBean(final String name, @Nullable final Class<T> requiredType,
			@Nullable final Object[] args, boolean typeCheckOnly) throws BeansException 
  
  - 对传进来的bean名字进行解析
  如果该bean是一个工厂bean，九八他的前缀标识符& 去掉;如果有别名，把别名作为bean的名字。
  
  - 调用上面的getsingleton方法：从bean的缓存里面获取name对应的bean对象，当获取的对象不为空以及参数args是空时，
  这个bean可能还在创建中，没有完全被初始化。
  - 对获取到的 sharedInstance Bean 进行分类讨论
   - sharedInstance  不是 FactoryBean--》直接返回 sharedInstance
   - 如果是 FactoryBean ， 调用 protected Object getCachedObjectForFactoryBean(String beanName) 从工厂缓存里面获取。
   
  #AbstractBeanFactory 的主要方法
  
  重要字段：
  - private final Map<String, RootBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap<>(256); 
  用map建立一一对应的关系，方便 读取 关系。
  
  他对应的方法：protected RootBeanDefinition getMergedLocalBeanDefinition(String beanName) throws BeansException 
  
  - private final Set<String> alreadyCreated = Collections.newSetFromMap(new ConcurrentHashMap<>(256));
	把已经创建的 bean or 将要被创建的 bean 放到Set里面。
	
	他对应的方法：protected void markBeanAsCreated(String beanName) ;在把他标记为created之前，
	要同步 mergedBeanDefinitions： 目的是要清除之前的bean的配置，
	因为可能 bean的配置已经在这个时候发生了变化，不是最新的bean配置。
	
# spring如何创建bean 实例对象？
核心方法： 	protected abstract Object createBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)
			throws BeanCreationException;  
			
			这方法在AbstractBeanFactory只有方法体，没有实现，实现是在同包support下面的AbstractAutowireCapableBeanFactory

内部方法：
- protected Class<?> resolveBeanClass(final RootBeanDefinition mbd, String beanName, final Class<?>... typesToMatch)
			throws CannotLoadBeanClassException ;
			
			这个方法在调用的时候，不需要传第三个参数，
			
			也可以通过编译。把这个 bean 对应的配置的 class属性解析出来 成为一个 Object。
			如果结果resolvedClass不是空，但是孩子的 bean 定义里面有 id 属性， 没有 class属性。
			那么需要把解析出来的 class 设置到 这个 bean 定义里面。
			
- 	protected Object resolveBeforeInstantiation(String beanName, RootBeanDefinition mbd) ; 在这个方法里面调用了	
protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName)：默认返回的是null和	public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
			throws BeansException : 返回 existingBean or 解析出来的不为空的对象;
			此时 resolveBeforeInstantiation 方法返回的是  实例化的 bean , 他是一个 proxy, 但似乎没有看到实例化对象的部分.  实例化之前的这次后置处理失败了；若这个 proxy 是 null, 则往下走。
			
- protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final @Nullable Object[] args)
			throws BeanCreationException ; 
			
			用 BeanWrapper 来实例化 bean  ， 
			
			如果 bean 的定义是单例的话，删除 beanName对应的 BeanWrapper . 如果之前没有对应的 BeanWrapper， 
			就调用 protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) ；从 BeanWrapper 里面获得 bean. 开始初始化 bean , 
			调用 protected void populateBean(String beanName, RootBeanDefinition mbd, @Nullable BeanWrapper bw) 来实现属性的注入
			- 核心语句： 	PropertyValues pvs = (mbd.hasPropertyValues() ? mbd.getPropertyValues() : null); 获得属性值对象。 	protected Object initializeBean(final String beanName, final Object bean, @Nullable RootBeanDefinition mbd)
			
			
	
	
	
  
  
   
