##为什么用 nginx
io 复用：提高并发+吞吐  
非阻塞，可处理2-3万的并发连接数  
内存消耗小：10个 nginx 占150M  
宕机概率小  
健康检查：再次的请求不会发给宕机的服务器  
master/worker 结构：接受用户请求是异步的，它先把所有请求全部接受下来，然后发送给 web 服务器，一边接收后端的返回数据，一边把这些数据发送给浏览器端  
异步非阻塞：epoll模型：一个队列，排队解决
##nginx 如何处理一个请求
首先，nginx启动，读取配置文件，得到需要监听的端口和 ip，初始化 socket，再进行监听；fork出多个子进程，子进程会竞争 接受 新的连接  
客户端与 nginx 3次握手，建立连接后，子进程接受 连接成功后，然后
创建 nginx 对 http 连接的封装:ngx_connection_t ,根据事件调用对应  
的事件处理模块:http 模块与客户端进行数据交换  
##动静资源分离的好处
静态资源(js,css,html,jpg)放入 nginx,动态资源(jsp)放入 tomcat  
减少对后端服务器的请求，提高对资源的响应速度,可对静态资源做缓存
##负载均衡的算法
轮训(默认)  
权重轮训  
ip_hash  
服务器响应时间  
url_hash
##服务器配置
```
 server {
listen80; # 端口号
location / {
    root /usr/share/nginx/html; # 静态文件路径
    proxy_pass http://192.168.20.1:8080; # 反向代理，应用服务器HTTP地址
}
```

```
# 负载均衡配置
upstream myapp {

server192.168.20.1:8080; # 应用服务器1

server192.168.20.2:8080; # 应用服务器2
}
  server {
listen80;

location / {

    proxy_pass http://myapp;

}
}

```

```
server {

listen80default_server;

server_name _;

return444; # 过滤其他域名的请求，返回444状态码
}
    server {

listen80;

server_name www.aaa.com; # www.aaa.com域名

location / {

    proxy_pass http://localhost:8080; # 对应端口号8080

}

}

server {
# 虚拟主机配置：多个域名指向同一个主机
server {
 
 listen80default_server;
 
 server_name _;
 
 return444; # 过滤其他域名的请求，返回444状态码
 }
     server {
 
 listen80;
 
 server_name www.aaa.com; # www.aaa.com域名
 
 location / {
 
     proxy_pass http://localhost:8080; # 对应端口号8080
 
 }
 
 }
 
 server {
 
 listen80;
 
 server_name www.bbb.com; # www.bbb.com域名
 
 location / {
 
     proxy_pass http://localhost:8081; # 对应端口号8081
 
 }
 }

```
##/etc/logrotate.d/nginx
nginx 日志的切割，如按天来切割
##nginx -V 
--with 等这些是启动的配置参数。  
nginx -V 2>&1 | sed 's/ /\n/g' 格式化输出  

修改配置之后：  
systemctl restart nginx.service  
systemctl reload nginx.service  
重定向：>/dev/null
##模块
http_stub_status_module:客户端状态  
http_random_index_module:随机主页  
http_sub_module:http内容替换  
http_access_module:基于ip访问控制
http_auth_basic_module:基于用户授权访问控制