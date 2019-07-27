package com.luo.core;

import com.luo.util.Assert;
import com.luo.util.ConcurrentReferenceHashMap;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Map;

/**
 * 解析通用类型的帮助类
 */
public final class GenericTypeResolver {

    @SuppressWarnings("rawtypes")
    private static final Map<Class<?>, Map<TypeVariable, Type>> typeVariableCache = new ConcurrentReferenceHashMap<>();

    private GenericTypeResolver() {

    }

    public static Class<?> resolveParameterType(MethodParameter methodParameter, Class<?> implementationClass) {
        Assert.notNull(methodParameter, "MethodParameter must not be null");
        Assert.notNull(implementationClass, "Class must not be null");
        methodParameter.setContainingClass(implementationClass);
        ResolvableType.resolveMethodParameter(methodParameter);
        return methodParameter.getParameterType();

    }

    public static Class<?> resolveReturnType(Method method, Class<?> clazz) {
        Assert.notNull(method, "Method must be not null");
        Assert.notNull(clazz, "Class must be not null");
        return ResolvableType.forMethodReturnType(method, clazz).resolve(method.getReturnType());
    }

    public static Class<?> getSingleGeneric(ResolvableType resolvableType) {
        Assert.isTrue(resolvableType.getGenerics().length == 1, "" +
                "Expected 1 type argument on generic inteface [" + resolvableType +
                "] but found " + resolvableType.getGenerics().length);

        return resolvableType.getGeneric().resolve();
    }

    public static Class<?> resolveReturnTypeArgument(Method method, Class<?> genericIfc) {
        Assert.notNull(method, "Method must be not null");
        ResolvableType resolvableType = ResolvableType.forMethodReturnType(method).as(genericIfc);

        if (!resolvableType.hasGenerics() || resolvableType.getType() instanceof WildcardType) {
            return null;
        }
        return getSingleGeneric(resolvableType);
    }
}
