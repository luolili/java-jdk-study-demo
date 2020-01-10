##动态代理需要注意的地方/spring事务不生效的8个地方？
1. 在同一个类里面，方法互相调用，切面不生效

2.try catch :catch 里面没有throw new RuntimeException

3.抛出 普通 Exception

4.在接口上打注解可能不生效，只有用了jdk代理才行

5.spring配置文件放入 DispatcherServlet不生效

6.数据库引擎不支持 事务

7.方法不是public

8.spring 和springmvc配置文件都扫描了service层

