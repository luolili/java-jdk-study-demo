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