Hibernate将先查询CUSTOMERS表中所有的记录，然后根据每条记录的ID，到ORDERS表中查询有参照关系的记录  
select 语句 太多，需要频繁访问数据库，没有利用SQL的连接查询功能  
在应用逻辑只需要访问Customer对象，而不需要访问Order对象的场合，加载Order对象完全是多余的操作，这些多余的Order对象白白浪费了许多内存空间  
Hibernate映射文件中fetch属性：  
select,join  
>select方式时先查询返回要查询的主体对象（列表），再根据关联外键id，每一个对象发一个select查询，获取关联的对象，形成n+1次查询
如果lazy=true（延迟加载），select在查询时只会查出主表记录，用到了关联数据时再自动在执行查询  
而join方式，主体对象和关联对象用一句外键关联的sql同时查询出来，不会形成多次查询