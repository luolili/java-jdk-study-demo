##TxNamespaceHandler 起点
允许基于 xml 声明是配置 和注解是的配置。他是 spring 事务管理的核心:center piece.

有2类管理事务的途径：
在xml 配置 tx开头 advice;基于注解+annotation-driven

主要属性:
```
	static final String TRANSACTION_MANAGER_ATTRIBUTE = "transaction-manager";

	static final String DEFAULT_TRANSACTION_MANAGER_BEAN_NAME = "transactionManager";

```

主要方法：
```
	static String getTransactionManagerName(Element element) {
    		return (element.hasAttribute(TRANSACTION_MANAGER_ATTRIBUTE) ?
    				element.getAttribute(TRANSACTION_MANAGER_ATTRIBUTE) : DEFAULT_TRANSACTION_MANAGER_BEAN_NAME);
    	}

@Override
	public void init() {
		registerBeanDefinitionParser("advice", new TxAdviceBeanDefinitionParser());
		//AnnotationDrivenBeanDefinitionParser annotation-driven
		registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
		registerBeanDefinitionParser("jta-transaction-manager", new JtaTransactionManagerBeanDefinitionParser());
	}

```
NamespaceHandlerSupport 的方法
```
private final Map<String, BeanDefinitionParser> parsers =
			new HashMap<String, BeanDefinitionParser>();
protected final void registerBeanDefinitionParser(String elementName, BeanDefinitionParser parser) {
		this.parsers.put(elementName, parser);
	}
```

---
##AnnotationDrivenBeanDefinitionParser
主要方法：
```
@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		registerTransactionalEventListenerFactory(parserContext);
		//mode 是<tx:annotation-driven 的一个属性
		String mode = element.getAttribute("mode");
		if ("aspectj".equals(mode)) {
			// mode="aspectj"
			registerTransactionAspect(element, parserContext);
		}
		else {
			// mode="proxy" 只分析这个分支
			//注册了3个 bean：AnnotationTransactionAttributeSource，TransactionInterceptor，BeanFactoryTransactionAttributeSourceAdvisor
			AopAutoProxyConfigurer.configureAutoProxyCreator(element, parserContext);
		}
		return null;
	}

```