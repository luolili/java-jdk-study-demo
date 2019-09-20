# GenericApplicationContext extends AbstractApplicationContext 通用的上下文，可以作为不同格式的bean definition 格式的文件阅读人的构造参数

usage:
```
GenericApplicationContext ctx = new GenericApplicationContext();
   XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
  xmlReader.loadBeanDefinitions(new ClassPathResource("applicationContext.xml"));
   PropertiesBeanDefinitionReader propReader = new PropertiesBeanDefinitionReader(ctx);
  propReader.loadBeanDefinitions(new ClassPathResource("otherBeans.properties"));
  ctx.refresh();
```
主要属性
- private final DefaultListableBeanFactory beanFactory; 通过他来设置一些属性：是否允许循环依赖，是否覆盖之前的 bean 定义等。
- 	private ResourceLoader resourceLoader; 资源加载的人； 可以根据文件路径获取到资源对象 Resource
- 	private boolean customClassLoader = false; 用名词代表是否的意思 ，默认不允许个性化的 class loader
- private final AtomicBoolean refreshed = new AtomicBoolean(); 是否刷新的标识， 默认是false

主要方法：
1.
```
//设置自己的 class loader
@Override
	public void setClassLoader(@Nullable ClassLoader classLoader) {
		super.setClassLoader(classLoader);
		this.customClassLoader = true;
	}
```
2. 对抽象类里面模板方法的实现
```
@Override
	protected final void refreshBeanFactory() throws IllegalStateException {
  //compareAndSet 实际值不等于希望值的时候返回 false,
		if (!this.refreshed.compareAndSet(false, true)) {// 标识已经刷新了，refreshed 为 true
			throw new IllegalStateException(
					"GenericApplicationContext does not support multiple refresh attempts: just call 'refresh' once");
		}
		this.beanFactory.setSerializationId(getId());
	}

```

```
@Override
	protected final void closeBeanFactory() {
		this.beanFactory.setSerializationId(null);
	}
  ```
  
  # 他也实现了 BeanDefinitionRegistry bean 的注册
  都是通过 bean factory 实现bean 定义的增加，删除，查询。添加别名，删除别名，该 bean 名字是否在使用中，该 bean 名字 是否是别名
