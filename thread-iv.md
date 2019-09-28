# 线程的创建方法？
- 继承 Thread
- 实现 Runnable: 减少程序之间的耦合度，面向接口 编程 是 设计模式 6个原则之一

# Runnable 和 Callable 接口 区别
- Runnable 只是执行 run方法里面的代码，Callable 有泛型的返回值，获取异步执行的结果，目的是获取多线程 运行的结果，
应对多线程未知性。

# CycllicBarria 和 CountdownLatch
- 前者的某个 thread 运行到某个点上之后，这个 thread 停止运行， 直到 其他 thread 运行到这个点，所有 thread 才重新运行；只可唤起一个任务；可重用
- 后者只是给某个数值-1，该 thread 继续运行；唤起所有任务；不可重用

# 为什么 wait/notify/notifyAll方法要在同步块里面被调用？
- 她们在调用钱 必须获得对象的锁

# 什么是 jmm?
- 是一种 多线程 访问 Java 内存的规范，
- 主内存+工作内存，类的状态，也就是类的共享var, 是存储在主内存的，每个线程 在访问这些var的时候， 会读一次 主内存的var ，并且把
她们拷贝到 工作内存里面。 每个线程操作的是自己工作内存的var, 在每个线程执行完了之后， 把最新的数据更新到 主内存。
- 有几个原子操作
- volatile 的使用规则
- happens-before:操作a 必然先行在b之前

# syn 与 volatile 区别？
- syn用于 方法和代码块；vol 用于 var
- 用syn会有阻塞，vol 没有阻塞
- syn有可见性 和 原子性，vol：可见性
- syn性能比vol差
