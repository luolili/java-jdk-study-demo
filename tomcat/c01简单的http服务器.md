##http
基于请求-响应的协议，使用可靠的tcp协议，tcp默认80端口。  
总是客户端主动发起请求 
###http 请求 
http请求包含请求方法：如 post行：  
POST: /demo/demo.jsp HTTP/1.1  
 
/demo/demo.jsp: uri（相对于服务器根目录的路径）  
HTTP/1.1:请求的协议和版本  

请求头：客户端环境和请求实体内容的描述
###http 响应
协议--状态吗--描述：HTTP/1.1 200 OK  
响应头  
响应实体内容
##Socket 通信的管道
客户端连接服务端的管道