#hashmap
1.put 元素的时候，hash(k) & table.length得到元素要被放在第几个数组元素下面

2.hash值一样，equals不一样，把元素放在链表最后面

3.resize:7版本，会对所有元素rehash,放入新的位置；8版本，判断元素原hash值新增的bit位数，0就索引不变，1就索引：原索引+oldTab.length

##hash一致算法

简单的hash算法是对服务器的数量取模；一致性hash算法是对2的32次方取模。

hash(服务器的ip)% 2^32 确定某服务器（节点）在hash环上的位置；