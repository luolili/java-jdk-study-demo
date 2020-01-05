##SqlSessionFactory 从链接 or 数据源中创建SqlSession

接口，定义了 一系列 openSession的重载方法：
```$xslt

SqlSession openSession();

  SqlSession openSession(boolean autoCommit);
  //获取配置内容
   Configuration getConfiguration();
```
SqlSessionFactory 是应用作用域，只创建一次。

SqlSession 是线程私有的，是请求作用域（每次收到一个http请求，就打开，返回一个响应，就关闭）或方法作用域。一个 sqlSession和数据库进行一次交互
##他的一个默认实现--DefaultSqlSessionFactory

主要属性：
```$xslt
 private final Configuration configuration;
 //唯一的构造方法
  public DefaultSqlSessionFactory(Configuration configuration) {
    this.configuration = configuration;
  }
```
核心方法：
```$xslt
 @Override
  public SqlSession openSession() {
    return openSessionFromDataSource(configuration.getDefaultExecutorType(), null, false);
  }
  
   private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) {
      Transaction tx = null;
      try {
        final Environment environment = configuration.getEnvironment();
        final TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
        tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
        final Executor executor = configuration.newExecutor(tx, execType);
        //DefaultSqlSession 里面是sql 语句的实现
        return new DefaultSqlSession(configuration, executor, autoCommit);
      } catch (Exception e) {
        closeTransaction(tx); // may have fetched a connection so lets call close()
        throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
      } finally {
        ErrorContext.instance().reset();
      }
    }

```

##sqlSession接口
执行 命令，获得 mapper，处理事务。

核心方法：
```$xslt
    //泛型接口
 <T> T selectOne(String statement);
 <T> T selectOne(String statement, Object parameter);
  <E> List<E> selectList(String statement);
  //return a of Map[Integer,Author] for selectMap("selectAuthors","id")
   <K, V> Map<K, V> selectMap(String statement, String mapKey);
   // 在insert/update/delete时 提交数据库连接
   void commit();
   void rollback();
    void clearCache();
    Configuration getConfiguration();
    <T> T getMapper(Class<T> type);
    //获取数据库连接
     Connection getConnection();
    
```
##sqlSession 的默认实现：DefaultSqlSession not thread-safe

主要属性：
```$xslt
 private Configuration configuration;
  private Executor executor;

  private boolean autoCommit;
   private boolean dirty;
    private List<Cursor<?>> cursorList;
    
```

主要方法：

先获得 mapper：
```$xslt
//根据类型获取 mapper
  @Override
  public <T> T getMapper(Class<T> type) {
    return configuration.<T>getMapper(type, this);
  }
  //Configuration 类，sqlSession 下的 某个类型的mapper
   public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
      return mapperRegistry.getMapper(type, sqlSession);
    }
     //MappedRegistry 类
     public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
     //工厂模式，knownMappers 是个hash map:key:type,value:MapperProxyFactory
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
        if (mapperProxyFactory == null) {
          throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
        }
        try {
          return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
          throw new BindingException("Error getting mapper instance. Cause: " + e, e);
        }
      }
```
###什么是 MapperProxyFactory
主要属性：
```$xslt
// mapper 接口
private final Class<T> mapperInterface;
// mapper 里面的方法定义
  private final Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<Method, MapperMethod>();

 public MapperProxyFactory(Class<T> mapperInterface) {
    this.mapperInterface = mapperInterface;
  }
  
  
```

主要方法：
```$xslt
//MapperProxy 实现了 InvocationHandler
protected T newInstance(MapperProxy<T> mapperProxy) {
    return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
  }
  
   public T newInstance(SqlSession sqlSession) {
      final MapperProxy<T> mapperProxy = new MapperProxy<T>(sqlSession, mapperInterface, methodCache);
      return newInstance(mapperProxy);
    }
```

```$xslt
 @Override
  public <T> T selectOne(String statement) {
    return this.<T>selectOne(statement, null);
  }
  
  //查询结果是0 行，则返回null，返回多个，就报错
  @Override
  public <T> T selectOne(String statement, Object parameter) {
    // Popular vote was to return null on 0 results and throw exception on too many.
    List<T> list = this.<T>selectList(statement, parameter);
    if (list.size() == 1) {
      return list.get(0);
    } else if (list.size() > 1) {
      throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
    } else {
      return null;
    }
  }
  
```

查询多个：
```$xslt
 @Override
  public <E> List<E> selectList(String statement, Object parameter) {
    return this.selectList(statement, parameter, RowBounds.DEFAULT);
  }
  
   @Override
    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
      try {
        MappedStatement ms = configuration.getMappedStatement(statement);
        //executor 是真正执行 sql 的类
        return executor.query(ms, wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER);
      } catch (Exception e) {
        throw ExceptionFactory.wrapException("Error querying database.  Cause: " + e, e);
      } finally {
        ErrorContext.instance().reset();
      }
    }
```

## executor 如何完成 数据库操作

Executor 接口，
主要属性：
```$xslt
//查询结果为0的处理方式
 ResultHandler NO_RESULT_HANDLER = null;
```
主要方法：
```$xslt
 int update(MappedStatement ms, Object parameter) throws SQLException;
 
  <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) throws SQLException;

```
