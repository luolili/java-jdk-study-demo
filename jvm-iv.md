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