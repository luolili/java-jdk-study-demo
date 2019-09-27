1. left join使用的注意点：

例子：
classes : 存放所有班级的表：id,name;

student:id,class_id,gender

需求1：找出 每个 班级的名称以及对应的班级的女同学数量？

```
select c.name count(s.name) from classes c
left join student s
on c.id = s.class_id
and s.gender = 'F'
group by c.name 
```
需求2：找出 一班的同学总数？

```
select c.name, count(s.name) from classes
left join student s
on c.id=s.class_id

where c.name='一班'
group by c.name
```

note:如果想对右表进行限制，则一定要在on条件中进行，若在where中进行则可能导致数据缺失;
对左表进行过滤必须用where。


2. jpa查询结果应该是多条记录，若您用User 来接受返回结果的话，
就只能获得其中一条记录。不会报错：
```
User getByUsername(String username);
```

# jpa 的 getOne 和 findById 方法的区别？
- getOne返回一个实体的引用，无结果会抛出异常；EntityNotFoundException, no session
- findById返回一个Optional对象；no value root..

# mybatis接口绑定的方式
- 注解：@Select,@Update
- xml:当sql比较长的时候

# mysql 索引的实现？
- B+Tree: 只在最末端叶子节点存数据，叶子节点是以链表的形式互相指向的
- Myisam引擎（非聚集索引）：他创建一个表，
实际生成了三个文件：user.myi 索引文件；user.myd:数据文件；user.frm:数据结构类型。

当我们执行 ： select * from user where id=2，

他的执行流程：
- 去user.myi 文件查看有没有 以 id 为索引的索引树
- 根据这个 id 索引 找到叶子节点 id值，从而 得到他里面的数据地址：叶子姐弟啊村的是索引+数据地址
- 根据数据地址去 user.myd文件找 对应的数据，返回出来

Innodb(聚集索引)：默认以主键 为索引，所以不需要myi文件。
- 有2个文件：user.ibd 索引文件；user.frm:数据结构类型。
- 叶子节点 存的是 索引+数据

select * from user where name='xc'

- 找到 name 索引树
- 根据 name 的值 找到 树下叶子的 name索引+主键值
- 用找到的主键值，去主键索引树找到这条数据

Myisam 与Innodb区别：
- Myisam 是表级锁，不支持事务；Indb支持事务，支持全文索引（v5.6),行级锁

select * from user where sex='f': 不需要为sex字段建立索引，因为sex只有2个值

---
1.mybatis如何 分页？
- 使用 RowBounds 进行分页，针对ResultSet 结果集进行 内存分页，而非物理分页；
- 可以使用 插件 物理分页，拦截待执行的 sql ,然后重写 sql， 根据dialect ,添加
对应的物理分页参数和语句 