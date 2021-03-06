package com.luo.core;

import com.luo.lang.Nullable;
import com.luo.util.Assert;
import com.luo.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * helper class for the method parameters
 */
public class MethodParameter {
    //attr
    private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];


    private final Executable executable;//from java.reflect

    private final int parameterIndex;

    //volatile 修饰的变量 不需要立即初始化
    @Nullable
    private volatile Parameter parameter;

    private int nestingLevel = 1;

    @Nullable
    Map<Integer, Integer> typeIndexesPerLevel;

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

        //参数名，参数对象，参数的索引
        this.parameterIndex = original.parameterIndex;
        this.parameter = original.parameter;
        this.parameterName = original.parameterName;

        //参数类型，通用参数类型，
        this.parameterType = original.parameterType;
        this.genericParameterType = original.genericParameterType;
        //参数上的注解
        this.parameterAnnotations = original.parameterAnnotations;

        this.containingClass = original.containingClass;
        this.nestingLevel = original.nestingLevel;
        this.typeIndexesPerLevel = original.typeIndexesPerLevel;

        this.parameterNameDiscover = original.parameterNameDiscover;

    }

    //-----参数是方法对象  exectable代表了方法对象或构造对象
    public MethodParameter(Method method, int parameterIndex, int nestingLevel) {
        Assert.notNull(method, "Method must not be null");
        this.executable = method;
        this.parameterIndex = validateIndex(method, parameterIndex);
        this.nestingLevel = nestingLevel;
    }

    //默认参数的内嵌等级为1
    public MethodParameter(Method method, int parameterIndex) {
        this(method, parameterIndex, 1);
    }
    //----参数是构造对象

    /**
     * @param ctor           constructor to specify a parameter for
     * @param parameterIndex
     * @param nestingLevel   a List of Lists: its nesting level is 1
     */
    public MethodParameter(Constructor<?> ctor, int parameterIndex, int nestingLevel) {
        Assert.notNull(ctor, "Constructor must not be null");
        this.executable = ctor;
        this.parameterIndex = validateIndex(ctor, parameterIndex);
        this.nestingLevel = nestingLevel;
    }

    //默认参数的内嵌等级为1
    public MethodParameter(Constructor<?> ctor, int parameterIndex) {
        this(ctor, parameterIndex, 1);
    }

    //对参数索引的范围检查
    private static int validateIndex(Executable executable, int parameterIndex) {
        //-1 获取参数的个数
        int count = executable.getParameterCount();
        Assert.isTrue((parameterIndex >= -1 && parameterIndex < count),
                () -> "Parameter index needs to be between -1 and  " + (count - 1));
        return parameterIndex;

    }

    //通过exectable来获取方法对象
    @Nullable
    public Method getMethod() {
        return (this.executable instanceof Method ? (Method) this.executable : null);
    }

    @Nullable
    public Constructor<?> getConstructor() {
        return (this.executable instanceof Constructor ? (Constructor<?>) this.executable : null);
    }

    public Class<?> getDeclaringClass() {
        return this.executable.getDeclaringClass();
    }

    //Member is to identify info of a field , method ,ctor
    public Member getMember() {
        return this.executable;
    }


    //暴露在方法或者构造方法上的注解，而非参数上的注解
    public AnnotatedElement getAnnotatedElement() {
        return this.executable;
    }

    public Executable getExecutable() {
        return this.executable;
    }

    //------

    //根据参数的索引来获取参数对象：这个方法是属于类的，而不是类的内部类的方法，索引这里参数索引是从0开始的
    public Parameter getParameter() {
        //-1 检查索引的范围
        if (this.parameterIndex < 0) {
            throw new IllegalStateException("cannot retrieve the parameter descriptor for method return type");
        }
        //-2 获取参数对象, 目的是检查该参数对象是否为空
        Parameter parameter = this.parameter;
        if (parameter == null) {
            //从executable中获取参数的数组对象
            parameter = this.executable.getParameters()[parameterIndex];
            this.parameter = parameter;
        }

        return parameter;
    }

    public int getParameterIndex() {
        return this.parameterIndex;
    }

    public int getNestingLevel() {
        return this.nestingLevel;
    }

    //增加内嵌水平
    public int increaseNestingLevel() {
        return this.nestingLevel++;
    }

    //延迟构建,map用来维护内嵌水平与type index的关系
    public Map<Integer, Integer> getTypeIndexesPerLevel() {
        if (this.typeIndexesPerLevel == null) {
            this.typeIndexesPerLevel = new HashMap<>(4);
        }
        return this.typeIndexesPerLevel;
    }

    //设置当前内嵌水平所对应的type index
    public Integer setTypeIndexForCurrentLevel(int typeIndex) {
        return getTypeIndexesPerLevel().put(this.nestingLevel, typeIndex);
    }

    //获取当前内嵌水平所对应的type index
    public Integer getTypeIndexForCurrentLevel() {
        return getTypeIndexesPerLevel().get(this.nestingLevel);
    }

    //获取指定的内嵌水平所对应的type index
    public Integer getTypeIndexForLevel(int nestingLevel) {
        return getTypeIndexesPerLevel().get(nestingLevel);
    }

    public void decreaseNestingLevel() {
        getTypeIndexesPerLevel().remove(this.nestingLevel);
        this.nestingLevel--;
    }

    //copy
    @Override
    protected MethodParameter clone() {
        return new MethodParameter(this);
    }

    public MethodParameter nested() {
        MethodParameter nestedParam = this.nestedMethodParameter;

        if (nestedParam != null) {
            return nestedParam;
        }

        nestedParam = clone();

        nestedParam.nestingLevel = this.nestingLevel + 1;
        this.nestedMethodParameter = nestedParam;
        return nestedParam;
    }

    public Class<?> getParameterType() {
        Class<?> paramType = this.parameterType;
        if (paramType == null) {
            if (this.parameterIndex < 0) {
                Method method = getMethod();
                //用方法的返回类型作为参数类型，以返回Class类型
                paramType = (method != null ? method.getReturnType() : void.class);
            } else {
                paramType = this.executable.getParameterTypes()[parameterIndex];
            }
            this.parameterType = paramType;//对类的parameterType重新赋值
        }
        return paramType;
    }

    //模板方法：返回的是后置处理的array，这是他的默认实现
    protected Annotation[] adaptAnnotationArray(Annotation[] annotations) {
        return annotations;
    }

    public Annotation[] getParameterAnnotations() {
        Annotation[] paramAnns = this.parameterAnnotations;
        if (paramAnns == null) {
            Annotation[][] annotationArray = this.executable.getParameterAnnotations();
            int index = this.parameterIndex;
            //是构造方法的实例对象，是内部类.annotationArray.length是参数的个数
            //对于内部类，他的参数索引是从-1 开始的
            if (this.executable instanceof Constructor &&
                    ClassUtils.isInnerClass(this.executable.getDeclaringClass()) &&
                    annotationArray.length == this.executable.getParameterCount() - 1) {
                index = this.parameterIndex - 1;

            }
            paramAnns = (index >= 0 && index < annotationArray.length ?
                    adaptAnnotationArray(annotationArray[index]) : EMPTY_ANNOTATION_ARRAY);
            this.parameterAnnotations = paramAnns;

        }
        return paramAnns;
    }


    public Type getGenericParameterType() {
        //-1 先从本类中的属性获取通用type
        Type paramType = this.genericParameterType;

        //如果获取的type为null
        if (paramType == null) {
            if (this.parameterIndex < 0) {
                Method method = getMethod();
                paramType = (method != null ? method.getGenericReturnType() : void.class);
            } else {
                //从executable获取参数类型的数组
                Type[] genericParameterTypes = this.executable.getGenericParameterTypes();
                //参数是内部类的情况
                int index = this.parameterIndex;
                if (this.executable instanceof Constructor &&
                        ClassUtils.isInnerClass(this.executable.getDeclaringClass()) &&
                        genericParameterTypes.length == this.executable.getParameterCount() - 1) {
                    index = this.parameterIndex - 1;
                }
                //参数索引不在范围之内，返回结果调用的是 getParameterType()
                paramType = (index >= 0 && index < genericParameterTypes.length ?
                        genericParameterTypes[index] : getParameterType());

            }
            this.genericParameterType = paramType;
        }
        return paramType;
    }
    //Nullable的类型可能是javax.annotation.Nullable 或findbug.Nullable
    private boolean hasNullableAnnotation() {
        for (Annotation ann : getParameterAnnotations()) {
            if ("Nullable".equals(ann.annotationType().getSimpleName())) {
                return true;
            }

        }
        return false;
    }

    //当参数的类型是java8的Optional的时候返回内嵌的方法参数，否则返回自身
    public MethodParameter nestedIfOptional() {
        return (getParameterType() == Optional.class ? nested() : this);
    }

    public Class<?> getContainingClass() {
        Class<?> containingClass = this.containingClass;
        return (containingClass != null ? containingClass : getDeclaringClass());
    }

    public void setContainingClass(Class<?> containingClass) {
        this.containingClass = containingClass;
    }

    public void setParameterType(@Nullable Class<?> parameterType) {
        this.parameterType = parameterType;
    }
}
