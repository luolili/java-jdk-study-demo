1.HashMap 容量为什么是2的次幂？

& 运算快，比%快；
可保证 索引值 都在 cap 里面，不会超出数组长度；
(n-1)&hash:当n 是2的次幂满足：(n-1)&hash=hash%n

2.Comparable 和 Comparator 区别？

Comparable 排序接口，可 use Collections.sort 排序，在增加比较的功能的时候，要修改代码；Comparator 是一个外部比较器，可实现通用的复杂的逻辑，可以新定义一个比较器，实现比较，不需要修改原代码。

3.fail-fast 与 fail-safe 区别？

fail-fast: 集合在 use iter 遍历的时候，如果修改了集合的内容，modCount 会+1，若 modCount 与 expectedModCount 不相等，抛出ConcurrentModificationException

> 它是用来检测 程序的bug，调用 iterator 的remove 方法不会抛出异常

fail-safe:先复制 一份 集合，在 这个集合上遍历。

4.CopyOnWriteArrayList 是怎么做到线程安全的？

在add,reomve,set元素的时候，先加锁，是复制一份原来的数组，进行add,remove,set。用于读多写少的情况，如缓存。

5.volitile 原理？

java的8个原子操作：

1.lock,unlock

2.read（主内存，传输到工作内存），load（把read 传来的变量放进工作内存），use（把var从工作内存传到执行引擎）

3.assign（作用于工作内存，执行引擎传来的值给工作内存的var），
store（作用于工作内存，工作内存-->主内存），write（作用于主内存，把工作内存传来的值放入主内存）

volitile的规则：

read,load,use 必须连续出现；assign,store,write 必须连续出现。

做到了：
每次读取前必须先从主内存刷新最新的值；每次写入后必须立即同步回主内存当中

DCL：单利的双重检查，必须用volitile 修饰单利。

因为：
```aidl
Singleton singleton = new Singleton();
```
他不是原子操作：

```aidl
memory = allocate();
initInstance(memory);
instance = memory;
```
他可能是这个顺序执行：
```aidl
memory = allocate();
instance = memory;
initInstance(memory);
```

这是一个没有完成初始化（仅仅指向了一块内存空间）的半个对象。



