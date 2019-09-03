#AbstractAutowireCapableBeanFactory里面的属性： instantiationStrategy

1. 接口： InstantiationStrategy

里面定义了3个实例化 bean 的方法体，根据不同的参数条件来创建bean 实例对象。
他是根据 bean 定义来创建的。
方法1：
```
Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner)
			throws BeansException;
      
 ```
 
 方法2：
 ```
	Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner,
			Constructor<?> ctor, @Nullable Object... args) throws BeansException;
      
 ```
 
 方法3：
 
  ```
	Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner,
			Constructor<?> ctor, @Nullable Object... args) throws BeansException;
      
 ```
 Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner,
			@Nullable Object factoryBean, Method factoryMethod, @Nullable Object... args)
			throws BeansException;
      
 ```
 
 
 它们3个在实例化失败的时候会出现 BeansException 。
 
 他的实现类有2个： SimpleInstantiationStrategy ， CglibSubclassingInstantiationStrategy
 
 ## SimpleInstantiationStrategy 分析
 不支持方法的注入： method injection.
 原因：
 ```
 	protected Object instantiateWithMethodInjection(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner) {
		throw new UnsupportedOperationException("Method Injection not supported in SimpleInstantiationStrategy");
	}
 
 ```
 
 子类可以重写他，但是会抛出异常。
 
 从bd里面获取到 Constructor 对象 或者从 bd 里面 虎丘到bean class 再从bean class 里面 获取 Constructor 对象；
 调用 BeanUtils.instantiateClass(constructorToUse);
 
 对于Java语言，直接调用 ctor.newInstance(args).
 
 对于第二个具有构造器惨呼的方法来说，本质和第一个方法是一样的，区别在于第二个方法各异指定具体的构造方法来创建实例对象。
 
 第三个方法 ： Object result = factoryMethod.invoke(factoryBean, args);
 
 利用传进来的工厂方法来创建对象。
 
 ## CglibSubclassingInstantiationStrategy ： 默认的创建 bean 实例的类
 用cglib库来动态生成子类。
 ```
 
 @Override
	protected Object instantiateWithMethodInjection(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner,
			@Nullable Constructor<?> ctor, @Nullable Object... args) {

		// Must generate CGLIB subclass...
		return new CglibSubclassCreator(bd, owner).instantiate(ctor, args);
	}
  ```
  CglibSubclassCreator 是这个类的私有静态 inner class. 原理同上。
  
  
 
 
