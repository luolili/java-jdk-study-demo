1. redis 大 key 和 大 value 问题？

一个简单的 key 存储 一个 大value:  
分成 几个 key-value, 每个字段 是一个 value.  
 >value 设计：string 类型 要小于 10kb,list ,set ,hash,zset里面的元素不超过5000个。
 
 > 非字符串的 key，不要用 del 删除，用 hscan,sscan,zscan 来渐进式删除；防止 bigkey到了过期时间自动删除的情况（会触发 del 操作，造成阻塞）
 
 ##如何排序
 sort 命令：sort day desc (对数字集合排序)  
 sort name alpha(对字符串排序)