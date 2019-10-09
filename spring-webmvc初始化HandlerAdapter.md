# springmvc 如何初始化 Handler的 适配器？
1.主要属性：
- private List<HandlerAdapter> handlerAdapters; 

2.初始化 方法：通过 传入的上下文参数
```
private void initHandlerAdapters(ApplicationContext context) {
		this.handlerAdapters = null;// 对 要初始化的对象 初始化 为 null

		if (this.detectAllHandlerAdapters) {
			// Find all HandlerAdapters in the ApplicationContext, including ancestor contexts.
      // 先从 上下文里面 找 到所有的 适配器：从 ListableBeanFactory 里面去拿，true: 是否加载 多例模式或其他 scoped bean;false:懒加载
			Map<String, HandlerAdapter> matchingBeans =
					BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerAdapter.class, true, false);
			if (!matchingBeans.isEmpty()) {
				this.handlerAdapters = new ArrayList<>(matchingBeans.values());
				// We keep HandlerAdapters in sorted order.
				AnnotationAwareOrderComparator.sort(this.handlerAdapters);
			}
		}
    // 上下文里面没有适配器
		else {
			try {
      //单纯 通过 bf 的 getBean 方法获取到 单个 bean
				HandlerAdapter ha = context.getBean(HANDLER_ADAPTER_BEAN_NAME, HandlerAdapter.class);
				this.handlerAdapters = Collections.singletonList(ha);
			}
			catch (NoSuchBeanDefinitionException ex) {
				// Ignore, we'll add a default HandlerAdapter later.
			}
		}

		// Ensure we have at least some HandlerAdapters, by registering
		// default HandlerAdapters if no other adapters are found. 根据 接口 类型 通过策略获取到 接口的 实现类集合
		if (this.handlerAdapters == null) {
			this.handlerAdapters = getDefaultStrategies(context, HandlerAdapter.class);
			if (logger.isDebugEnabled()) {
				logger.debug("No HandlerAdapters found in servlet '" + getServletName() + "': using default");
			}
		}
	}
```
如果在Bean factory 里面 没有适配器，默认 是 SimpleControllerHandlerAdapter。

从 ListableBeanFactory 里面去拿：
- 根据传进来的 type 获取到对应的 bean 子明的 String[]
- 遍历 上面的数组，调用 getBean方法 获得 Bean, 装入到 LinkedHashMap

# getDefaultStrategies 方法
```
protected <T> List<T> getDefaultStrategies(ApplicationContext context, Class<T> strategyInterface) {
		String key = strategyInterface.getName();
    // 从 配置文件里面根据 接口的类型 获取到 配置的 接口的实现类
		String value = defaultStrategies.getProperty(key);
		if (value != null) {
    // 获取到配置的三个类
			String[] classNames = StringUtils.commaDelimitedListToStringArray(value);
			List<T> strategies = new ArrayList<>(classNames.length);
			for (String className : classNames) {
				try {
        // 通过 DispatcherServlet 的类加载器 来加载这三个类
					Class<?> clazz = ClassUtils.forName(className, DispatcherServlet.class.getClassLoader());
					Object strategy = createDefaultStrategy(context, clazz);
					strategies.add((T) strategy);
				}
				catch (ClassNotFoundException ex) {
					throw new BeanInitializationException(
							"Could not find DispatcherServlet's default strategy class [" + className +
							"] for interface [" + key + "]", ex);
				}
				catch (LinkageError err) {
					throw new BeanInitializationException(
							"Unresolvable class definition for DispatcherServlet's default strategy class [" +
							className + "] for interface [" + key + "]", err);
				}
			}
			return strategies;
		}
		else {
			return new LinkedList<>();
		}
	}
```

如 ： HandlerAdapter 是一个策略接口，他的 实现类的对象 叫做策略对象

配置文件：DispatcherServlet.properties 配置的 适配器
- HttpRequestHandlerAdapter ：处理 http请求
- SimpleControllerHandlerAdapter
- RequestMappingHandlerAdapter ：处理 有注解 RequestMapping
