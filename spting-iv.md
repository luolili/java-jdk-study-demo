## spring 如何解决循环依赖？

分2步 来讲：

1.创建 bean 的时候

2.获取 bean 的时候

创建 bean 的时候：在 AbstractBeanFactory 里面的抽象方法 createBean 方法的实现类 AbstractAutowireCapableBeanFactory里面
的 doCreateBean 方法 里面：
```
boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences &&
				isSingletonCurrentlyInCreation(beanName));
		if (earlySingletonExposure) {
			if (logger.isDebugEnabled()) {
				logger.debug("Eagerly caching bean '" + beanName +
						"' to allow for resolving potential circular references");
			}
			addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
		}
    //放入单例工厂和已经注册的单例，从早暴露单例对象集合删除
    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
		Assert.notNull(singletonFactory, "Singleton factory must not be null");
		synchronized (this.singletonObjects) {
			if (!this.singletonObjects.containsKey(beanName)) {
				this.singletonFactories.put(beanName, singletonFactory);
				this.earlySingletonObjects.remove(beanName);
				this.registeredSingletons.add(beanName);
			}
		}
	}
```


获取 bean 的时候：先从singletonObjects 获取，没有就 从早暴露单例对象集合 获取，没有从 单例工厂里面获取；
获得之后放入 早暴露单例对象集合 ，从单例工厂里面移除 

```
protected Object getSingleton(String beanName, boolean allowEarlyReference) {
		Object singletonObject = this.singletonObjects.get(beanName);
		if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
			synchronized (this.singletonObjects) {
				singletonObject = this.earlySingletonObjects.get(beanName);
				if (singletonObject == null && allowEarlyReference) {
					ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
					if (singletonFactory != null) {
						singletonObject = singletonFactory.getObject();
						this.earlySingletonObjects.put(beanName, singletonObject);
						this.singletonFactories.remove(beanName);
					}
				}
			}
		}
		return singletonObject;
	}
 ```
 
 ## spring bean 生命周期
 
 实例化--属性注入--设置 bean name-- 设置 bean factory--beanPostProcessor 的前置初始化--InitializingBean de afterProperties 方法--
 beanPostProcessor 的后置初始化-- init-method --Disposable 销毁

## 依赖注入的方式
1.构造方法：设置大量的属性

2.setter：设置少量的属性

3.接口

在 spring 里，只用 构造方法注入和 setter 注入。

##BeanFactory 和 ApplicationContext 区别
1.前：用懒加载；后：及时加载

2.前：不支持国际化；后：支持国际化

3.前：不支持基于依赖的注解；后：支持基于依赖的注解
##ioc 实现机制
工厂模式+反射

## 什么是 spring 装配？自动装配的方式
构造容器里面多个 bean 的依赖关系。

1.byName/Type,autodetect,no