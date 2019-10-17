# 入口：DispatcherServlet 的 doService 方法里面的 doDispatch 方法	mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

handle 方法：来自 AbstractHandlerMethodAdapter 
```
public final ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		return handleInternal(request, response, (HandlerMethod) handler);
	}
  ```
  
  handleInternal(request, response, (HandlerMethod) handler);方法：来自 RequestMappingMethodAdapter
  
  里面调用： mav = invokeHandlerMethod(request, response, handlerMethod);
  
  ```
  protected ModelAndView invokeHandlerMethod(HttpServletRequest request,
			HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {

		ServletWebRequest webRequest = new ServletWebRequest(request, response);
		try {
			WebDataBinderFactory binderFactory = getDataBinderFactory(handlerMethod);
			ModelFactory modelFactory = getModelFactory(handlerMethod, binderFactory);

			ServletInvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
			if (this.argumentResolvers != null) {// 参数解析
				invocableMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
			}
			if (this.returnValueHandlers != null) {
				invocableMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
			}
			invocableMethod.setDataBinderFactory(binderFactory);
			invocableMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);

			ModelAndViewContainer mavContainer = new ModelAndViewContainer();
			mavContainer.addAllAttributes(RequestContextUtils.getInputFlashMap(request));
			modelFactory.initModel(webRequest, mavContainer, invocableMethod);
			mavContainer.setIgnoreDefaultModelOnRedirect(this.ignoreDefaultModelOnRedirect);

			AsyncWebRequest asyncWebRequest = WebAsyncUtils.createAsyncWebRequest(request, response);
			asyncWebRequest.setTimeout(this.asyncRequestTimeout);

			WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
			asyncManager.setTaskExecutor(this.taskExecutor);
			asyncManager.setAsyncWebRequest(asyncWebRequest);
			asyncManager.registerCallableInterceptors(this.callableInterceptors);
			asyncManager.registerDeferredResultInterceptors(this.deferredResultInterceptors);

			if (asyncManager.hasConcurrentResult()) {
				Object result = asyncManager.getConcurrentResult();
				mavContainer = (ModelAndViewContainer) asyncManager.getConcurrentResultContext()[0];
				asyncManager.clearConcurrentResult();
				if (logger.isDebugEnabled()) {
					logger.debug("Found concurrent result value [" + result + "]");
				}
        //
				invocableMethod = invocableMethod.wrapConcurrentResult(result);
			}

//--
			invocableMethod.invokeAndHandle(webRequest, mavContainer);
			if (asyncManager.isConcurrentHandlingStarted()) {
				return null;
			}

			return getModelAndView(mavContainer, modelFactory, webRequest);
		}
		finally {
			webRequest.requestCompleted();
		}
    ```
    
    ```
    public void invokeAndHandle(ServletWebRequest webRequest, ModelAndViewContainer mavContainer,
			Object... providedArgs) throws Exception {
    // 获取方法的参数
		Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);
		setResponseStatus(webRequest);
    }
    ```
    
    ```
    public Object invokeForRequest(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer,
			Object... providedArgs) throws Exception {
    // 获取方法的参数
		Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
		if (logger.isTraceEnabled()) {
			logger.trace("Invoking '" + ClassUtils.getQualifiedMethodName(getMethod(), getBeanType()) +
					"' with arguments " + Arrays.toString(args));
		}
		Object returnValue = doInvoke(args);
		if (logger.isTraceEnabled()) {
			logger.trace("Method [" + ClassUtils.getQualifiedMethodName(getMethod(), getBeanType()) +
					"] returned [" + returnValue + "]");
		}
		return returnValue;
    ```

request: 里面包含 request：ShiroHttpServletRequest,这个request里面 包含：request:RequestFacade,他里面包含了 Request 对象；

coyoteRequest=R{/work/api/coupon}

InvocableHandlerMethod 的 getMethodArgumentValues
```
	@Nullable
	public Object invokeForRequest(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer,
			Object... providedArgs) throws Exception {
			//获取当前请求的参数
		Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
		}
```

```
private Object[] getMethodArgumentValues(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer,
			Object... providedArgs) throws Exception {
		// 获取参数对象数组
		MethodParameter[] parameters = getMethodParameters();
		Object[] args = new Object[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			MethodParameter parameter = parameters[i];
			parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
			args[i] = resolveProvidedArgument(parameter, providedArgs);
			if (args[i] != null) {
				continue;
			}
			
			
			if (this.argumentResolvers.supportsParameter(parameter)) {
				try {
					
					args[i] = this.argumentResolvers.resolveArgument(
							parameter, mavContainer, request, this.dataBinderFactory);
							
					continue;
				}
				catch (Exception ex) {
					if (logger.isDebugEnabled()) {
						logger.debug(getArgumentResolutionErrorMessage("Failed to resolve", i), ex);
					}
					throw ex;
				}
			}
			if (args[i] == null) {
				throw new IllegalStateException("Could not resolve method parameter at index " +
						parameter.getParameterIndex() + " in " + parameter.getExecutable().toGenericString() +
						": " + getArgumentResolutionErrorMessage("No suitable resolver for", i));
			}
		}
		return args;
	}
```
argumentResolvers 解析出 参数里面包含的字段以及从前端传过来的字段值。HandlerMethodArgumentResolverComposite resolveArgument 方法。
```
public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

		HandlerMethodArgumentResolver resolver = getArgumentResolver(parameter);
		if (resolver == null) {
			throw new IllegalArgumentException("Unknown parameter type [" + parameter.getParameterType().getName() + "]");
		}
		return resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
	}
```
进入 ModelAttributeMethodProcessor 的 resolveArgument(parameter, mavContainer, webRequest, binderFactory);

```
public final Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

		Assert.state(mavContainer != null, "ModelAttributeMethodProcessor requires ModelAndViewContainer");
		Assert.state(binderFactory != null, "ModelAttributeMethodProcessor requires WebDataBinderFactory");
		// 获取到 参数的参数名称
		String name = ModelFactory.getNameForParameter(parameter);
		 // 获取到参数上的注解
		ModelAttribute ann = parameter.getParameterAnnotation(ModelAttribute.class);
		if (ann != null) {
			mavContainer.setBinding(name, ann.binding());
		}

		Object attribute = null;
		BindingResult bindingResult = null;

		if (mavContainer.containsAttribute(name)) {
			attribute = mavContainer.getModel().get(name);
		}
		else {
			// Create attribute instance
			try {
				attribute = createAttribute(name, parameter, binderFactory, webRequest);
			}
			catch (BindException ex) {
				if (isBindExceptionRequired(parameter)) {
					// No BindingResult parameter -> fail with BindException
					throw ex;
				}
				// Otherwise, expose null/empty value and associated BindingResult
				if (parameter.getParameterType() == Optional.class) {
					attribute = Optional.empty();
				}
				bindingResult = ex.getBindingResult();
			}
		}

		if (bindingResult == null) {
			// Bean property binding and validation;
			// skipped in case of binding failure on construction. 调用 DefaultDataBinderFactory 的 createBinder
			WebDataBinder binder = binderFactory.createBinder(webRequest, attribute, name);//binder 包含属性对象和属性名字
			if (binder.getTarget() != null) {
				if (!mavContainer.isBindingDisabled(name)) {
					bindRequestParameters(binder, webRequest);//先创建 ServletRequest 对象，把WebDataBinder转为ServletRequestDataBinder
				}
				validateIfApplicable(binder, parameter);//对参数上有@Validated 注解 验证
				if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
					throw new BindException(binder.getBindingResult());
				}
			}
			// Value type adaptation, also covering java.util.Optional
			if (!parameter.getParameterType().isInstance(attribute)) {
				attribute = binder.convertIfNecessary(binder.getTarget(), parameter.getParameterType(), parameter);
			}
			bindingResult = binder.getBindingResult();//bindingResult 里面的 target 包含了 参数
		}

		// Add resolved attribute and BindingResult at the end of the model
		Map<String, Object> bindingResultModel = bindingResult.getModel();
		mavContainer.removeAttributes(bindingResultModel);
		mavContainer.addAllAttributes(bindingResultModel);

		return attribute;// 返回 参数对象
	}

```
如何创建参数的实例对象？attribute = createAttribute(name, parameter, binderFactory, webRequest); ServletModelAttributeMethodProcessor
,最终调用 ModelAttributeMethodProcessor super.createAttribute(attributeName, parameter, binderFactory, request);

获取到参数类型的 primary class,若为空，获取 clazz.getConstructors(); 利用构造方法来实例化参数对象。



