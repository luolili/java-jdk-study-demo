1. redis 大 key 和 大 value 问题？

一个简单的 key 存储 一个 大value:  
分成 几个 key-value, 每个字段 是一个 value.  
 >value 设计：string 类型 要小于 10kb,list ,set ,hash,zset里面的元素不超过5000个。
 
 > 非字符串的 key，不要用 del 删除，用 hscan,sscan,zscan 来渐进式删除；防止 bigkey到了过期时间自动删除的情况（会触发 del 操作，造成阻塞）
 
 ##如何排序
 sort 命令：sort day desc (对数字集合排序)  
 sort name alpha(对字符串排序)
 ##热点 key 失效的解决方法
 把 热点 key 分散味多个子 key，存储到不同的 redis机器上，通过某种 hash算法找到子key
 ##redis 集群 master 假死和脑裂
 
 ##redis 如何主从同步
 在 slave 启动的时候， 会给 master 发送一条 sync 的消息，master 收到消息后 会调用 syncCommand 方法进行同步处理，在方法里面会调用 rdbSaveBackground 方法 开启数据备份的进程，进程会执行 rdbSave 方法进行数据的全量备份。  
 备份成功后，会更新 master的各种状态，然后把数据发送给等待的 slave
 ---
 ##为什么用缓存
 提高性能：内存的存取数据快  
 高并发：
 ##单线程模型
 io 多路复用程序 把客户端连接压入队列
 ##如何实现高并发
 读的高并发：一主多从，从负责读；4个 slave 可达到20万/秒的并发量（QPS）
 > redis replication 的机制：
 异步复制数据到 slave 节点；slave node 可连接到其他的 slave node;slave node 在复制的时候，不会影响 master node 的正常工作；slave node 在复制的时候，是用旧的数据来提供服务，当复制完的时候，会暂停服务，把旧的数据删除；
 
 ##幂等性
 redis+token，先获取token,如果有，删除token
 ##redis 挂掉了怎么办
 主从模式
 ##redis 超卖
 分布式锁
 ##hystrix降级原理
 ##枚举为什么是单利