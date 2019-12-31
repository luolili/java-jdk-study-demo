1. @SpringBootApplication : 
包含了
- @SpringBootConfiguration
- @EnableAutoConfiguration
  - 包含@Configuration
- @ComponentScan 代替配置文件的component-scan


2. @ConfigurationProperties(prefix="com") 配置属性的引入 配合@Component使用

3. @EnableConfigurationProperties(MyConfig.class) 引入MyConfig类里面包含的字段属性

4. @Bean 代替xml里面的bean配置

5. @ConditionalOnMissingBean： 当classpath没有注解指定的类的时候，这配置生效。@ConditionalOnMissingClass,
@ConditionalOnMissingProperty

6. @EnableJpaRepositories(basePackages= {"com.luo"}: 扫描和发现指定包及其子包中的Repository定义

