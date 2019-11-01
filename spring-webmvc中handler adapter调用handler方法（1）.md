# RequestMappingHandlerAdapter 里面的方法 invokeHandlerMethod
```
protected ModelAndView invokeHandlerMethod(HttpServletRequest request,
			HttpServletResponse response, HandlerMethod handlerMethod) throws Exception 
```

```
ServletWebRequest webRequest = new ServletWebRequest(request, response);
```

把 HttpServletRequest 适配 成 ServletWebRequest。adapter 模式：对象适配器。

被 适配者 ：HttpServletRequest；目标接口：RequestAttributes，里面有 方法 getAttribute .
适配者：ServletRequestAttributes。

ServletRequestAttributes 的主要属性：

1.private final HttpServletRequest request;

2.private HttpServletResponse response;

3.private volatile HttpSession session;


构造方法：
```
public ServletRequestAttributes(HttpServletRequest request) {
		Assert.notNull(request, "Request must not be null");
		this.request = request;
	}
  
  public ServletRequestAttributes(HttpServletRequest request, @Nullable HttpServletResponse response) {
		this(request);
		this.response = response;
	}
```

主要方法：

```
@Override // 覆盖 RequetsAttributes 接口里面的 getAttribute 方法
	public Object getAttribute(String name, int scope) {
		if (scope == SCOPE_REQUEST) {
			if (!isRequestActive()) {
				throw new IllegalStateException(
						"Cannot ask for request attribute - request is not active anymore!");
			}
			return this.request.getAttribute(name);// 调用 HttpServletRequest 的方法
		}
		else {
			HttpSession session = getSession(false);
			if (session != null) {
				try {
					Object value = session.getAttribute(name);
					if (value != null) {
						this.sessionAttributesToUpdate.put(name, value);
					}
					return value;
				}
				catch (IllegalStateException ex) {
					// Session invalidated - shouldn't usually happen.
				}
			}
			return null;
		}
	}
```

对 setAttribute,removeAttribute，getAttributeNames，registerDestructionCallback 等 方法也做了配置

registerDestructionCallback 方法是怎么做的？

AbstractRequestAttributes 实现了 registerDestructionCallback。AbstractRequestAttributes 实现了 RequestAtrtributes 接口，用于解决
请求完成之后的任务。

主要属性： 
- protected final Map<String, Runnable> requestDestructionCallbacks = new LinkedHashMap<>(8);//key:attr ; 
主要方法：

```
protected final void registerRequestDestructionCallback(String name, Runnable callback) {
		Assert.notNull(name, "Name must not be null");
		Assert.notNull(callback, "Callback must not be null");
		synchronized (this.requestDestructionCallbacks) {
			this.requestDestructionCallbacks.put(name, callback);
		}
	}
  ```
  执行回调：利用 Runnable 的 run 方法
  ```
  private void executeRequestDestructionCallbacks() {
		synchronized (this.requestDestructionCallbacks) {
			for (Runnable runnable : this.requestDestructionCallbacks.values()) {
				runnable.run();
			}
			this.requestDestructionCallbacks.clear();
		}
	}
``` 
  
