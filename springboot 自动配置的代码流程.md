#sb 是如何进行自动配置的？

@SpringBootApplication 包含 注解 @EnableAutoConfiguration，他只能写在类上面，可以执行一些配置类，吧她们排除。

@AutoConfigurationPackage ：自动导入包的配置。
```
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(AutoConfigurationPackages.Registrar.class)
public @interface AutoConfigurationPackage {

}
```
看 AutoConfigurationPackages.Registrar 他是 AutoConfigurationPackages 静态内部类：
```
static class Registrar implements ImportBeanDefinitionRegistrar, DeterminableImports {

		@Override
		public void registerBeanDefinitions(AnnotationMetadata metadata,
				BeanDefinitionRegistry registry) {
        //在这里打断点，用idea看 new PackageImport(metadata).getPackageName() 值
        // 他获取的是 XXApplication所在的包名
			register(registry, new PackageImport(metadata).getPackageName());
		}

		@Override
		public Set<Object> determineImports(AnnotationMetadata metadata) {
			return Collections.singleton(new PackageImport(metadata));
		}

	}
```

核心类：AutoConfigurationImportSelector

核心方法：selectImports
```
//实现了 ImportSelector 接口
@Override
	public String[] selectImports(AnnotationMetadata annotationMetadata) {
		if (!isEnabled(annotationMetadata)) {
			return NO_IMPORTS;
		}
    //加载 spring-autoconfigure-metadata.properties
		AutoConfigurationMetadata autoConfigurationMetadata = AutoConfigurationMetadataLoader
				.loadMetadata(this.beanClassLoader);
        // 加载 EnableAutoConfiguration 注解配置的exclude,excludeName
		AnnotationAttributes attributes = getAttributes(annotationMetadata);
    // 获取spring.factories 里面的配置类
		List<String> configurations = getCandidateConfigurations(annotationMetadata,
				attributes);
        //排除 不同的jar/项目里面的spring.factories 里面配置的相同的配置类
		configurations = removeDuplicates(configurations);
		Set<String> exclusions = getExclusions(annotationMetadata, attributes);
		checkExcludedClasses(configurations, exclusions);
		configurations.removeAll(exclusions);
    //过滤 spring-autoconfigure-metadata.properties
		configurations = filter(configurations, autoConfigurationMetadata);
		fireAutoConfigurationImportEvents(configurations, exclusions);
		return StringUtils.toStringArray(configurations);
	}

```

