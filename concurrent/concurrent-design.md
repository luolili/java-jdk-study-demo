## 高并发的系统如何做限流？
1.1加缓存：提高访问速度和访问量，保护数据库，大型网站主要是 读。

1.2 降级：对一些服务和页面有策略的降级，确保核心人物正常运行，策略：拒绝服务，延迟服务，随机服务；

根据服务范围：可以砍掉一些功能/模块。

1.3 限流：算法：计数器+令牌桶（guava）

1.4  Nginx:gx_http_limit_conn+gx_http_limit_req

limit_conn:对同一个ip的连接数，并发量限制；

## 秒杀系统的思考？
三个层次：高性能+一致性（扣库存）+高可用；

高性能：高读+高写，高读，就尽量少读，高写：数据拆分；

高性能：数据的动静分离：秒杀页面只有时间在不停的变动，其他不变。

动静分离3个step:
> 1.数据拆分：分离出动态数据；用户：user info+login status+avatar 通过动态请求获取；用户偏好，地域偏好：异步方式加载；秒杀时间：动态请求获取
>2.静态缓存：对http链接进行缓存，而不是仅仅对数据缓存，key:url；缓存到浏览器+cdn+服务端；用浏览器必须让用户手动刷新；服务端：解析http较慢；cdn:常用：处理大并发的静态资源的请求。但必须保证全国的cdn在秒级失效缓存数据+命中率：在某些cdn节点上进行缓存，和谐节点的特点：访问量集中的地方+网络好+距离主站远。最后选择cdn的二级缓存：缓存数量少+容量大。

>3.数据整合：前台如何组织数据页面，主要是如何加载动态数据？2类方法：ESI（web服务器请求动态数据，用户体验好，他要求服务器性能高）+CSI（web服务器只返回静态页面，发一个异步js请求动态数据）

热点优化：热点操作（零点刷新/下单/加购物车，防止用户频繁刷页面）+热点数据

热点数据3step：
>1.热点识别：热点数据分静态热点（可提前预测的热点数据，统计处 top n的商品）+动态热点（无法预测）。

>2.热点隔离：热点数据识别出来之后需要隔离他们，三个层次:业务隔离（提前对热点进行缓存预热）+系统隔离（分组部署，或单独给秒杀一个域名）+数据隔离（单独给秒杀一个数据库/db服务组 或缓存集群，为了横向/纵向扩展 ）

>3.热点优化：缓存（可长期缓存静态数据）+限流

系统/代码优化：

- 减少序列化：减少 rpc 调用，把多个关联比较强的模块 合并部署（微服务设计规范）
- 字符变字节流，减少数据 的编码转换，只打印 DO 的基础要素和核心要素
- 控制异常heap/stack 输出的深度

---
一致性

1.减库存的方式
- 下单减
- 付款减
- 预扣库存

下单减：用户体验好，不会出现下单后不能付款的情况；缺点:可能卖不出去，一般不会出现，恶意下单不付款的情况会导致库存清零；

付款减：可避免下单减的恶意下单，缺点：200人下单成功，对100个商品，导致很多下单的人不能付款，用户体验差。

预扣库存：下单时预减库存，付款时减库存，没有解决恶意下单。最常用：下单后有个有效付款时间。

对恶意下单的解决方法：对象频繁下单不付款的人进行标记，限制下单不付款的次数。

超卖：对有些商品，是不允许库存为负。

方法：

1.事务：若减库存后为0，就回滚

2.设置数据库 库存的类型是无符号整数，当库存为负会报错

3.case when

优化：库存是热点数据。

*秒杀场景解决高并发读问题，是通过分层判断/校验。*

如用户是否有秒杀资格，商品状态，秒杀是否结束等。目的是过滤掉无效请求。

*秒杀场景解决高并发写问题，是通过优化db。*

---
高可用

1.流量削峰

add 答题，提高购买的复杂度，延迟请求+防止作弊，对提交所有的时间进行判断，防止机器答题

2.排队

消息队列：缺点：请求积压（虽然保护了下游系统，但和丢弃请求没多大区别）+用户体验差

3.过滤：分层

对读进行限流+读缓存+写限流+写校验

高可用建设涉及：架构（可扩展+容错，避免单点问题：多地方单元化部署 ，即使 某个IDC出现问题，也不会影响系统运行）+编码（健壮性，如rpc调用时，设置合理的超时推出机制，防止被其他系统拖垮，对无法预料的返回错误进行默认处理）+测试+发布+运行（实时监控）+故障发生（定位原因）


##高并发的架构
1.分层：目的是为了后续系统的可拆分+可复用。

2.分割：分为多个系统。

3.分布式：

分布式部署子系统+分布式静态资源（动静分离，用单独的域名）+分布式数据+分布式计算（对后台批量处理的任务）

4.集群：分布式部署的系统，需要集群才可对外提供服务；多台服务器部署同一个系统构成一个集群，通过负载均衡设备提供服务

5.（多级）缓存：本地缓存+分布式缓存。

6.异步：MQ

7.冗余备份

