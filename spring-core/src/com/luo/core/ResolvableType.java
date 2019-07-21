package com.luo.core;

import com.luo.lang.Nullable;
import com.luo.util.ConcurrentReferenceHashMap;
import com.luo.util.ObjectUtils;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.Map;

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
        this.resolved = resolveClass();

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

    private ResolvableType(Class<?> clazz) {
        this.resolved = (clazz != null ? clazz : Object.class);
        this.type = this.resolved;

        this.typeProvider = null;
        this.variableResolver = null;
        this.componentType = null;//如果没有加他，会报他没有被初始化
        this.hash = null;
    }

    //获取Type的下属，如果没有就是他自己
    public Type getType() {
        return SerializableTypeWrapper.unwrap(this.type);
    }

    //获取type的原生Class
    public Class<?> getRawClass() {
        if (this.type == this.resolved) {
            return this.resolved;
        }
        Type rawType = this.type;
        if (this.type instanceof ParameterizedType) {
            rawType = ((ParameterizedType) this.type).getRawType();
        }
        return (rawType instanceof Class ? (Class<?>) rawType : null);
    }

    public Object getSource() {
        Object source = (this.typeProvider != null ? this.typeProvider.getSource() : null);
        return (source != null ? source : this.type);
    }

    public Class<?> resolve(Class<?> fallback) {
        return (this.resolved != null ? this.resolved : fallback);
    }

    public Class<?> toClass() {
        return resolve(Object.class);
    }

    public boolean isArray() {
        if (this == NONE) {
            return false;
        }
        return ((this.type instanceof Class && ((Class<?>) this.type).isArray() ||
                (this.type instanceof GenericArrayType || resolveType().isArray())));
    }

    private boolean isAssignableFrom(ResolvableType other) {
        return isAssignableFrom(other, null);
    }

    private boolean isAssignableFrom(ResolvableType other, @Nullable Map<Type, Type> mathedBefore) {
        if (this == NONE || other == NONE) {
            return false;
        }
        if (isArray()) {
            return (other.isArray() && (getComponentType().equals(other.getComponentType())));
        }
        if (mathedBefore != null && mathedBefore.get(this.type) == other.type) {
            return true;
        }

        WildcardBounds ourBounds = WildcardBounds.get(this);
        WildcardBounds typeBounds = WildcardBounds.get(other);

    }

    //inner helper
    private static class WildcardBounds {
        private final Kind kind;
        private final ResolvableType[] bounds;

        public WildcardBounds(Kind kind, ResolvableType[] bounds) {
            this.kind = kind;
            this.bounds = bounds;
        }


        public boolean isSameKind(WildcardBounds bounds) {
            return (this.kind == bounds.kind);
        }

        private boolean isAssignable(ResolvableType source, ResolvableType from) {
            return (this.kind == Kind.UPPER ? source.isAssignableFrom(from) : from.isAssignableFrom(source));
        }

        public boolean isAssignableFrom(ResolvableType... types) {
            for (ResolvableType bound : this.bounds) {
                for (ResolvableType type : types) {
                    if (!isAssignable(bound, type)) {
                        return false;
                    }

                }
            }
            return true;
        }
        public ResolvableType[] getBounds() {
            return this.bounds;
        }

        @Nullable
        public static WildcardBounds get(ResolvableType type) {
            ResolvableType resolveToWildcard = type;
            while (!(resolveToWildcard.getType() instanceof WildcardType)) {
                if (resolveToWildcard == NONE) {
                    return null;
                }
                resolveToWildcard = resolveToWildcard.resolveType();
            }

            WildcardType wildcardType = (WildcardType) resolveToWildcard.type;
            Kind boundsType = (wildcardType.getLowerBounds().length > 0 ? Kind.LOWER : Kind.UPPER);
            Type[] bounds = (boundsType == Kind.UPPER ? wildcardType.getUpperBounds() : wildcardType.getLowerBounds());
            ResolvableType[] resolvableBounds = new ResolvableType[bounds.length];
            for (int i = 0; i < bounds.length; i++) {

                resolvableBounds[i] = forType(bounds[i], type.variableResolver);

            }
            return new WildcardBounds(boundsType, resolvableBounds);

        }

        enum Kind {
            UPPER, LOWER
        }
    }
    //对Type的解析，结果为Class
    private Class<?> resolveClass() {
        if (this.type == EmptyType.INSTANCE) {
            return null;
        }

        if (this.type instanceof Class) {
            return (Class<?>) this.type;
        }

        /**
         * GenericArrayType 代表一个数组类型，他的成分类型是参数化的类型或类型变量
         */
        if (this.type instanceof GenericArrayType) {
            Class<?> resolvedComponent = getComponentType().resolve();
            return (resolvedComponent != null ? Array.newInstance(resolvedComponent, 0).getClass() : null);
        }
        return resolveType().resolve();
    }

    //通过type 获取componentType
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
            //Type继承了Class,调用Class的getComponentType方法
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


    public ResolvableType[] getGenerics() {
        if (this == NONE) {
            return EMPTY_TYPES_ARRAY;
        }
        ResolvableType[] generics = this.generics;
        if (generics == null) {
            if (this.type instanceof Class) {
                //TypeVariable接口继承Type
                Type[] typeParams = ((Class<?>) this.type).getTypeParameters();
                //初始化generics
                generics = new ResolvableType[typeParams.length];
                for (int i = 0; i < generics.length; i++) {
                    //forType 静态方法
                    generics[i] = ResolvableType.forType(typeParams[i], this);

                }

            } else if (this.type instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) this.type).getActualTypeArguments();
                generics = new ResolvableType[actualTypeArguments.length];
                for (int i = 0; i < actualTypeArguments.length; i++) {
                    generics[i] = forType(actualTypeArguments[i], this.variableResolver);
                }

            } else {
                generics = resolveType().getGenerics();
            }
            this.generics = generics;
        }
        return generics;
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
        for (ResolvableType interfaceType : getInterfaces()) {
            ResolvableType interfaceAsType = interfaceType.as(type);
            if (interfaceAsType != NONE) {
                return interfaceAsType;
            }
        }
        return getSuperType().as(type);
    }

    public ResolvableType getSuperType() {
        //-1 先获得ResolvableType
        Class<?> resolved = resolve();
        if (resolved == null || resolved.getGenericSuperclass() == null) {
            return NONE;
        }
        ResolvableType superType = this.superType;

        if (superType == null) {
            superType = forType(resolved.getGenericSuperclass(), this);
            this.superType = superType;
        }
        return superType;


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
            this.interfaces = interfaces;

        }
        return interfaces;
    }

    public static ResolvableType forType(@Nullable Type type, @Nullable ResolvableType owner) {
        VariableResolver variableResolver = null;
        if (owner != null) {
            variableResolver = owner.asVariableResolver();
        }
        return forType(type, variableResolver);

    }

    @Nullable
    VariableResolver asVariableResolver() {
        if (this == NONE) {
            return null;
        }
        return new DefaultVariableResolver();
    }

    @SuppressWarnings("serial")
    private class DefaultVariableResolver implements VariableResolver {
        @Override
        public Object getSource() {
            return ResolvableType.this;
        }

        @Override
        public ResolvableType resolveVariable(TypeVariable<?> variable) {
            return ResolvableType.this.resolveVariable(variable);
        }
    }

    //从TypeVariable 获得ResolvableType 通过Type
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

    //VariableResolver 的实现
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
