1. 反射中，Class.forName 和 ClassLoader 区别:
前者会不仅会加载.class文件到jvm里面，还会对类进行解释， 执行类里面的static块； 后者只是加载.class文件，
只有在调用了newInstance方法之后，才会去执行static块

2. 不实现个性化的equals和hashCode方法的坏处：
不重写hashCode会降低map等集合的索引速度； equals相等的2个对象， 它们的hashCode也是一样的。

#clean code
1. 如果方法参数多于3个，用类来封装
2. 用最简单的方式解决

#http
1. URI: 统一资源标识符：uniform resource identifier
包含URL:定位符：locator + 