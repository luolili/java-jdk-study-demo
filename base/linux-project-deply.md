##如何用docker部署 springboot项目
1.安装 docker:yum install docker

检查docker版本:docker --version  
启动docker:systemctl start docker  
设置docker镜像源：etc/docker/deamon.json  
```

```

重启docker:systemctl restart docker

2.redis:docker pull redis  
docker images   
docker run --name myredis 6379:6379 -d redis-server --appendonly yes  
3.mysql:docker pull mysql:5.7.27  
docker run --name mymysql -e mysql_root_password=admin -d -p 3306:3306