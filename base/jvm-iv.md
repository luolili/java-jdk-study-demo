1.分代GC 是怎么工作的？
- 为什么分代？ 不同的对象有 不同的生命周期，对应不同的收集方式，来提高回收效率
与业务信息相关的对象，如Session,thread,Socket连接，他们生命周期更长；临时var ,如
String对象，生命周期短
- 如何分代？年轻代：Young;年老代：old;持久代：permanent.
持久代：存放类 信息，他与要 收集的 java对象关系不大。

所有新生成的 对象 首先都放在 Young，可以尽快回收 生命周期短的对象。

Young分为三个区：Eden+2个Survivor。大部分对象 在 Eden 中生成，当Eden 满时，还存活
的对象会被 copy 到 其中一个 Survivor，当这个 Survivor区满时，此区存活的 对象 copy 到
另一个 Survivor。当他们都满，最终存活 的对象 会被 copy 到 年老区：Tenured

old:从Young区存活的对象，会到 old区，这些对象生命周期很长。

持久代：存放静态文件，如java类，方法。对于像 hibernate 会动态生成一些 class ,需要
给持久代 分配更大空间。

---
1. 什么时候GC？
- Scavenge GC:当新对象生成，并且Eden 申请空间fail,会发生 Scavenge GC,swap非存活对象，
把存活对象 copy 到 Survivor. Eden区 的空间小，GC频繁，GC算法效率高

- Full GC: 对整个 Heap进行 GC，包括三个分代。比Scavenge GC 慢，对 jvm的调优，多是
对于 Full GC 的调节。年老代 被写满，持久代 被写满，显示 调用 System.gc()

2.String等常量信息是在哪里？
- java6: 方法区；java7：堆

---
1.类加载机制？

bootstrap:加载 jvm 基础核心类库：rt.jar

extension: 加载 java.ext.dirs 系统制定的类库

system: 加载 classpath/java.class.path 下面的类

2.java类加载过程？

加载--验证--准备--解析--解析--初始化--使用--卸载。

3.GC 内存分配？

对象 优先分配到 Eden 区；
大对象直接进入 老年代：-XX:PretenureSizeThreshold=2 设置大对象的最小值；

```
-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=3145728`

```
长期存活的对象进入老年代；
```
-Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1

```
动态年龄判断；
空间分配担保

4.为什么要有GC？

避免内存泄漏和资源浪费；

5.垃圾回收机制？

什么对象需要回收？
引用计数器：每个地方引用他的时候，计数器+1，引用失效时-1.当计数器为0时，可以回收。无法解决循环引用问题。

可达性分析：从GC roots 作为起始点，向下搜索，走过的路径 是 引用链，当一个对象到 GC roots 没有任何引用链时，可回收。

方法区的静态/常量 ,stack上 的对象可作为GC roots

什么时候回收？

cpu 空闲的时候；堆满了的时候；调用System.gc

怎么回收？

mark-sweep:标记清除，会产生很多碎片+效率额低；copying:把可用内存分为2半，每次只用一半，用完之后，把还活着的对象 拷贝到另一半内存；mark-compact:在mark-sweep的基础上，把可回收的对象移到一边上，然后回收掉那一边的对象；分代：新生代：朝生夕死，用copying，发生 Minor GC,老年代：没有额外空间对他进行分配担保：mark-sweep or mark-compact,发生Major GC

6.jvm垃圾回收优点，考略2种回收机制

引用计数+可达性分析。

7.什么是分布式垃圾回收(DGC)？它是如何工作的？

8.jvm什么区会抛出oom？

方法区+运行时常量池+堆。

方法区是所有线程共享的，他存放类信息+常量+静态变量+JIT 编译后的代码。

运行时常量池：属于方法区的一部分。存放：字面量（文本string+final）+符号引用（字段+方法）


堆上出现oom:堆的大小分配不合理。

虚拟机stack:如果线程请求的stack 大于 所分配的stack,出现stackoverflow;若虚拟机stack是可以动态扩容的，可能oom

直接内存不够：oom

堆（新生代+老年代+（元空间）永久代）+Virtual空间：对象优先在Eden，jvm为每个 Thread 分配了一个私有的缓存区（Thread local allocation buffer），目的是避免多线程 同时分配内存需要加锁等机制减慢分配速度。Eden区不够会 触发 minor gc，也叫新生代gc，在minor gc存活下来的对象会被复制 到 Survivor（避免过早进行 full gc和内存碎片，减慢向老年带复制存活对象） 

Virtual空间：Xms （初始值）小于Xmx（最大值）时，差值这部分内存

-XX:NewSize:指定新生代的大小；

-XX:NewRatio:老年代/新生代，默认是2，老年代太大，full gc虽然不那么频繁，但每次需要的时间很长；

-XX:SurvivorRation:Eden/Survivor

##为什么用双亲委派机制
防止内存里面存在多分一样的字节码。保证类的唯一性。