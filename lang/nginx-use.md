##为什么用 nginx
io 复用：提高并发+吞吐
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