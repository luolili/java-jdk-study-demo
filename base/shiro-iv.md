1.shiro 的组件？

身份认证/登陆；权限验证/授权；会话管理（session）；加密（密码加密保存）；web支持（可以与web集成）；缓存（用户信息）

2.核心组件？

Subject:当前用户操作；

SecurityManager:管理所有的Subject;

Realms:权限验证

3.流程？
认证过程：

客户端请求--filter 拦截请求--调用realm的认证方法--根据用户名获取他的所有权限--有往下继续执行，没有，到错误页面

创建 SecurityManager--主体提交认证--SecurityManager 认证--Authenticator 认证-- realm 验证