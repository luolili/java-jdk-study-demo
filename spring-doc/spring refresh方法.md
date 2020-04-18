13个方法：

##prepareRefresh
设置容器的状态为 active;设置监听器;必要属性是否存在，不存在报错
##obtainFreshBeanFactory
设置容器序列化id，获取 BeanFactory
##prepareBeanFactory
 设置 BeanFactory 属性；添加后置处理器；设置忽略的自动装配接口；注册组件
 ##postProcessBeanFactory
BeanFactory 创建后的 处理
##invokeBeanFactoryPodtProcessors
处理 bean 定义