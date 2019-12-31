1.HashMap 容量为什么是2的次幂？

& 运算快，比%快；
可保证 索引值 都在 cap 里面，不会超出数组长度；
(n-1)&hash:当n 是2的次幂满足：(n-1)&hash=hash%n

2.Comparable 和 Comparator 区别？

Comparable 排序接口，可 use Collections.sort 排序，在增加比较的功能的时候，要修改代码；Comparator 是一个外部比较器，可实现通用的服啊的逻辑，可以新定义一个比较器，实现比较，不需要修改原代码。

3.fail-fast 与 fail-safe 区别？

fail-fast: 集合在 use iter 遍历的时候，如果修改了集合的内容，modCount 会+1，若 modCount 与 expectedModCount 不相等，抛出ConcurrentModificationException

> 它是用来检测 程序的bug，调用 iterator 的remove 方法不会抛出异常

fail-safe:先复制 一份 集合，在 这个集合上遍历。