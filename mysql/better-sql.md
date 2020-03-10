##1.explain 各个参数的含义

1.rows: 扫描的行数
2.type:链接类型，好的sql至少是range级别。

3.key_len:索引长度
4.key：用到的索引的名字

## 2.in 包含的值不能太多
in 里面的常量会被存入一个数组，而且这个数组是排好序的，对于连续的数，用between，或join

##3.当只需要一条数据的时候，用limit 1
他可让type列是const类型

##4.区分in 和 exists

select * from A where id in (select id from B)

select * from A where exists(select * from B  where B.id = A.id)
in 是先执行子查询，适合外表大内表小；
exists 先执行父查询
##5.不在 where 后面进行 null判断和计算

##6.避免隐士类型转换

##库和表的字符集用uft-8
兼容性更好
##单表的数据小于500万
历史数据归档（日志数据），分库分表
##冷热数据分离，减小表的宽度
避免更多的关联操作
##优先用更小的数据存储类型
列的字段越大，建立索引所需空间更大；ip地址转为整形:inet_aton；  
自增id用无符号整形；varchar(255)可以存255个汉字，占765字节
##避免用text,blob
text 占64k数据，mysql的临时表不支持 text，blob大数据类型
##不要用 UUID MD5 HASH 字符串作为主键
无法保证数据的顺序增长
##把区分度最高，字段长度小，使用最频繁的放在联合索引最左
提高 io性能，减少索引的建立
##避免用外键约束
在业务端实现数据的参照完整性，外键会影响父表和子表的写操作
##不同的数据库用不同的账号，禁止跨库查询
安全性
##避免子查询，用 join
子查询的结果集无法用到索引

子查询一般出现在 in 子句里面，子查询是简单sql（没有 union,group by,order by limit），才可变为 join
##避免关联太多的表，小于5张表
##对同一类可以用 or 的时候，用 in来代替
in 的值不超过500个，性能更好
##大 sql 分成 小 sql
一个 sql 只用 一个 cpu
##超过100玩行的批量操作，要分批次进行
造成扈从延迟；大批量修改，一定是在一个事务里进行的，从而导致大量的阻塞，
让其他应用无法链接到数据库
---  
#sql 必知必会
排序
## order by子句要放在最后
##排序默认是升序，若要对多个列进行排序，每个列后面都必须加上关键字
大小写的排序：取决于数据库的设置，字典中 A 与 a 是相同的  
过滤
##客户端代码（用开发语言）也可过滤，但不建议这么做
优化 数据库可以更快的过滤数据，而开发语言过滤数据会降低应用的性能，失去了可伸缩性，另外，它必须通过网络发送多余的数据，浪费网络带宽
## != 和<> 往往可互换，但不是所有的 dbms 都同时支持它们
## between and 包含开始值和结束值
## and 和 or 组合可能会因为计算顺序而得不到预期结果
select * from product where vend_id = 'D1' or vend_id = 'D3'  
and prod_price > 2

正确的写法：  
select * from product where (vend_id = 'D1' or vend_id = 'D3')  
and prod_price > 2  
## in 一般 比 一组 or 要快,in 可包含其他 select 语句（in 最大优点）
通配符  
##方括号 [] 匹配单个字符
select prod_name from product where prod_name like '[JM]%'  
select prod_name from product where prod_name like '[^JM]%' 
## 别名 保留字 as 是可选的，但最好加上，别名最好是一个单词
数据处理函数  
##用函数的问题：兼容性 
## distinct 不可用于 count(*)
分组  
##列里面包含 Null 那 Null 行会作为一组
##group by 子句的列必须是检索列，有效的表达式，不可是聚集函数
##where 和 having 区别：where:行级过滤， 在分组前进行过滤，having 在分组后过滤
##group by 一般和 order by 一起用，实现数据排序