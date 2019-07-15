package com.luo.core;

import com.luo.lang.Nullable;
import com.luo.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * helper class for the method parameters
 */
public class MethodParameter {
    //attr
    private static final Annotation[] empty_annotation_array = new Annotation[0];


    private final Executable executable;//from java.reflect

    private final int parameterIndex;

    //volatile 修饰的变量 不需要立即初始化
    @Nullable
    private volatile Parameter parameter;

    private int nestingLevel = 1;

    @Nullable
    private Map<Integer, Integer> typeIndexesPerLevel;

    @Nullable
    private volatile Class<?> containingClass;
    @Nullable
    private volatile Class<?> parameterType;
    @Nullable
    private volatile Type genericParameterType;
    //参数上的注解数组
    @Nullable
    private volatile Annotation[] parameterAnnotations;
    @Nullable
    private volatile ParameterNameDiscover parameterNameDiscover;

    //参数的名称
    @Nullable
    private volatile String parameterName;

    @Nullable
    private volatile MethodParameter nestedMethodParameter;

    //构造方法

    public MethodParameter(MethodParameter original) {
        Assert.notNull(original, "Original must not be null");
        this.executable = original.executable;

        this.parameterIndex = original.parameterIndex;
        this.parameter = original.parameter;
        this.containingClass = original.containingClass;
        this.parameterType = original.parameterType;
        this.genericParameterType = original.genericParameterType;
        this.nestingLevel = original.nestingLevel;
        this.parameterAnnotations = original.parameterAnnotations;
        this.typeIndexesPerLevel = original.typeIndexesPerLevel;
        this.parameterName = original.parameterName;

    }

}
