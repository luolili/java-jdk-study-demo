# AbstractRefreshableApplicationContext 继承了 AbstractApplicationContext

主要属性：
- private Boolean allowBeanDefinitionOverriding; 是否允许 bean 定义的覆盖，默认是 true,允许 覆盖 相同的 bean 名字注册不同的定义。
- private Boolean allowCircularReferences; 是否允许 循环依赖，默认是允许 循环依赖，如果关闭了他，在代码里面又出现的循环依赖，就会抛出异常
- private DefaultListableBeanFactory beanFactory; 上下文的默认 bean 工厂
- 	private final Object beanFactoryMonitor = new Object(); 内部 bean 工厂的同步锁。

主要方法：

1.
```

@Override
	protected final void refreshBeanFactory() throws BeansException {
		if (hasBeanFactory()) {
			destroyBeans();
			closeBeanFactory();
		}
		try {
			DefaultListableBeanFactory beanFactory = createBeanFactory();//
			beanFactory.setSerializationId(getId());//设置 新工厂的id
			customizeBeanFactory(beanFactory);// 给 bean factory 设置 是否覆盖 bean 定义和是否 允许循环依赖。
      //具体的实现子类有：ClassPathXmlApplicationContext，FileSystemXmlApplicationContext等
			loadBeanDefinitions(beanFactory);// 模板方法，一般把读取 bean 定义的工作交给  definition readers. 每次刷新都会调用它。
			synchronized (this.beanFactoryMonitor) {
				this.beanFactory = beanFactory;
			}
		}
		catch (IOException ex) {
			throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
		}
	}
```

他真正实现了刷新上下文，如果存在 bean factory ,关闭它，然后初始化一个全新的 bean factory 给上下文的下一个生命周期。

2.
```

@Override
	public final ConfigurableListableBeanFactory getBeanFactory() {
		synchronized (this.beanFactoryMonitor) {
			if (this.beanFactory == null) {
				throw new IllegalStateException("BeanFactory not initialized or already closed - " +
						"call 'refresh' before accessing beans via the ApplicationContext");
			}
			return this.beanFactory;
		}
	}
  
  	protected final boolean hasBeanFactory() {
		synchronized (this.beanFactoryMonitor) {
			return (this.beanFactory != null);
		}
	}
```

加同步锁来获取 bean factory, 类型 是 ConfigurableListableBeanFactory。

```
@Override
	protected final void closeBeanFactory() {
		synchronized (this.beanFactoryMonitor) {
			if (this.beanFactory != null) {
				this.beanFactory.setSerializationId(null);
				this.beanFactory = null;
			}
		}
	}
  ```
  ```
  @Override
	protected void cancelRefresh(BeansException ex) {
		synchronized (this.beanFactoryMonitor) {
			if (this.beanFactory != null)
				this.beanFactory.setSerializationId(null);
		}
		super.cancelRefresh(ex);
	}

  ```
  取消刷新的步骤：一：把工厂的id设置为null,;二：设置 他的属性active 为 false.
