package com.luo.core;

import com.luo.lang.Nullable;
import com.luo.util.ConcurrentReferenceHashMap;
import com.luo.util.ObjectUtils;
import sun.reflect.generics.factory.CoreReflectionFactory;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.repository.ClassRepository;
import sun.reflect.generics.scope.ClassScope;

import java.io.Serializable;
import java.lang.reflect.*;

/**
 * 封装java反射中的Type
 */
@SuppressWarnings("serial")
public class ResolvableType implements Serializable {

    public static final ResolvableType NONE = new ResolvableType(EmptyType.INSTANCE, null,
            null, 0);
    private static final ResolvableType[] EMPTY_TYPES_ARRAY = new ResolvableType[0];

    private static final ConcurrentReferenceHashMap<ResolvableType, ResolvableType> cache =
            new ConcurrentReferenceHashMap<>(256);

    @Nullable
    private final SerializableTypeWrapper.TypeProvider typeProvider;

    //将要被解析的类型
    private final Type type;

    @Nullable
    private final VariableResolver variableResolver;

    @Nullable
    private final ResolvableType componentType;

    @Nullable
    private final Integer hash;
    @Nullable
    private Class<?> resolved;

    @Nullable
    private volatile ResolvableType superType;

    @Nullable
    private volatile ResolvableType[] interfaces;

    @Nullable
    private volatile ResolvableType[] generics;


    //构造方法:hash有默认的方法计算
    public ResolvableType(Type type, @Nullable SerializableTypeWrapper.TypeProvider typeProvider, @Nullable VariableResolver variableResolver) {
        this.type = type;
        this.typeProvider = typeProvider;
        this.variableResolver = variableResolver;
        this.componentType = null;
        this.hash = calculateHashCode();
        this.resolved = null;

    }

    //自定义hash ，resolved
    public ResolvableType(Type type, @Nullable SerializableTypeWrapper.TypeProvider typeProvider,
                          @Nullable VariableResolver variableResolver, @Nullable Integer hash) {
        this.type = type;
        this.typeProvider = typeProvider;
        this.variableResolver = variableResolver;
        this.componentType = null;
        this.hash = hash;
        this.resolved = null;

    }

    private ResolvableType(Type type, @Nullable SerializableTypeWrapper.TypeProvider typeProvider,
                           @Nullable VariableResolver variableResolver, @Nullable ResolvableType componentType) {

        this.type = type;
        this.typeProvider = typeProvider;
        this.variableResolver = variableResolver;
        this.componentType = componentType;
        this.hash = null;
        this.resolved = resolveClass();
    }

    private Class<?> resolveClass() {
        if (this.type == EmptyType.INSTANCE) {
            return null;
        }

        if (this.type instanceof Class) {
            return (Class<?>) this.type;
        }


        if (this.type instanceof GenericArrayType) {
            Class<?> resolvedComponent = getComponentType().resolve();
            return (resolvedComponent != null ? Array.newInstance(resolvedComponent, 0).getClass() : null);
        }
        return resolveType().resolve();
    }

    public ResolvableType getComponentType() {
        //-1 构造方法
        if (this == NONE) {
            return NONE;
        }
        //-2 字段
        if (this.componentType != null) {
            return this.componentType;
        }
        //-3 解析type
        if (this.type instanceof Class) {
            //先获取componentType, 从Class.通过componentType 构造ResolvableType
            Class<?> componentType = ((Class<?>) this.type).getComponentType();
            return forType(componentType, this.variableResolver);

        }

        if (this.type instanceof GenericArrayType) {
            return forType(((GenericArrayType) this.type).getGenericComponentType(), this.variableResolver);
        }
        return resolveType().getComponentType();
    }

    @Nullable
    public Class<?> resolve() {
        return this.resolved;
    }
    ResolvableType resolveType() {
        //-1 type is ParameterizedType
        if (this.type instanceof ParameterizedType) {
            return forType(((ParameterizedType) this.type).getRawType(), this.variableResolver);
        }
        //-2 type is WildcardType,有upperBounds and lowerBounds
        if (this.type instanceof WildcardType) {
            Type resolved = resolveBounds(((WildcardType) this.type).getUpperBounds());
            if (resolved == null) {
                resolved = resolveBounds(((WildcardType) this.type).getLowerBounds());
            }
            return forType(resolved, this.variableResolver);

        }
        //-3 type is TypeVariable
        if (this.type instanceof TypeVariable) {
            TypeVariable<?> variable = (TypeVariable<?>) this.type;
            //variableResolver用来解析var
            if (this.variableResolver != null) {
                ResolvableType resolved = this.variableResolver.resolveVariable(variable);
                if (resolved != null) {
                    return resolved;
                }

            }
            //variable.getBounds() return type[]
            return forType(resolveBounds(variable.getBounds()), this.variableResolver);
        }
        return NONE;
    }

    //解析type[]
    @Nullable
    private Type resolveBounds(Type[] bounds) {
        if (bounds.length == 0 || bounds[0] == Object.class) {
            return null;
        }
        return bounds[0];
    }

    static ResolvableType forType(@Nullable Type type, @Nullable VariableResolver variableResolver) {
        return forType(type, null, variableResolver);
    }
    static ResolvableType forType(
            @Nullable Type type, @Nullable SerializableTypeWrapper.TypeProvider typeProvider, @Nullable VariableResolver variableResolver) {
        //SerializableTypeWrapper作用
        if (type == null && typeProvider != null) {
            type = SerializableTypeWrapper.forTypeProvider(typeProvider);
        }
        if (type == null) {
            return NONE;
        }
        if (type instanceof Class) {
            return new ResolvableType((Class<?>) type, typeProvider, variableResolver, (ResolvableType) null);
        }

        cache.purgeUnreferenceEntries();
        ResolvableType resultType = new ResolvableType(type, typeProvider, variableResolver);

        ResolvableType cachedType = cache.get(resultType);
        if (cachedType == null) {
            cachedType = new ResolvableType(type, typeProvider, variableResolver, resultType.hash);
            cache.put(cachedType, cachedType);
        }
        resultType.resolved = cachedType.resolved;
        return resultType;
    }

    private int calculateHashCode() {
        int hashcode = ObjectUtils.nullSafeHashCode(this.type);
        //考虑typeProvider获得的type的hashcode
        if (this.typeProvider != null) {
            hashcode = 31 * hashcode + ObjectUtils.nullSafeHashCode(this.typeProvider.getType());
        }

        if (this.variableResolver != null) {
            hashcode = 31 * hashcode + ObjectUtils.nullSafeHashCode(this.variableResolver.getSource());
        }
        //本类的hashcode
        if (this.componentType != null) {
            hashcode = 31 * hashcode + ObjectUtils.nullSafeHashCode(this.componentType);
        }
        return hashcode;


    }


    //把给定的type转为ResolvableType
    public ResolvableType as(Class<?> type) {
        //先检查当前类是不是空类
        if (this == NONE) {
            return NONE;
        }
        Class<?> resolved = resolve();

        if (resolved == null || type == resolved) {
            return this;
        }

    }

    //获取所有被本类实现的接口
    public ResolvableType[] getInterfaces() {
        Class<?> resolved = resolve();
        if (resolved == null) {
            return EMPTY_TYPES_ARRAY;
        }

        ResolvableType[] interfaces = this.interfaces;
        if (interfaces == null) {
            //从resolved里面获取接口
            Type[] genericIfcs = resolved.getGenericInterfaces();
            interfaces = new ResolvableType[genericIfcs.length];//重新构造直接从本类获取到的ResolvableType数组
            for (int i = 0; i < genericIfcs.length; i++) {

                interfaces[i] = forType(genericIfcs[i], this);

            }

        }


    }

    public static ResolvableType forType(@Nullable Type type, @Nullable ResolvableType owner) {
        VariableResolver variableResolver = null;
        if (owner != null) {

        }
        return forType(type, variableResolver);

    }


    ResolvableType asVariableResolver() {
        if (this == NONE) {
            return null;
        }
        return null;
    }

    @SuppressWarnings("serial")
    static class DefaultVariableResolver implements VariableResolver {
        @Override
        public Object getSource() {
            return null;
        }

        @Override
        public ResolvableType resolveVariable(TypeVariable<?> variable) {
            return null;
        }
    }

    private ResolvableType resolveVariable(TypeVariable<?> variable) {
        if (this.type instanceof TypeVariable) {
            return resolveType().resolveVariable(variable);
        }

        if (this.type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) this.type;
            Class<?> resolved = resolve();
            if (resolved == null) {
                return null;
            }
            TypeVariable<? extends Class<?>>[] variables = resolved.getTypeParameters();
            for (int i = 0; i < variables.length; i++) {

                if (ObjectUtils.nullSafeEquals(variables[i].getName(), variable.getName())) {
                    Type actualType = parameterizedType.getActualTypeArguments()[i];
                    return forType(actualType, this.variableResolver);

                }
            }
            Type ownerType = parameterizedType.getOwnerType();
            if (ownerType != null) {
                return forType(ownerType, this.variableResolver).resolveVariable(variable);
            }

        }
        if (this.variableResolver != null) {
            return this.variableResolver.resolveVariable(variable);
        }
        return null;
    }




    //实现了java 反射里面的Type接口，这里没有实现里面的方法
    @SuppressWarnings("serial")
    static class EmptyType implements Type, Serializable {
        static final Type INSTANCE = new EmptyType();
        Object readResolve() {
            return INSTANCE;
        }
    }

    //策略接口 用来解析TypeVariables
    interface VariableResolver extends Serializable {

        Object getSource();

        //接口里面直接引用包含该接口的类
        ResolvableType resolveVariable(TypeVariable<?> variable);
    }

    //VariableResolver 的默认实现
    @SuppressWarnings("serial")
    private static class TypeVariablesVariableResolver implements VariableResolver {
        private final TypeVariable<?>[] variables;
        private final ResolvableType[] generics;

        public TypeVariablesVariableResolver(TypeVariable<?>[] variables, ResolvableType[] generics) {
            this.variables = variables;
            this.generics = generics;
        }

        @Override
        public Object getSource() {
            return this.generics;
        }

        @Override
        @Nullable
        public ResolvableType resolveVariable(TypeVariable<?> variable) {
            for (int i = 0; i < this.variables.length; i++) {
                TypeVariable<?> v1 = SerializableTypeWrapper.unwrap(this.variables[i]);
                TypeVariable<?> v2 = SerializableTypeWrapper.unwrap(variable);
                if (ObjectUtils.nullSafeEquals(v1, v2)) {
                    return this.generics[i];
                }
            }
            return null;
        }
    }


}
