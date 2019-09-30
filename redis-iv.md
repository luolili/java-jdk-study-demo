# Redis
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
  配置：save 60 1000 : 在60秒内至少有1000个改动的时候，自动保存一次数据集。
  - AOF: append only file: 把所有对redis操作的命令，保存到文件里面，
     重新执行这些文件里面的命令来恢复数据， 速度慢。
     配置：默认appendonly no,开启：appendonly yes.
     aof的三个同步策略：
      - 每次擦做的时候，都会刷新文件，很慢，but 安全
      - 每秒同步刷新一次，可能会丢失一些数据
      - 让操作系统在需要的时候刷新数据，不安全
      
      配置：默认是appendasync everysec
     
    虽然rdb恢复数据的速度很快，但是会丢失大量的数据；一般用aof日志来恢复数据。
    redis4的混合持久化： 在aof刷新的时候，先记录上次的dump.rdb版本，
    然后记录上次的魁召版本到现在的增量操作，然后合并成一个文件，覆盖原来的appendonly.aof文件，
    redis重启的时候，先加载快照的内容，然后重放aof文件里面增量操作的内容。
    
    note: appendonly.aof里面有2部分内容：一部分是rdb,一部分是aof.
    
    开启：aof-use-rdb-preamble yes
7. 主从模式的作用是什么？
   - 数据的冗余备份
     即便我们开启了RDB or AOF也不能保证数据不丢失，当硬盘损坏了，数据也就丢失了；
    在slave上进行数据备份， 提高数据的抗灾能力。当slave要删除旧的数据，加载新的数据的时候，
   slave会阻塞连接的请求。
   - 完成读写分离
    master不做持久化，做查询 ；slave只写，进行修改操作。
      
8. 如何发现热key?
    - 热key就是突然有几百万个请求去访问redis撒行的同一个key，导致redis崩掉，从而接下来的请求就到数据库了。
    秒杀的商品；
    
 ---
 
 1. 如何保证缓存和数据可的双写一致性？
 
 只要是双写，就一定存在数据一致性的问题。
 
 cache aside pattern: 读的时候，先读缓存，缓存没有才读数据库；然后取出 数据放入缓存；更新的时候，先更新数据库，然后再删除缓存。
 
 为什么是删除缓存，而不是更新缓存。缓存里面的数据可能是需要关联多个表的数据，并进行计算，才能计算出缓存里面的数据。更新缓存的代价很高。
 另外这个缓存是不是会被频繁访问？
 
 删除缓存，是lazy计算的思想。不是每次都要做复杂的计算。需要的时候才做这个计算。
 
 问题：先修改数据库，然后删除缓存，如果删除缓存失败的，怎么办？先删除缓存，再修改数据库。
 
 更复杂的情况：先删除了缓存，再去修改数据库，此时还没有修改数据库的时候，一个请求就过来了，先查缓存，发现缓存是空的，去查数据库，
 查到的还是旧数据，然后又把他放到缓存里面，最后数据库修改成功，但是数据库和缓存里面的数据就不一致了。
 
 只有再对数据再并发读写的时候，才会出现这个问题。
 
 
2.如何用redis实现排行榜？

- 使用redis的zset
- 命令zadd:  zadd page_rank 10 google.com
page_rank:key; 10:排名；google.com:value

zrange page_rank 0 -1 : 显示所有的排名

zrank page_rank google.com : 获取google.com的排名：从0开始

- 取反：zset默认是从小到大排序

````
public Double getScore(Long oneDayGoldBean){
String score = String.valueOf(oneDayGoldBean)
return -Double.valueOf(score);
}
```

3. Hash 的实现方式？
- 存储用户信息对象：包含用户id，name，age,birthday,通过用户id获取该用户的name，age.
hash 内部的value 是一个 HashMap ，他的key是属性，value 是属性的值，通过key:用户id+field(redis 把hashmap的key称为field)
获取对应的属性数据。

- 当 hashmap的成员比较少的时候，用的是类似一维数组，而不是真正的 hashmap结构，这个时候，对应的value的redisObject的encoding是zipmap;
当成员数量大的时候，是ht

4.list 的实现方式？
- 关注列表，粉丝列表用 list存储
- list 是一个双向的链表，可以反向查找和遍历

5. set？
- 可以自动排序，其value永远为 null的 HashMap,通过计算hash来快速排序

6. sorted set?
- 可以通过用户提供的 优先级：score 来为成员排序，插入有序的， 以发表时间作为 score 来排序
- 用hashmap来存储，用skipList 排序；hashmap是存放成员到 score的映射，跳跃表 是存放所有的成员

---
1. 对于设置了过期时间的key的注意事项？
> 导致 key 过期时间被删除的操作
- del/set/getset 等命令 会清楚过期时间
- persist操作会清楚过期时间，把 key 转为 一个持久化的 key
- 使用 expire/pexpire 设置的过期时间 为 负数 或者是  过去的时间；expire 可以更新 过期时间
- rename 导致旧的 key 的过期时间 将会转移到 新的key
- incr / lpush/hset 不会删除 过期时间

2.redis 的数据结构的应用？
- String : 缓存，限流，计数器，分布式锁，分布式Session
- hash: 用户信息，用户主页访问量，组合查询
- list: 微博关注人时间轴 列表
- Set: 赞，标签，好友关系，踩
- zset:排行榜

大促销：扣减库存：
> redis 扣减库存 --记录扣减日志 --同步worker--库存DB



