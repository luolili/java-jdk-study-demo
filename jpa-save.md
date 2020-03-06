# hibernate 保存一个对象的过程？
- UserRepo 调用 SimpleJpaRepository 的 save 方法， SimpleJpaRepository 的 属性：entityInformation
> entityInformation 具有 entityName, id元数据，model元数据，domainClass: name 属性：类的全限定名；versionAttribute:版本属性，值是Optional.Empty


- entityInformation 调用他的isNew方法，调用他的父类 ： AbstractEntityInformation 的 isNew(T entity)
```
public boolean isNew(T entity) {

		ID id = getId(entity);//id=null
		Class<ID> idType = getIdType();//String

		if (!idType.isPrimitive()) {
			return id == null;
		}

		if (id instanceof Number) {
			return ((Number) id).longValue() == 0L;
		}

		throw new IllegalArgumentException(String.format("Unsupported primitive id type %s!", idType));
	}

```

如何获取id？

```
public ID getId(T entity) {

		BeanWrapper entityWrapper = new DirectFieldAccessFallbackBeanWrapper(entity);//entityWrapper 有 WrappedObject 封装要保存的对象
    // id元数据 保存了jpaEntityName: User, hasIdentifierProperty=true, isVersioned=false,version=null,id=null,hasIdClass=false
    //attributes: name=id, javaType=String
		if (idMetadata.hasSimpleId()) {//判断 attributes 的size==1
			return (ID) entityWrapper.getPropertyValue(idMetadata.getSimpleIdAttribute().getName());
		}

		BeanWrapper idWrapper = new IdentifierDerivingDirectFieldAccessFallbackBeanWrapper(idMetadata.getType(), metamodel);
		boolean partialIdValueFound = false;

		for (SingularAttribute<? super T, ?> attribute : idMetadata) {
			Object propertyValue = entityWrapper.getPropertyValue(attribute.getName());

			if (propertyValue != null) {
				partialIdValueFound = true;
			}

			idWrapper.setPropertyValue(attribute.getName(), propertyValue);
		}

		return partialIdValueFound ? (ID) idWrapper.getWrappedInstance() : null;
	}
``` 

SimpleJpaRepository 的 save方法
```
@Transactional
	public <S extends T> S save(S entity) {

		if (entityInformation.isNew(entity)) {
			em.persist(entity);// SharedEntityManagerCreator 调用 
			return entity;
		} else {
			return em.merge(entity);
		}
	}
``` 

代理来完成persist方法的调用
```

@Override
		@Nullable
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			// Invocation on EntityManager interface coming in...

			if (method.getName().equals("equals")) {
				// Only consider equal when proxies are identical.
				return (proxy == args[0]);
			}
			else if (method.getName().equals("hashCode")) {
				// Use hashCode of EntityManager proxy.
				return hashCode();
			}
			else if (method.getName().equals("toString")) {
				// Deliver toString without touching a target EntityManager.
				return "Shared EntityManager proxy for target factory [" + this.targetFactory + "]";
			}
			else if (method.getName().equals("getEntityManagerFactory")) {
				// JPA 2.0: return EntityManagerFactory without creating an EntityManager.
				return this.targetFactory;
			}
			else if (method.getName().equals("getCriteriaBuilder") || method.getName().equals("getMetamodel")) {
				// JPA 2.0: return EntityManagerFactory's CriteriaBuilder/Metamodel (avoid creation of EntityManager)
				try {
					return EntityManagerFactory.class.getMethod(method.getName()).invoke(this.targetFactory);
				}
				catch (InvocationTargetException ex) {
					throw ex.getTargetException();
				}
			}
			else if (method.getName().equals("unwrap")) {
				// JPA 2.0: handle unwrap method - could be a proxy match.
				Class<?> targetClass = (Class<?>) args[0];
				if (targetClass != null && targetClass.isInstance(proxy)) {
					return proxy;
				}
			}
			else if (method.getName().equals("isOpen")) {
				// Handle isOpen method: always return true.
				return true;
			}
			else if (method.getName().equals("close")) {
				// Handle close method: suppress, not valid.
				return null;
			}
			else if (method.getName().equals("getTransaction")) {
				throw new IllegalStateException(
						"Not allowed to create transaction on shared EntityManager - " +
						"use Spring transactions or EJB CMT instead");
			}

			// Determine current EntityManager: either the transactional one
			// managed by the factory or a temporary one for the given invocation.
			EntityManager target = EntityManagerFactoryUtils.doGetTransactionalEntityManager(
					this.targetFactory, this.properties, this.synchronizedWithTransaction);

			if (method.getName().equals("getTargetEntityManager")) {
				// Handle EntityManagerProxy interface.
				if (target == null) {
					throw new IllegalStateException("No transactional EntityManager available");
				}
				return target;
			}
			else if (method.getName().equals("unwrap")) {
				Class<?> targetClass = (Class<?>) args[0];
				if (targetClass == null) {
					return (target != null ? target : proxy);
				}
				// We need a transactional target now.
				if (target == null) {
					throw new IllegalStateException("No transactional EntityManager available");
				}
				// Still perform unwrap call on target EntityManager.
			}
      //transactionRequiringMethods 包含的方法名称有6个：persist,refresh,remove,merge,flush,joinTransaction
			else if (transactionRequiringMethods.contains(method.getName())) {
				// We need a transactional target now, according to the JPA spec.
				// Otherwise, the operation would get accepted but remain unflushed...  target 不为null,进入 boolean isNewEm = false;
				if (target == null || (!TransactionSynchronizationManager.isActualTransactionActive() &&
						!target.getTransaction().isActive())) {
					throw new TransactionRequiredException("No EntityManager with actual transaction available " +
							"for current thread - cannot reliably process '" + method.getName() + "' call");
				}
			}

//target 不为null, 
			// Regular EntityManager operations.
			boolean isNewEm = false;
			if (target == null) {
				logger.debug("Creating new EntityManager for shared EntityManager invocation");
				target = (!CollectionUtils.isEmpty(this.properties) ?
						this.targetFactory.createEntityManager(this.properties) :
						this.targetFactory.createEntityManager());
				isNewEm = true;
			}

			// Invoke method on current EntityManager.
			try {
      //调用EntityManager接口里面的方法，target 就是 SessionImpl
				Object result = method.invoke(target, args);//result=null;更新的话，result=更新后的对象
				if (result instanceof Query) {
					Query query = (Query) result;
					if (isNewEm) {
						Class<?>[] ifcs = ClassUtils.getAllInterfacesForClass(query.getClass(), this.proxyClassLoader);
						result = Proxy.newProxyInstance(this.proxyClassLoader, ifcs,
								new DeferredQueryInvocationHandler(query, target));
						isNewEm = false;
					}
					else {
						EntityManagerFactoryUtils.applyTransactionTimeout(query, this.targetFactory);
					}
				}
				return result;
			}
			catch (InvocationTargetException ex) {
				throw ex.getTargetException();
			}
			finally {
				if (isNewEm) {
					EntityManagerFactoryUtils.closeEntityManager(target);
				}
			}
		}
```

方法参数method:
- clazz = EntityManager; name = persist(方法名称)
方法参数 args: 传进来的 User 对象


