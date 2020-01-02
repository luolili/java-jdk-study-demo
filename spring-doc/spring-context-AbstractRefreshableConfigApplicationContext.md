# AbstractRefreshableConfigApplicationContext 基于xml和file的配置位置的抽象类

主要属性：
- private String[] configLocations; 

主要方法：
1。
```

	public void setConfigLocation(String location) {
		setConfigLocations(StringUtils.tokenizeToStringArray(location, CONFIG_LOCATION_DELIMITERS));
	}
  
  public void setConfigLocations(@Nullable String... locations) {
		if (locations != null) {
			Assert.noNullElements(locations, "Config locations must not be null");
			this.configLocations = new String[locations.length];
			for (int i = 0; i < locations.length; i++) {
				this.configLocations[i] = resolvePath(locations[i]).trim();
			}
		}
		else {
			this.configLocations = null;
		}
	}
```

```
public static void noNullElements(@Nullable Object[] array, String message) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					throw new IllegalArgumentException(message);
				}
			}
		}
	}

```

```
//子类可以重写他，来提供一系列配置的位置。
@Nullable
	protected String[] getConfigLocations() {
		return (this.configLocations != null ? this.configLocations : getDefaultConfigLocations());
	}

```
一个数组里面不能有空的元素。
```
@Override
	public void setBeanName(String name) {
		if (!this.setIdCalled) {
			super.setId(name);
			setDisplayName("ApplicationContext '" + name + "'");
		}
	}
```
通过实现 BeanNameAware 接口来给 上下文设置id， 展示的名字.
```@Override
	public void afterPropertiesSet() {
		if (!isActive()) {//在没有激活的情况下才进行刷新
			refresh();
		}
	}
```

触发上下文刷新。

