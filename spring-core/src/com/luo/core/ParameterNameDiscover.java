package com.luo.core;

import com.luo.lang.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 从方法对象或者构造方法上获取到方法的参数名称的数组
 * 用接口来寻找参数名称
 * 注意：寻找参数的名称不总是可能的， 但是还是有很多策略是可尝试的，
 * 如：查看调试的信息，因为这些信息可能在编译的时候被发表出来；
 *当方法上有AspectJ的注释，可以看参数名的注解值
 */
public interface ParameterNameDiscover {
    @Nullable
    String[] getParameterNames(Method method);

    @Nullable
    String[] getParameterNames(Constructor<?> ctor);
}
