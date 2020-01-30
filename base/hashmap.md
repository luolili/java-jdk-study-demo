#hashmap
1.put 元素的时候，hash(k) & table.length得到元素要被放在第几个数组元素下面

2.hash值一样，equals不一样，把元素放在链表最后面

3.resize:7版本，会对所有元素rehash,放入新的位置；8版本，判断元素原hash值新增的bit位数，0就索引不变，1就索引：原索引+oldTab.length

##hash一致算法

简单的hash算法是对服务器的数量取模；一致性hash算法是对2的32次方取模。

hash(服务器的ip)% 2^32 确定某服务器（节点）在hash环上的位置；

##1.为什么用 HashMap
1.他采用了数组+链表的结构，在查询和修改元素方法都很快

2.非同步，很快

3.key 可以是null
##2.HashMap 原理
先对 key 调用 hashCode 方法，根据计算出的 hash确定 map数组的bucket位置，在bucket 里面存 Map.Node，也就是k-v;
如果 hash一样了，那么比较equals,如果一样，就覆盖旧的node,不一样就加入链表。

##3.如何减少 hash 冲突
扰动函数：就是对象的 hashCode 方法，尽量让不同的对象返回不同的 hash，从而减少 map equals 方法的调用
##4.为什么不用二叉树，用红黑树
二叉树在特殊情况下会变为线性结构
##多线程 rehash 有什么问题
hash 冲突变得更频繁。
