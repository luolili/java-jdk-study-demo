#启动流程
SpringFactoriesLoader 加载所有的SpringApplicationRunListeners:通过调用 starting 方法通知所有的 加载所有的SpringApplicationRunListeners  
创建和配置 Environment:
>1.判断 Environment 是否存在，不存在就创建，是web项目，StandardServletEnvironment  
配置profile ,properties  
调用SpringApplicationRunListener的environmentPrepared()方法，通知事件监听者：应用的Environment已经准备好 

打印banner  
根据是否是web项目，创建 ApplicationContext  
创建一系列 FailureAnalyzer:
>创建流程依然是通过SpringFactoriesLoader获取到所有实现FailureAnalyzer接口的class，然后在创建对应的实例。FailureAnalyzer用于分析故障并提供相关诊断信息  

调用 ApplicationContext refresh 方法  
查找当前context中是否注册有CommandLineRunner（命令行运行器）和ApplicationRunner（应用运行器），如果有则遍历执行它们