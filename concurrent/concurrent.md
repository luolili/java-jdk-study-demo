1.syn 的原理？

进入和退出管程（monitor）,同步代码块：用的是syn 的 monitorenter+monitorexit 指令；同步方法：读取方法的acc_synchronized 标签 隐士实现.

实例对象的组成：对象头+实例var+填充数据；

填充数据是为了字节对齐；syn锁是在对象头里面；

对象头组成：mark word+class metadata address;
mark word:锁信息，对象hashcode:25bit,对象分代的年龄.
ObjectMonitor:每个等待的线程会进入一个队列：EntryList,调用wait的时候进入 waitSet，锁的owner释放锁，count-1,获得锁的线程：count+1

2.如何确定对象的锁？

Thread 方法
```
 public static native boolean holdsLock(Object obj);
```

3.java 对原生锁做了哪些优化？

偏向锁+轻量级锁+自旋锁+自适应自旋锁+锁消除+锁粗化

自旋锁：共享数据的锁定状态只会持续很短的时间，不挂起线程，而是让等待的线程不断去尝试获取锁，默认是10次。
会占用cpu，但不会处理任务

自适应自旋锁：根据过去的经验，计算出合理的自旋等待时间。

> 最佳实践：减少锁的持有时间
``` 
public Matcher matcher(CharSequence input) {
           if (!compiled) {
               synchronized(this) {
                   if (!compiled)
                       compile();
               }
           }
           Matcher m = new Matcher(this, input);
           return m;
       }
`
```
> 最佳实践：缩小锁的范围：ConcurrentHashMap

>最佳实践：在读多写少的情况下，用ReentrantReadWriteLock代替独占锁

锁分离思想：LinkedBlockingQueue:take方法和put方法分别作用于链表的前端和尾巴,take和put之间不存在竞争，只有take与take,put 与put才会有竞争。
```
  /** Lock held by take, poll, etc */
    private final ReentrantLock takeLock = new ReentrantLock();
     /** Lock held by put, offer, etc */
        private final ReentrantLock putLock = new ReentrantLock();

```

锁粗化：减少对锁的请求和同步次数，特别是在循环
>轻量级锁是在无竞争的情况下使用CAS操作来代替互斥量的使用，从而实现同步；而偏向锁是在无竞争的情况下完全取消同步。

3.java 锁机制？

jvm 的 syn+jdk 的 lock 接口