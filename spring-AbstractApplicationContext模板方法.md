# AbstractApplicationContext 抽象类 实现了 ApplicationContext ：抽象实现；具体的子类来实现 他里面的抽象方法。

1. /** Logger used by this class. Available to subclasses. */
	protected final Log logger = LogFactory.getLog(getClass());
  
  private String id = ObjectUtils.identityToString(this);
  
  private String displayName = ObjectUtils.identityToString(this);
  
  	private final AtomicBoolean active = new AtomicBoolean();// 上下文是否激活的标签。
    
    private final Object startupShutdownMonitor = new Object();//启动和关闭上下文的同步锁。
    
    	private Thread shutdownHook;/ jvm关闭的钩子的引用
      
      @Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getApplicationName() {
		return "";
	}
  
  # 模板方法：
  @Override
	public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
		return getBeanFactory();
	}
  
  getBeanFactory();是一个没有在抽象类里面实现的抽象方法。
  
  ```
  @Override
	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			// Prepare this context for refreshing.
			prepareRefresh();

			// Tell the subclass to refresh the internal bean factory.
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

			// Prepare the bean factory for use in this context.
			prepareBeanFactory(beanFactory);

			try {
				// Allows post-processing of the bean factory in context subclasses.
				postProcessBeanFactory(beanFactory);//模板方法

				// Invoke factory processors registered as beans in the context.
				invokeBeanFactoryPostProcessors(beanFactory);

				// Register bean processors that intercept bean creation.
				registerBeanPostProcessors(beanFactory);

				// Initialize message source for this context.
				initMessageSource();

				// Initialize event multicaster for this context.
				initApplicationEventMulticaster();

				// Initialize other special beans in specific context subclasses.
				onRefresh();// 模板方法

				// Check for listener beans and register them.
				registerListeners();

				// Instantiate all remaining (non-lazy-init) singletons.
				finishBeanFactoryInitialization(beanFactory);

				// Last step: publish corresponding event.
				finishRefresh();
			}

			catch (BeansException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Exception encountered during context initialization - " +
							"cancelling refresh attempt: " + ex);
				}

				// Destroy already created singletons to avoid dangling resources.
				destroyBeans();

				// Reset 'active' flag.
				cancelRefresh(ex);

				// Propagate exception to caller.
				throw ex;
			}

			finally {
				// Reset common introspection caches in Spring's core, since we
				// might not ever need metadata for singleton beans anymore...
				resetCommonCaches();//申明的方法，字段，注解，属性方法，别名描述器等。
			}
		}
	}

  
  ```
  
  - 刷新钱的准备： 给一部分属性赋值：
    this.startupDate = System.currentTimeMillis();
		this.closed.set(false);
		this.active.set(true);
 - 准备 一个 bean factory,给他设置相应的 配置。比如：class loader, expression resolver, property editor registrar;
 
 上下文的回调：context callback:
 beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
		beanFactory.ignoreDependencyInterface(EnvironmentAware.class);// 把要忽略的接口添加到集合：hashset里面.
    
    	beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
		beanFactory.registerResolvableDependency(ResourceLoader.class, this);//一般的 bean factory 是没有作为可解析的类型注册的。
    
    // Register early post-processor for detecting inner beans as ApplicationListeners.
		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));
