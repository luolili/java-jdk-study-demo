# GenericXmlApplicationContext GenericApplicationContext, 是一个 ClassPathXmlApplicationContext，FileSystemXmlApplicationContext替代品

主要属性：
- private final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this);

主要方法：
1.
```
public GenericXmlApplicationContext(Resource... resources) {
		load(resources);
		refresh();// 自动刷新上下文
	}
  
  public GenericXmlApplicationContext(String... resourceLocations) {
		load(resourceLocations);
		refresh();
	}
  
  public GenericXmlApplicationContext(Class<?> relativeClass, String... resourceNames) {
		load(relativeClass, resourceNames);
		refresh();
	}
```

2.
```public void load(Resource... resources) {
		this.reader.loadBeanDefinitions(resources);//真正加载文件内容的方法是在 reader 里面
	}
 ```
