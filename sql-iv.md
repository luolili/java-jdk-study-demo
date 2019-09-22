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
