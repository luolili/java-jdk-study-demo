package com.luo.core.convert;

import com.luo.core.MethodParameter;
import com.luo.lang.Nullable;
import com.luo.util.ConcurrentReferenceHashMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 这里没有以来java.bean的PropertyDescriptor因为在诸如安卓，java ME这样的环境下
 * 他是不可用的， 所以在这里写一个自己的属性类
 */
public final class Property {


    private static Map<Property, Annotation[]> annotationCache = new ConcurrentReferenceHashMap<>();
    private final Class<?> objectType;

    @Nullable
    private final Method readMethod;

    @Nullable
    private final Method writeMethod;

    private final String name;
    private final MethodParameter methodParameter;

    @Nullable
    private Annotation[] annotations;

    public Property(Class<?> objectType, @Nullable Method readMethod, @Nullable Method writeMethod, @Nullable String name) {
        this.objectType = objectType;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        this.name = name;
    }

    public Method getReadMethod() {
        return this.readMethod;
    }

    public Method getWriteMethod() {
        return this.writeMethod;
    }

    private MethodParameter resolveParameterType() {

    }

    public MethodParameter resolveReadMethodParameter() {
        if (getReadMethod() == null) {
            return null;
        }


    }


}
