#  XmlBeanDefinitionReader 他把月底 xml 文档的任务交给了 BeanDefinitionDocumentReader 的实现类

主要属性：
- public static final int VALIDATION_NONE = XmlValidationModeDetector.VALIDATION_NONE; 不进行 xml 验证
- 	public static final int VALIDATION_AUTO = XmlValidationModeDetector.VALIDATION_AUTO; 自动验证
- public static final int VALIDATION_DTD = XmlValidationModeDetector.VALIDATION_DTD;
- 	public static final int VALIDATION_XSD = XmlValidationModeDetector.VALIDATION_XSD;
- private int validationMode = VALIDATION_AUTO; 默认是自动验证
- private boolean namespaceAware = false; 是否在意命名空间
- private Class<?> documentReaderClass = DefaultBeanDefinitionDocumentReader.class; 文档阅读人
- 	private ProblemReporter problemReporter = new FailFastProblemReporter(); 报告问题的人
- 	private ReaderEventListener eventListener = new EmptyReaderEventListener(); 阅读事件监听的人
- 	private NamespaceHandlerResolver namespaceHandlerResolver; 命名空间解析人
- private DocumentLoader documentLoader = new DefaultDocumentLoader(); 加载文档的人
- private final ThreadLocal<Set<EncodedResource>> resourcesCurrentlyBeingLoaded =
			new NamedThreadLocal<>("XML bean definition resources currently being loaded");  标识文档正在加载
      
   
   
 主要方法：
 1.
 ```
 	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
		super(registry);
	}
 
 ```
 用注册的地方 来创建一个阅读人。
 2.
 ```
 @Override
	public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
		return loadBeanDefinitions(new EncodedResource(resource));
	}
 ```
 
 ```
 //返回 发现的bean definition 的个数； EncodedResource 是 xml file 的资源描述人,指定了 xml 的编码
 public int loadBeanDefinitions(EncodedResource encodedResource) throws BeanDefinitionStoreException {
		Assert.notNull(encodedResource, "EncodedResource must not be null");
		if (logger.isInfoEnabled()) {
			logger.info("Loading XML bean definitions from " + encodedResource.getResource());
		}

//获取要加载的资源文件的 set 列表；若没有就初始化4个元素的 set列表
		Set<EncodedResource> currentResources = this.resourcesCurrentlyBeingLoaded.get();
		if (currentResources == null) {
			currentResources = new HashSet<>(4);
			this.resourcesCurrentlyBeingLoaded.set(currentResources);
		}
    //一个资源文件 不会被多次加载
		if (!currentResources.add(encodedResource)) {
			throw new BeanDefinitionStoreException(
					"Detected cyclic loading of " + encodedResource + " - check your import definitions!");
		}
    //在通过 encoedeReource获取到Resource,通过 Resource 获取到输入来源，出现 io 异常
		try {
			InputStream inputStream = encodedResource.getResource().getInputStream();
			try {
      // 把输入来源 转换为 InputSource, 从而 设置资源的编码，编码从encoedeResource 里面获得
				InputSource inputSource = new InputSource(inputStream);
				if (encodedResource.getEncoding() != null) {
					inputSource.setEncoding(encodedResource.getEncoding());
				}
        //上面的操作是准备 inputSource , 下面是做加载任务
				return doLoadBeanDefinitions(inputSource, encodedResource.getResource());
			}
			finally {
				inputStream.close();// 关闭流
			}
		}
		catch (IOException ex) {
			throw new BeanDefinitionStoreException(
					"IOException parsing XML document from " + encodedResource.getResource(), ex);
		}
		finally {
			currentResources.remove(encodedResource);//当加载完成之后，该资源就不是在当前的资源列表，从 currentResources 里面删除
			if (currentResources.isEmpty()) {
				this.resourcesCurrentlyBeingLoaded.remove();//当加载完成之后，该资源就不是正在加载的状态了，从正在加载的set列表里面删除
			}
		}
	}

 ```
 3.
 ```
 // InputSource 是来自 org.xml.sax 的 类， 不是spring的
 protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource)
			throws BeanDefinitionStoreException {
		try {
    // 获取资源对应的 文档对象
			Document doc = doLoadDocument(inputSource, resource);
      // todo 暂不分析
			return registerBeanDefinitions(doc, resource);
		}
		catch (BeanDefinitionStoreException ex) {
			throw ex;
		}
		catch (SAXParseException ex) {
			throw new XmlBeanDefinitionStoreException(resource.getDescription(),
					"Line " + ex.getLineNumber() + " in XML document from " + resource + " is invalid", ex);
		}
		catch (SAXException ex) {
			throw new XmlBeanDefinitionStoreException(resource.getDescription(),
					"XML document from " + resource + " is invalid", ex);
		}
		catch (ParserConfigurationException ex) {
			throw new BeanDefinitionStoreException(resource.getDescription(),
					"Parser configuration exception parsing XML from " + resource, ex);
		}
		catch (IOException ex) {
			throw new BeanDefinitionStoreException(resource.getDescription(),
					"IOException parsing XML document from " + resource, ex);
		}
		catch (Throwable ex) {
			throw new BeanDefinitionStoreException(resource.getDescription(),
					"Unexpected exception parsing XML document from " + resource, ex);
		}
	}

 
 ``` 
 出现的ex:
 - sax 解析到某一行出现 错误； io ex
