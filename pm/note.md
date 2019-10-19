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