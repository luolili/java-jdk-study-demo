# AbstractBeanDefinition 是羽毛丰满的 BD 的实现的基类

主要属性：
- public static final String SCOPE_DEFAULT = ""; 默认的 scope 是空的，等价于 是单例模式，
- 来自 AutowireCapableBeanFactory 的5种注入方式 常量： autowire constant
- public static final int DEPENDENCY_CHECK_NONE = 0; 依赖检查；public static final int DEPENDENCY_CHECK_OBJECTS = 1;
- public static final int DEPENDENCY_CHECK_SIMPLE = 2;public static final int DEPENDENCY_CHECK_ALL = 3;
- 	private int role = BeanDefinition.ROLE_APPLICATION; 用户定义 的BD

主要方法：
```
// class 类型 是Object 类型的，获取class name
public String getBeanClassName() {
		Object beanClassObject = this.beanClass;
		if (beanClassObject instanceof Class) {
			return ((Class<?>) beanClassObject).getName();
		}
		else {
			return (String) beanClassObject;
		}
	}
```

```
// 通过 String 类型的 className 和 class loader 获取 Class 对象
public Class<?> resolveBeanClass(@Nullable ClassLoader classLoader) throws ClassNotFoundException {
		String className = getBeanClassName();
		if (className == null) {
			return null;
		}
		Class<?> resolvedClass = ClassUtils.forName(className, classLoader);
		this.beanClass = resolvedClass;
		return resolvedClass;
	}
  ```
  
  ```
  当BD 里面没有设置 scope的时候，默认是单例模式
  @Override
	public boolean isSingleton() {
		return SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
	}
  
  @Override
	public boolean isPrototype() {
		return SCOPE_PROTOTYPE.equals(scope);
	}
  // 这个 BD 对应的 bean 是否是抽象的，抽象 意味着 部实例化他，而是把他作为子类的父亲
  // 默认 是false, 如果是是true，就是告诉 BF 在任何情况下 不实例化他。
  public void setAbstract(boolean abstractFlag) {
		this.abstractFlag = abstractFlag;
	}
  ```
  
  ```
  // 决定用那种注入方式
  public int getResolvedAutowireMode() {
  // 自动推断
		if (this.autowireMode == AUTOWIRE_AUTODETECT) {
			// Work out whether to apply setter autowiring or constructor autowiring.
			// If it has a no-arg constructor it's deemed to be setter autowiring,
			// otherwise we'll try constructor autowiring.
			Constructor<?>[] constructors = getBeanClass().getConstructors();
			for (Constructor<?> constructor : constructors) {
      //默认使用 无参构造方法--by type
				if (constructor.getParameterCount() == 0) {
					return AUTOWIRE_BY_TYPE;
				}
			}
      //没有 无参的构造方法-- by constructor
			return AUTOWIRE_CONSTRUCTOR;
		}
		else {
    //使用配置的 非自动推断的注入方式
			return this.autowireMode;
		}
	}
  ```
  
  ```
  //@Qualifier
  public void addQualifier(AutowireCandidateQualifier qualifier) {
		this.qualifiers.put(qualifier.getTypeName(), qualifier);
	}
  // This the name of the bean to call the specified factory method on.
  public void addQualifier(AutowireCandidateQualifier qualifier) {
		this.qualifiers.put(qualifier.getTypeName(), qualifier);
	}

```

---
methodOverride 配置：决定哪些 对象的方法 将会在运行的时候被重写。 抽象类 MethodOverrides
主要属性：
- private final Set<MethodOverride> overrides = Collections.synchronizedSet(new LinkedHashSet<>(2));

在 根据 method获取他的时候，需要 syn
```
@Nullable
	public MethodOverride getOverride(Method method) {
		if (!this.modified) {
			return null;
		}
		synchronized (this.overrides) {
			MethodOverride match = null;
			for (MethodOverride candidate : this.overrides) {
				if (candidate.matches(method)) {// match  需要子类 来实现
					match = candidate;
				}
			}
			return match;
		}
	}

```

他的实现类：LookupOverride

主要属性：
- private final String beanName;
- private Method method;

主要方法：

```
public boolean matches(Method method) {
		if (this.method != null) {// 本身的方法对象不为空
			return method.equals(this.method);
		}
		else {
    //本身的方法为空，比较 方法名称，isOverloaded() 默认为 true
    // For backwards compatibility reasons, in a scenario with overloaded
	 //non-abstract methods of the given name, only the no-arg variant of a
	 //method will be turned into a container-driven lookup method
			return (method.getName().equals(getMethodName()) && (!isOverloaded() ||
					Modifier.isAbstract(method.getModifiers()) || method.getParameterCount() == 0));
		}
	}
  ```

  
