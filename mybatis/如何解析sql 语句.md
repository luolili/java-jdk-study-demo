##执行的流程？

1.SqlSessionFactoryBuilder : 读取配置（Configuration）并且创建 SqlSessionFactory

2.XmlConfigBuilder:解析 configuration 节点，内部用 XmlMapperBuilder 来解析 xml

3.XmlMapperBuilder：内部用 XmlStatementBuilder 解析节点（select等）
每个节点 对应一个 MapperStatement 对象，sqlSource 是他的一个属性，sqlSource 创建最终可执行的 sql 语句
sqlSource 的实现类：

DynamicSqlSource，RawSqlSource，StaticSqlSource，ProviderSqlSource（处理注解Annotation形式的sql）

4.LanguageDriver 是个接口：RawLanguageDriver 处理静态sql,XMLLanguageDriver 处理静态+动态sql