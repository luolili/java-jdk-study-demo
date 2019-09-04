# AbstractAutowireCapableBeanFactory 实现了 AutowireCapableBeanFactory接口

AutowireCapableBeanFactory 接口定义了 
- 注入类型的常量字段： autowaire_by_name, autowaire_by_type, autowaire_no, autowaire_constructor.
- <T> T createBean(Class<T> beanClass) throws BeansException; 仅仅通过class 创建 bean 
- void autowireBean(Object existingBean) throws BeansException; 注入 bean
- Object configureBean(Object existingBean, String beanName) throws BeansException; 配置 bean

更细粒度的方法：可以指定注入的方法
- Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException;
- Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException;
- void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck)
			throws BeansException;
- 	Object initializeBean(Object existingBean, String beanName) throws BeansException;

##  <T> T createBean(Class<T> beanClass) throws BeansException; 的实现
创建传进来的 class 的 RootBeanDefinition, 设置他的 scope 为多例模式。

调用 protected Object createBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)
			throws BeanCreationException
      
      他重载了 AbstractBeanFactory 里面的这个方法。 这个方法在 AbstractBeanFactory 是没有 实现的。
      
      
 ## void autowireBean(Object existingBean) throws BeansException;  的实现
 根据传进来的对象 获得 他的 class 同创建 bean 的时候一样 把 bean 的定义设置为多例模式：
 防止 把他作为依赖的 bean 放入容器。 
 
 创建 BeanWrapper的实例，并且初始化他， 设置属性值:populateBean(bd.getBeanClass().getName(), bd, bw);
 ---
 ## Object configureBean(Object existingBean, String beanName) throws BeansException;  的实现
 在进入这个方法的时候， 表明 已经创建好了 bean. markBeanAsCreated(beanName);
 把 这个bean 的名字放入 ：alreadyCreated：来自 AbstractBeanFactory。
 
 根据 bean 名称货取 mbd, 若 mbd 是 RootBeanDefinition , 转为 rbd. 检查 rbd 是否是多例，若不是，设置为多例。
 
 - 设置属性值： populateBean(bd.getBeanClass().getName(), bd, bw);
 - return initializeBean(beanName, existingBean, bd); 初始化 bean的实例，通过工厂方法，init method, pots processor.
 
 ## protected Object initializeBean(final String beanName, final Object bean, @Nullable RootBeanDefinition mbd)
 - private void invokeAwareMethods(final String beanName, final Object bean)
 
 ```
 private void invokeAwareMethods(final String beanName, final Object bean) {
		if (bean instanceof Aware) {
			if (bean instanceof BeanNameAware) {
				((BeanNameAware) bean).setBeanName(beanName);
			}
			if (bean instanceof BeanClassLoaderAware) {
				ClassLoader bcl = getBeanClassLoader();
				if (bcl != null) {
					((BeanClassLoaderAware) bean).setBeanClassLoader(bcl);
				}
			}
			if (bean instanceof BeanFactoryAware) {
				((BeanFactoryAware) bean).setBeanFactory(AbstractAutowireCapableBeanFactory.this);
			}
		}
	}
  
 ```
 这里完成了 bean 生命周期的 前三个阶段：BeanNameAware，BeanClassLoaderAware， BeanFactoryAware。
 class loader：当前线程的，ClassUtils的，系统的(ClassLoader.getSystemClassLoader()).
 
 - applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName): 初始化之前的后置处理；
 - protected void invokeInitMethods(String beanName, final Object bean, @Nullable RootBeanDefinition mbd)
			throws Throwable
      
     调用 afterPropertiesSet(); 调用个性化的初始化方法。
 
 
