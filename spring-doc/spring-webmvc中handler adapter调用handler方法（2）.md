# RequestMappingHandlerAdapter 里面的 invokeHandlerMethod

```
	WebDataBinderFactory binderFactory = getDataBinderFactory(handlerMethod);
```
WebDataBinderFactory 是一个工厂接口，是 创建web 数据绑定 实例，给有名字的目标对象： named target obj.
ServletRequestDataBinderFactory ，	return new ExtendedServletRequestDataBinder(target, objectName);

方法：
```
WebDataBinder createBinder(NativeWebRequest webRequest, @Nullable Object target, String objectName)
			throws Exception;
```

RequestMappingHandlerAdapter 只是 用 new 来创建工厂 对象。

DefaultDataBinderFactory 实现了 工厂接口。通过 new WebRequestDataBinder(target, objectName);

主要属性：
 
- private final WebBindingInitializer initializer; //初始化人

构造方法：
```
	public DefaultDataBinderFactory(@Nullable WebBindingInitializer initializer) {
		this.initializer = initializer;
	}
```
---
# spring 的 data binder

基类：DataBinder 可以用于 任何环境

主要属性：
- public static final String DEFAULT_OBJECT_NAME = "target";//默认的要绑定的 对象名字。
- public static final int DEFAULT_AUTO_GROW_COLLECTION_LIMIT = 256;// 兵丁的 数组或集合的长度最大256
-protected static final Log logger = LogFactory.getLog(DataBinder.class);//因为要创建很多binder,用静态 log
- private final Object target;
-private final String objectName;
- private boolean ignoreUnknownFields = true;
- private boolean ignoreInvalidFields = false;
-private int autoGrowCollectionLimit = DEFAULT_AUTO_GROW_COLLECTION_LIMIT;

构造方法：
```
public DataBinder(@Nullable Object target, String objectName) {
		this.target = ObjectUtils.unwrapOptional(target);//如果target是 一个 Optional，从 Optional 获取到里面的对象
		this.objectName = objectName;
	}
```

# WebDataBinder web 环境
定义了一个常量，其他主要方法 调用的 DataBinder

# WebRequestDataBinder 可以处理文件上传， 继承 WebDataBinder
通过请求里面 的 Content-Type 判断 请求是否是包含文件上传的请求

# WebExchangeDataBinder 处理 url query params or form data ，继承 WebDataBinder
