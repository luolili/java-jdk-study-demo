1.查看 包级别的uml,右击包

---
1.pattern and memory analysis

+ public;

- private

'#' protected

下划线：static

斜体：抽象方法
# 原则
1.开闭原则：对类，模块，方法的扩展开放，修改关闭。

> 用抽象构建框架，用实现扩展细节

可复用。面向抽象编程。

2.依赖倒置原则：高层模块不依赖低层模块，都要依赖其抽象

代码稳定性；可读性。

3. 单一职责：只有一个导致 类变更的 原因。

体现在 class/interface/method

降低类的复杂度，可读性，可维护性

4.接口隔离原则：多个专门的接口，客户端不应该
依赖他不需要的接口，适度

5.迪米特 原则：最少知道；一个对象应该对其他
对象保持最少的了解。适度。

---
1. 工厂方法：创建 对象 需要大量重复代码；
客户端不需要知道其实现细节。
```
Collection iterator()
```

```
URLStreamHandlerFactory
createURLStreamHandler()
```

2.抽象工厂：关注产品group
```
SqlSessionFactory
```

3.构建者模式：构建：多个step 与表现分离
StringBuilder,ImmutableSet,CacheBuilder,
BeanDefinitionBuilder

4.singleton:

Runtime,ErrorContext:mybatis

5. 原型模式：类初始化消耗资源多；在循环体产生
很多对象；构造方法复杂。ArrayList,HashMap

6.外观模式：jdbcUtils

7.decorator:BufferedReader

8.adapter:设计时不考虑。XmlAdapter,AdvisorAdapter,

9.cache/pool:享元。内/外部状态。

10.组合：把对象组合为树形结构。Map:putAll

11.桥接：抽象部分和细节分开，独立的变化，+组合。
jdbc.Driver:mysql driver, other driver,DriverManager

12.代理：目标对象：真实被调用。ProxyFactoryBean:getObject()
mybatis:MapperProxyFactory

13.template：hook

14.iterator:不必暴露集合的内部表示。

15.strategy:处理if else.

16.解释器：某类问题，经常发生，如日志

17.观察者：关联行为，触发机制。

18.备忘录：保存对象的状态：属性，为了恢复对象。

19.命令：把不同的请求 封装为不同的对象。
Runnable:Command,

20:中介：封装一组对象，解决复杂的引用关系，
交互的公共行为。聊天室。Timer:中介，TimeTask:User

21:责任链：一个请求的处理
需要一个或者多个对象协调处理。Filter,FilterChain

22.访问者：封装 操作。针对 数据结构。

23.状态：状态 与 行为。有多个状态，可互相转换。