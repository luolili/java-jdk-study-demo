##秒杀过程
提交订单--创建订单--支付--减库存：在一个实物里面 完成需要3分钟。

方法：  
1.拆分服务：订单服务，支付服务，库存服务，商品服务，会员服务，页面服务，时间服务，配置中心，注册中心，  

在订单服务和支付服务之间加个mq,前台提交订单后放入队列（前台调到支付页面），在队列里保存到数据库；数据库返回订单id 给订单服务，然后返回给前台  

秒杀政策：productId,price,miaosha_price,startTime,endTime  

##自动修改订单的状态
UpdateOrderStatus

##下单
接受下单请求的应用和处理下单逻辑的应用是分开的，可能不在同一个服务器上。
>线程1接收到http请求之后，会把请求放在消息队列中，应用2所在的服务器，监听到有下单的http请求之后，由应用2来处理下单的逻辑;当下单的逻辑处理完成之后，会将结果放置在消息队列中，同时在应用1中有另外一个线程，线程2监听到消息队列中有下单完成的消息之后，根据下单的结果做出相应的http响应

用 Callable+FutureTask+Thread 并行运行多个方法的调用  

提高性能：nginx（分流到不同的tomcat）+缓存。  
一个 tomcat 500的并发请求。
##cache的意义
cache 的大小远远小于 主内存的大小；其意义：时间局部性：若某个数据被访问，那他不久也会被访问；空间局部性原理：若某个数据被访问，他附近的数据很快被访问。
##多线程的风险
安全性；活跃性（死锁）  
性能：线程上下文切换；同步手段抑制编译优化；消耗更多的内存