##eureka 常见问题？

1.已经 stop 的服务注销有延迟？

方法：
关闭自我保护模式：eureka.server.enable-self-preservation=false

设置注销时间间隔：eureka.server.eviction-interval-timer-in-ms=1000

2.unknown问题：配置服务名

##string 占几个字节
6个字节：win系统是gbk，若是utf-8:7