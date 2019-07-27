package com.luo.core;

import com.luo.lang.Nullable;
import com.luo.util.Assert;
import com.luo.util.ConcurrentReferenceHashMap;

import java.lang.reflect.*;
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

    public static Class<?> resolveTypeArgument(Class<?> clazz, Class<?> genericIfc) {
        //由clazz 和genericIfc 获取ResolvableType
        ResolvableType resolvableType = ResolvableType.forClass(clazz).as(genericIfc);
        if (!resolvableType.hasGenerics()) {
            return null;
        }
        return getSingleGeneric(resolvableType);

    }

    public static Class<?>[] resolveTypeArguments(Class<?> clazz, Class<?> genericIfc) {
        //由clazz 和genericIfc 获取ResolvableType
        ResolvableType resolvableType = ResolvableType.forClass(clazz).as(genericIfc);
        if (!resolvableType.hasGenerics() || resolvableType.isEntirelyUnresolvable()) {
            return null;
        }
        return resolvableType.resolveGenerics(Object.class);

    }

    public static ResolvableType resolveVarivale(TypeVariable<?> typeVariable, ResolvableType contextType) {
        ResolvableType resolvedType;
        if (contextType.hasGenerics()) {
            //通过 Type 和ResolvableType 获得ResolvableType
            resolvedType = ResolvableType.forType(typeVariable, contextType);
            if (resolvedType.resolve() != null) {
                return resolvedType;
            }
        }

        //没有generics

        ResolvableType superType = contextType.getSuperType();
        if (superType != ResolvableType.NONE) {
            resolvedType = resolveVarivale(typeVariable, superType);
            if (resolvedType.resolve() != null) {
                return resolvedType;
            }
        }
        //contextType.getInterfaces() 始终不是null
        for (ResolvableType ifc : contextType.getInterfaces()) {
            resolvedType = resolveVarivale(typeVariable, ifc);
            if (resolvedType.resolve() != null) {
                return resolvedType;
            }

        }
        return ResolvableType.NONE;

    }

    public static Type resolveType(Type genericType, @Nullable Class<?> contextClass) {

        if (contextClass != null) {
            if (genericType instanceof TypeVariable) {
                ResolvableType resolvableTypeVariable =
                        resolveVarivale((TypeVariable<?>) genericType, ResolvableType.forClass(contextClass));
                if (resolvableTypeVariable != ResolvableType.NONE) {
                    Class<?> resolved = resolvableTypeVariable.resolve();
                    if (resolved != null) {
                        return resolved;
                    }
                }
            } else if (genericType instanceof ParameterizedType) {
                //genericType 不是TypeVariable, 是ParameterizedType
                ResolvableType resolvedType = ResolvableType.forType(genericType);
                if (resolvedType.hasUnresolvableGenerics()) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;

                    Class<?>[] generics = new Class<?>[parameterizedType.getActualTypeArguments().length];
                    Type[] typeArguments = parameterizedType.getActualTypeArguments();
                    //循环类型参数

                    for (int i = 0; i < typeArguments.length; i++) {
                        Type typeArgument = typeArguments[i];
                        if (typeArgument instanceof TypeVariable) {
                            ResolvableType resolvedTypeArgument =
                                    resolveVarivale((TypeVariable<?>) typeArgument, ResolvableType.forClass(contextClass));
                            if (resolvedTypeArgument != ResolvableType.NONE) {
                                generics[i] = resolvedTypeArgument.resolve();
                            } else {
                                generics[i] = ResolvableType.forType(typeArgument).resolve();
                            }

                        } else {//typeArgument 不是TypeVariable
                            generics[i] = ResolvableType.forType(typeArgument).resolve();
                        }


                    }

                    Class<?> rawClass = resolvedType.getRawClass();
                    if (rawClass != null) {

                    }


                }


            }
        }
    }
}

