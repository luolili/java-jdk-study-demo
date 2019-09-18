# AbstractXmlApplicationContext 从包含 bean 定义的 xml文档里面抽取配置。继承 AbstractRefreshableConfigApplicationContext

主要属性：
- private boolean validating = true;  是否使用xml 的验证规则，默认是true。
- 构造方法：无参构造方法+带父亲的构造方法

主要方法：

```
	@Override
	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
		// Create a new XmlBeanDefinitionReader for the given BeanFactory.
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

		// Configure the bean definition reader with this context's
		// resource loading environment.
		beanDefinitionReader.setEnvironment(this.getEnvironment());
		beanDefinitionReader.setResourceLoader(this);
		beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

		// Allow a subclass to provide custom initialization of the reader,
		// then proceed with actually loading the bean definitions.
		initBeanDefinitionReader(beanDefinitionReader);// 重写这个方法，使用自己的 reader, 或者关闭 xml 验证。
		loadBeanDefinitions(beanDefinitionReader);
	}

```

加载多个 bean 的定义， 通过 XmlBeanDefinitionReader xml bean 定义的阅读器。

steps:
- 先创建一个没有配置的 reader 
- 配置这个 reader
- 加载 xml 里面的 bean 定义。

```
	protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
		Resource[] configResources = getConfigResources();//默认实现是返回空
		if (configResources != null) {
			reader.loadBeanDefinitions(configResources);//根据资源集合，来加载 xml
		}
		String[] configLocations = getConfigLocations();
		if (configLocations != null) {
			reader.loadBeanDefinitions(configLocations);// 根据资源位置，来加载
		}
	}

```
加载的具体实现在： XmlBeanDefinitionReader

