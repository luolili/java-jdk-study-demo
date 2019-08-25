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
 
 6.为什么wait/notify方法是定义在Object里面而不是Thread里面？
 
 -如果不能通过Java关键字来实现同步，通信机制，那么在Object里面放入这个2个方法来实现通信机制是正确的做法。
 -每个对象都需要可以上锁
 
 7. 为什么Java不支持多重继承？
 - 多重继承让设计更加复杂，并在转换，构造方法连接等过程中产生问题；可使用接口实现多继承。
 
 8. 为什么Java不支持运算符的重载？
 - 简单性和清晰性： 他可能减慢jvm,因为他需要做额外的工作来识别运算符的实际含义，减少优化的机会；
 - 避免编程错误： 让人的学习曲线变得陡峭，编程错误也会增加
 
 9. 为什么String is final?
 - String 对象是缓存在String池里面，是共享的，所以始终存在风险，，所以让他不可变
 - 从hash map的角度看，对于键值来说，重要的是它们的不可变，用它们来查询它们对应的value对象。
 ---
 10. java的泛型是什么？
  - 其本质是参数化类型，有泛型类，泛型方法，泛型接口
  java5之前 是通过Object的引用来实现参数的任意化，
  但这样要做显示的强制类型转换，这种转换在编译期间是
  不做检查的，会把问题留给运行期
  - 泛型 是在编译期间检查类型安全，并且所有的强制
  转换都是自动隐含的，提高代码重用率，
  避免运行期出现ClassCastException
  
  11. 不同版本的泛型有什么不同？
  - java7：在实例化类型的时候，自动推断
  ```
  List<String> list = new ArrayList<String>();
  ```
  
  可以写成
  ```
    List<String> list = new ArrayList<>();
   ```
   ---
#Collection
1.对于ArrayList,优先使用普通for循环； LinkedList:优先使用foreach/iterator

---
#并发
1. 进程：是程序运行的基本单位，是程序的一次执行过程。
当运行main方法的时候，就启动了一个jvm进程，main方法是一个主线程；
每个线程有自己的程序计数器，虚拟机战，本地方法站。
一个Java查询的运行包含main线程和其他线程的执行。

---
#mysql
1. 5.5版本之前用的是myisam引擎， 他不支持事务和行级锁,外键；崩溃后无法安全恢复；5.5之后用innoDB
2. 索引：btree索引+ hash索引。
innoDB的btree: 树的叶节点data保存的是数据记录， 索引的key是表的主键。一个叶节点包含key和data
3. sql一直很慢的原因：
select * from t where c-2=11
上面再等号左边使用了运算导致索引没用上，select * from c=10+2是可以的
select * from where pow(c, 2) =1000
上面等号左边有函数操作导致没用索引

4. 为什么用自增列作为主键？
没显示定义主键会使用第一个不包含null值的唯一索引作为主键索引；若没这个，
使用innoDB的隐含的6字节长的rowId;
不用自增主键，每插入一条新纪录，需要找到合适位置而移动数据，增加了很多开销

5. 为什么索引可以提高效率？
数据索引的存储是有序的；
通过索引查询一条数据而非遍历索引记录；
效率是二分查找效率

---
#Redis
1. 缓存雪崩：redis缓存挂掉了，请求跑到数据库。
解决方法：
 - 事发前：实现redis的高可用：redis集群，redis cluster
 - 事发中：设置本地缓存：ehcache+限流hystrix,这样可以避免数据库挂掉
 - 事发后：做redis的持久化，在重启后从磁盘加载数据，快速恢复缓存数据
 
 2. 缓存穿透： 查询一个一定不存在的数据，如果数据库也不存在这个数据，
 那么就不会写入缓存。每次查询这个不存在的数据都会请求数据库，
 失去了缓存的意义
 
 如每次请求的id都是负数，每次都要查询数据库，返回空对象。
 解决方法：
   - 使用布隆 过滤器Bloom Filter or 压缩filter提前拦截不合法
   的请求参数；
   - 把空对象写入缓存，给他设置一个较短的过期时间。
   
   
   5. 如果有大量的key设置同一过期时间，需要注意什么？
在时间上加一个随机值，让过期时间分散一些，避免redis出现短暂的卡顿。

6. redis的持久化方案？
  - RDB： 快照形式，定期把数据存放到磁盘里面。服务器断电的时候会丢失部分数据
  - AOF: append only file: 把所有对redis操作的命令，保存到文件里面，
     重新执行这些文件里面的命令来恢复数据， 速度慢。
7. 主从模式的作用是什么？
   - 数据的冗余备份
     即便我们开启了RDB or AOF也不能保证数据不丢失，当硬盘损坏了，数据也就丢失了；
    在slave上进行数据备份， 提高数据的抗灾能力。当slave要删除旧的数据，加载新的数据的时候，
   slave会阻塞连接的请求。
   - 完成读写分离
    master不做持久化，做查询 ；slave只读，不进行修改操作。
      
8. 如何发现热key?
    - 热key就是突然有几百万个请求去访问redis撒行的同一个key，导致redis崩掉，从而接下来的请求就到数据库了。
    秒杀的商品；
  
  
  
  
 #MQ
 1. 数据丢失的3中情况？
 - 生产者在传消息的时候，消息丢失了
 生产者在发送消息之前开启rabbbbitMQ的事务channel.txSelect,如果mq没有收到消息，生产者这边会报错，
 回滚事务，收到了消息，提交事务。
 ```
 channel.txSelect
try {
    // 这里发送消息
} catch (Exception e) {
    channel.txRollback

    // 这里再次重发这条消息
}

// 提交事务
channel.txCommit

 ```
 另外，可以开启confirm模式：
 在生产者那里设置开启 confirm 模式之后，你每次写的消息都会分配一个唯一的 id，然后如果写入了 RabbitMQ 中，RabbitMQ 会给你回传一个 ack 消息，告诉你说这个消息 ok 了。如果 RabbitMQ 没能处理这个消息，会回调你的一个 nack 接口，告诉你这个消息接收失败，你可以重试。而且你可以结合这个机制自己在内存里维护每个消息 id 的状态，如果超过一定时间还没接收到这个消息的回调，那么你可以重发。
 
 事务机制和 confirm 机制最大的不同在于，事务机制是同步的，你提交一个事务之后会阻塞在那儿，但是 confirm 机制是异步的，你发送个消息之后就可以发送下一个消息，然后那个消息 RabbitMQ 接收了之后会异步回调你的一个接口通知你这个消息接收到了。

所以一般在生产者这块避免数据丢失，都是用 confirm 机制的。
 - MQ挂掉了
 持久化到磁盘。
 step:
   - 创建 queue 的时候将其设置为持久化
       这样就可以保证 RabbitMQ 持久化 queue 的元数据，但是它是不会持久化 queue 里的数据的
   - 第二个是发送消息的时候将消息的 deliveryMode 设置为 2
      就是将消息设置为持久化的，此时 RabbitMQ 就会将消息持久化到磁盘上去。
      
 - 消费者得到了这个消息，但是还没来得及处理就挂掉了，MQ以为这个消息已经被处理了。
 你必须关闭 RabbitMQ 的自动 ack，可以通过一个 api 来调用就行，然后每次你自己代码里确保处理完的时候，再在程序里 ack 一把。这样的话，如果你还没处理完，不就没有 ack 了？那 RabbitMQ 就认为你还没处理完，这个时候 RabbitMQ 会把这个消费分配给别的 consumer 去处理，消息是不会丢的。
 
 
---
  #Docker
  1.docker的镜像和容器有什么区别？
  镜像是个只读的模板，一个独立的文件系统，叫统一文件系统，union file system,镜像可以基于dockerfile来构建；
  docker镜像创建的实例就是容器，容器是用来运行app。每个容器是相互隔离的。
  
  ---
#clean code
 - 如果方法参数多于3个，用类来封装
 - 用最简单的方式解决

---
#http
1. URI: 统一资源标识符：uniform resource identifier
包含URL:定位符：locator + 统一资源名称：uniform resource name
2. status code:

 - 1XX: informational :信息性状态码，接受的请求正在处理
 
 - 2XX： Succeeded，请求ok,处理完毕
 
 204: no content: 只需要客户端向服务端发送信息，
 不需要服务端返回数据
 206：partial content: 客户端进行了范围请求，
 响应报文包含的只是Content-Range所指定的范围
 的实体内容
 
 - 3XX： redirection, 需要进行附加操作来完成请求
   - 301: moved permanently:永久性redirect
   - 302:Found:临时性redirect，使用Get
   - 303: See other,要求客户端必须使用Get请求获得资源
 
   - 304: not modified: 请求报文里面包含
 如：if-Match, if-Modified-Since,若不满足这些条件，
 会有304
 
 - 4XX：client err,客户端错误，服务器无法处理请求
   - 400：bad request: 请求里面存在语法错误
   - 401：Unauthorized:发送的请求需要有认证信息
 
 5XX: server err
 
 3. springboot如何解析http参数？
 - 在后台直接写参数名，或者加@RequestParam注解
 这种是直接从HttpServletRequest对象里面获取值:request.getParameter("t")
 
 - 前端用post请求，把参数的json格式放在Body里面，在后台需要加
 @RequestBody注解。
 {
 "id":1,
 "name": "lx"
 }
 
 如果不是spring环境，那么要通过request.getReader()来获取Body里面的数据，
 然后通过json工具类把他转为java bean.
 code:
 ```
 BufferedReader reader = request.getReader();
 
 StringBuilder builder = new StringBuilder();
 
 String line;
 
 while( (line=reader.readLine()) != null ) {
 
 builder.append(line);
 }
 
 SysUser user = JSONObject.parseObject(builder.toString(), SysUser.class)
 
 ```
 
 ---
 #spring
 1. spring 里面的bean 是线程安全的吗？
 spring没用对单例模式的bena做多线程的封装处理；
 对于dao层，因为他是不存储数据的，所以他是无状态的，
 从某种程度上说，他是安全的；
 对于VO层来说他是要村塾数据的，也就是有状态的，
 需要使用多例模式保证线程安全。
 
 2. spring的bean的作用域有哪些？
  - request: 每次HTTP请求都会重新创建一个bean；
  - session: 同一个HTTP session共享一个bean
  - singleton/prototype/global-session
 
 3. spring的自动装配方式有哪几种？
  - byName
  - byType
  - 构造方法
  -  no --默认，使用显示bean引用
  - autodetect: 先用构造方法 + @Autowired装配，不行用byType
  
4. spring为什么默认是单例模式bean?
- 创建bean的时候，先看配置是否是单例，是单例，先从缓存中取，没有创建，指挥创建一次。
- 减少新生成实例的消耗
- 减少jvm垃圾回收
- 可以快速获取到bean
- 不能做到线程安全：在有状态的情况下。


  ---
  #ORM
  1. mybatis里面的#与$ 符号的区别
  mybatis会把sql里面的#{} 替换为?
  2. RowBounds是一次性查询全部结果吗？
  不是。Fetch Size配置了每次可以取出多少条数据。

  3. 获取上一次自动生成的id： select last _insert_id()
  
  ---
  #concurrent--并发
  1. 实现Thread有几种方法？
  - 实现Runnable接口
  - 继承Thread
  
  2. 如何启动Thread?
  
  3. 如何停止Thread？
  用interrupt.
  
  被停止的一方在每次循环中or适当的时候检查终端信号；
  优先在方法层面抛出Exception; 
  volatile的boolean无法处理长时间的阻塞情况
  
  
  4. 线程的一生？
  
  6个。new -- runnable -- terminated
  --blocked--waiting--timed waiting
  5. Thread/Object方法
  调用wait/notifyAll之前，当前线程必须具有sync锁；
  notify：唤醒多个线程中的其中一个,选择方式由jvm决定；
  不可被重写，他们是native/finakl;
  相似功能：Condition.
  6.Thread的各个属性？
  id;名字;守护线程：daemon；优先级。
  
  7. 未捕获Exception怎么处理？
  
  8. 多Thread会导致的问题？

---
#ES
1.ES和lucence关系？
ES基于lucence实现，进行了扩展，提供了更多的查询语句，
通过rest api 与底层交互。

elasticsearch.bat启动ES。

安装kibana:用来连接ES，注意：kibana版本要和ES版本一样.
安装ik分词器，注意：kibana版本要和ES版本一样，解压
目录的下面是config等文件。
测试IK：
```
POST _analyze 
{
"analyzer": "ik_max_word",
"text": "我是一个人"
}

```
concept:

- 索引集合：index--database
  - 类型：type-- table
    - 文档：document--Row
      - 字段：field--column
      
分片：分成多个部分；备份：replica
  
  
 聚合：
 用于统计。