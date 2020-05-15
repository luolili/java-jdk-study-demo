##http 和 https区别
http:通过明文的方式发送内容，攻击人可截取web浏览器和网络服务器直接传输的报文，不适合敏感信息的传输：如密码，账号

https:其安全基础是 加了 ssl.

http:端口 是 80；https:端口443；

https握手和连接缓存比较耗时。
##tcp 与 udp 区别
tcp 是基于连接的；udp 是无连接的  
tcp 需要的系统资源更多  
tcp 保证数据正确性和顺序；udp不保证这些  
tcp 是可靠的；udp 是不可靠的
tcp 传输慢； udp 传输快  
场景：tcp:浏览器，文件传输；udp:qq语音/视频