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
包含URL:定位符：locator + 统一资源名称：uniform resource name
2. status code:

 1XX: informatioal :信息性状态码，接受的请求正在处理
 
 2XX： Succeeded，请求ok,处理完毕
 
 204: no conntent: 只需要客户端向服务端发送信息，
 不需要服务端返回数据
 206：partial content: 客户端进行了范围请求，
 响应报文包含的只是Content-Range所指定的范围
 的实体内容
 
 3XX： redirection, 需要进行附加操作来完成请求
 301: moved permanently:永久性redirect
 302:Found:临时性redirect，使用Get
 303: See other,要求客户端必须使用Get请求获得资源
 
 304: not modified: 请求报文里面包含
 如：if-Match, if-Modified-Since,若不满足这些条件，
 会有304
 
 4XX：client err,客户端错误，服务器无法处理请求
 400：bad request: 请求里面存在语法错误
 401：Unauthorized:发送的请求需要有认证信息
 
 5XX: server err