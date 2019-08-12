1. 反射中，Class.forName 和 ClassLoader 区别:
前者会不仅会加载.class文件到jvm里面，还会对类进行解释， 执行类里面的static块； 后者只是加载.class文件，
只有在调用了newInstance方法之后，才会去执行static块

2. 不实现个性化的equals和hashCode方法的坏处：
不重写hashCode会降低map等集合的索引速度； equals相等的2个对象， 它们的hashCode也是一样的。

3. 为什么不能在静态方法里面调用非静态成员？
因为静态方法的调用是不依赖类的实例对象的，而非静态成员的调用要依赖类的实例对象。

4. 成员变量和局部变量的几个区别？
  -- 成员变量是属于类的，局部变量是定义在方法里面的或者是方法的参数；局部变量不能加访问修饰符和static。它们都可以加final
  -- 局部变量是存在堆栈上
  -- 局部变量不会自动赋值；没用被final修饰的成员变量有默认的初始化的值，被final修饰的成员变量必须显示的赋值。
  
5. 构造方法的特点？
 -- 没用返回值，也不能用void 声明构造方法
 -- 不需要调用
 
#Collection
1.对于ArrayList,优先使用普通for循环； LinkedList:优先使用foreach/iterator


#并发
1. 进程：是程序运行的基本单位，是程序的一次执行过程。
当运行main方法的时候，就启动了一个jvm进程，main方法是一个主线程；
每个线程有自己的程序计数器，虚拟机战，本地方法站。
一个Java查询的运行包含main线程和其他线程的执行。


#mysql
1. 5.5版本之前用的是myisam引擎， 他不支持事务和行级锁,外键；崩溃后无法安全恢复；5.5之后用innoDB
2. 索引：btree索引+ hash索引。
innoDB的btree: 树的叶节点data保存的是数据记录， 索引的key是表的主键。一个叶节点包含key和data
3. sql一直很慢的原因：
select * from t where c-2=11
上面再等号左边使用了运算导致索引没用上，select * from c=10+2是可以的
select * from where pow(c, 2) =1000
上面等号左边有函数操作导致没用索引

#Redis
1. 缓存雪崩：redis缓存挂掉了，请求跑到数据库。
解决方法：
 -- 事发前：实现redis的高可用：redis集群，redis cluster
 --事发中：设置本地缓存：ehcache+限流hystrix,这样可以避免数据库挂掉
 --事发后：做redis的持久化，在重启后从磁盘加载数据，快速恢复缓存数据
 
 2. 缓存穿透： 查询一个一定不存在的数据，如果数据库也不存在这个数据，
 那么就不会写入缓存。每次查询这个不存在的数据都会请求数据库，
 失去了缓存的意义
 
 如每次请求的id都是负数，每次都要查询数据库，返回空对象。
 解决方法：
   -- 使用布隆 过滤器Bloom Filter or 压缩filter提前拦截不合法
   的请求参数；
   -- 把空对象写入缓存，给他设置一个较短的过期时间。
   
   
  #Docker
  1.docker的镜像和容器有什么区别？
  镜像是个只读的模板，一个独立的文件系统，叫统一文件系统，union file system,镜像可以基于dockerfile来构建；
  docker镜像创建的实例就是容器，容器是用来运行app。每个容器是相互隔离的。
  
  
#clean code
1. 如果方法参数多于3个，用类来封装
2. 用最简单的方式解决

#http
1. URI: 统一资源标识符：uniform resource identifier
包含URL:定位符：locator + 统一资源名称：uniform resource name
2. status code:

 1XX: informational :信息性状态码，接受的请求正在处理
 
 2XX： Succeeded，请求ok,处理完毕
 
 204: no content: 只需要客户端向服务端发送信息，
 不需要服务端返回数据
 206：partial content: 客户端进行了范围请求，
 响应报文包含的只是Content-Range所指定的范围
 的实体内容
 
 3XX： redirection, 需要进行附加操作来完成请求
 301: moved permanently:永久性redirect
 302:Found:临时性redirect，使用Get
 303: See other,要求客户端必须使用Get请求获得资源
 
 304: not modified: 请求报文里面包含
 如：if-Match, if-Modified-Since,若不满足这些条件，
 会有304
 
 4XX：client err,客户端错误，服务器无法处理请求
 400：bad request: 请求里面存在语法错误
 401：Unauthorized:发送的请求需要有认证信息
 
 5XX: server err
 
 #spring
 1. spring 里面的bean 是线程安全的吗？
 spring没用对单例模式的bena做多线程的封装处理；
 对于dao层，因为他是不存储数据的，所以他是无状态的，
 从某种程度上说，他是安全的；
 对于VO层来说他是要村塾数据的，也就是有状态的，
 需要使用多例模式保证线程安全。
 
 2. spring的bean的作用域有哪些？
  1. request: 每次HTTP请求都会重新创建一个bean；
  2. session: 同一个HTTP session共享一个bean
  3. singleton/prototype/global-session
 
 3. spring的自动装配方式有哪几种？
  1. byName
  2. byType
  3.构造方法
  4. no --默认，使用显示bean引用
  5.autodetect: 先用构造方法 + @Autowired装配，不行用byType
  
  #ORM
  1. mybatis里面的#与$ 符号的区别
  mybatis会把sql里面的#{} 替换为?
  2. RowBounds是一次性查询全部结果吗？
  不是。Fetch Size配置了每次可以取出多少条数据。
