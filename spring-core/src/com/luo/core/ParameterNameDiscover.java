package com.luo.core;

import com.luo.lang.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 从方法对象或者构造方法上获取到方法的参数名称的数组
 */
public interface ParameterNameDiscover {
    @Nullable
    String[] getParameterNames(Method method);

    @Nullable
    String[] getParameterNames(Constructor<?> ctor);
}
