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

