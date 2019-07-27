package com.luo.core;

import com.luo.lang.Nullable;
import com.luo.util.*;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
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
    //type对应的Class
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
        //Type是Class的子接口
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

    //factory method
    public static ResolvableType forClass(@Nullable Class<?> clazz) {
        return new ResolvableType(clazz);
    }

    public boolean isAssignableFrom(Class<?> other) {
        return isAssignableFrom(forClass(other), null);
    }

    public boolean isAssignableFrom(ResolvableType other) {
        return isAssignableFrom(other, null);
    }

    //判断obj是不是ResolvableType的实例
    public boolean isInstance(Object obj) {
        return (obj != null && isAssignableFrom(obj.getClass()));
    }

    public ResolvableType asCollection() {
        return as(Collection.class);
    }

    public ResolvableType asMap() {
        return as(Map.class);
    }

    public boolean hasGenerics() {
        return getGenerics().length > 0;
    }

    public boolean isUnresolvableTypeVariable() {
        if (this.type instanceof TypeVariable) {
            //无variableResolver
            if (this.variableResolver == null) {
                return true;//可以被解析
            }

            TypeVariable<?> variable = (TypeVariable<?>) this.type;
            ResolvableType resolved = this.variableResolver.resolveVariable(variable);
            if (resolved == null || resolved.isUnresolvableTypeVariable()) {
                return true;
            }

        }
        return false;
    }

    public Class<?>[] resolveGenerics() {
        ResolvableType[] generics = getGenerics();
        Class<?>[] resolvedGenerics = new Class<?>[generics.length];
        for (int i = 0; i < generics.length; i++) {
            resolvedGenerics[i] = generics[i].resolve();
        }
        return resolvedGenerics;

    }

    public Class<?>[] resolveGenerics(Class<?> fallback) {
        ResolvableType[] generics = getGenerics();
        Class<?>[] resolvedGenerics = new Class<?>[generics.length];
        for (int i = 0; i < generics.length; i++) {
            resolvedGenerics[i] = generics[i].resolve(fallback);
        }
        return resolvedGenerics;

    }
    /**
     * @param other         the type to be checked
     * @param matchedBefore
     * @return true the other can be assigned to  this ResolvableType
     */
    private boolean isAssignableFrom(ResolvableType other, @Nullable Map<Type, Type> matchedBefore) {
        Assert.notNull(other, "ResolvableType must not be null");

        //-1 当other是空
        if (this == NONE || other == NONE) {
            return false;
        }
        //-1 this 是数组，还要比较数组里面的元素类型是否一样
        if (isArray()) {
            return (other.isArray() && (getComponentType().equals(other.getComponentType())));
        }
        //
        if (matchedBefore != null && matchedBefore.get(this.type) == other.type) {
            return true;
        }

        WildcardBounds ourBounds = WildcardBounds.get(this);
        WildcardBounds typeBounds = WildcardBounds.get(other);

        if (typeBounds != null) {
            return (ourBounds != null && ourBounds.isSameKind(typeBounds) &&
                    ourBounds.isAssignableFrom(typeBounds.getBounds()));
        }

        if (ourBounds != null) {
            //other是单个元素，非数组
            return ourBounds.isAssignableFrom(other);
        }
        //nested generic var
        boolean exactMatch = (matchedBefore != null);

        boolean checkGenerics = true;
        Class<?> ourResolved = null;
        if (this.type instanceof TypeVariable) {
            TypeVariable variable = (TypeVariable) this.type;
            if (this.variableResolver != null) {
                ResolvableType resolved = this.variableResolver.resolveVariable(variable);
                if (resolved != null) {
                    ourResolved = resolved.resolve();
                }

            }

            if (ourResolved == null) {
                if (other.variableResolver != null) {
                    ResolvableType resolved = other.variableResolver.resolveVariable(variable);
                    if (resolved != null) {
                        ourResolved = resolved.resolve();
                        checkGenerics = false;
                    }

                }
            }

            if (ourResolved == null) {
                // Unresolved type variable, potentially nested -> never insist on exact match
                exactMatch = false;
            }

        }
        if (ourResolved == null) {
            ourResolved = resolve(Object.class);
        }
        //获取resolved
        Class<?> otherResolved = other.toClass();

        // We need an exact type match for generics
        // List<CharSequence> is not assignable from List<String>
        if (exactMatch ? !ourResolved.equals(otherResolved) : !ClassUtils.isAssignable(ourResolved, otherResolved)) {
            return false;
        }
        //比较generics : ResolvableType数组
        if (checkGenerics) {
            ResolvableType[] ourGenerics = getGenerics();
            ResolvableType[] typeGenerics = other.as(ourResolved).getGenerics();

            if (ourGenerics.length != typeGenerics.length) {
                return false;
            }
            if (matchedBefore == null) {
                matchedBefore = new IdentityHashMap<>(1);
            }
            matchedBefore.put(this.type, other.type);
            for (int i = 0; i < ourGenerics.length; i++) {
                if (!ourGenerics[i].isAssignableFrom(typeGenerics[i], matchedBefore)) {
                    return false;
                }
            }

        }
        return true;

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

    //利用Type, VariableResolver, TypeProvider构造ResolvableType
    static ResolvableType forType(
            @Nullable Type type, @Nullable SerializableTypeWrapper.TypeProvider typeProvider, @Nullable VariableResolver variableResolver) {
        //SerializableTypeWrapper作用
        if (type == null && typeProvider != null) {
            type = SerializableTypeWrapper.forTypeProvider(typeProvider);
        }
        if (type == null) {
            return NONE;
        }
        if (type instanceof Class) {//type不需要转为Class
            return new ResolvableType(type, typeProvider, variableResolver, (ResolvableType) null);
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

    //把给定的Class类型的type转为ResolvableType
    public ResolvableType as(Class<?> type) {
        //先检查当前类是不是空类
        if (this == NONE) {
            return NONE;
        }
        //获取本类的type的Class
        Class<?> resolved = resolve();
        //当本类的type的Class为空or等于type
        if (resolved == null || type == resolved) {
            return this;
        }
        //用每个接口来将type转为ResolvableType，一旦有不是空的ResolvableType就返回他
        for (ResolvableType interfaceType : getInterfaces()) {
            ResolvableType interfaceAsType = interfaceType.as(type);
            if (interfaceAsType != NONE) {
                return interfaceAsType;
            }
        }
        //对本类的超类进行前面的转换
        return getSuperType().as(type);
    }

    public ResolvableType getSuperType() {
        //-1 先获得ResolvableType 的tye对应的Class，目的是通过Class确定他是否有超类
        Class<?> resolved = resolve();
        if (resolved == null || resolved.getGenericSuperclass() == null) {
            return NONE;
        }
        //-2 有超类，先检查本类的属性superType
        ResolvableType superType = this.superType;

        if (superType == null) {
            //用Class作为参数调用静态方法forType：在非静态方法里面调用静态方法
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

    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }
        if (!(other instanceof ResolvableType)) {
            return false;
        }


        //类型转化
        ResolvableType otherType = (ResolvableType) other;
        //属性type不一样
        if (!ObjectUtils.nullSafeEquals(this.type, otherType.type)) {
            return false;
        }
        // //属性type一样, 比较typeProvider 和根据typeProvider获得的Type

        if (this.typeProvider != otherType.typeProvider && (this.typeProvider == null || otherType.typeProvider == null) ||
                !ObjectUtils.nullSafeEquals(this.typeProvider.getType(), otherType.typeProvider.getType())) {
            return false;
        }
        //componentType
        if (!ObjectUtils.nullSafeEquals(this.componentType, otherType.componentType)) {
            return false;
        }
        return true;

    }

    @Override
    public int hashCode() {
        return (this.hash != null ? this.hash : calculateHashCode());
    }

    private Object readResolve() {
        return (this.type == EmptyType.INSTANCE ? NONE : this);
    }

    @Override
    public String toString() {
        if (isArray()) {
            return getComponentType() + "[]";
        }

        if (this.resolved == null) {
            return "?";
        }
        if (this.type instanceof TypeVariable) {
            TypeVariable<?> variable = (TypeVariable<?>) this.type;
            // Don't bother with variable boundaries for toString()...
            // Can cause infinite recursions in case of self-references
            if (this.variableResolver == null || this.variableResolver.resolveVariable(variable) == null) {
                return "?";
            }
        }

        StringBuilder result = new StringBuilder(this.resolved.getName());
        if (hasGenerics()) {
            result.append('<');
            result.append(StringUtils.arrayToDelimitedString(getGenerics(), ", "));
            result.append('>');
        }

        return result.toString();
    }

    public static ResolvableType forRawClass(@Nullable Class<?> clazz) {
        return new ResolvableType(clazz) {
            @Override
            public ResolvableType[] getGenerics() {
                return EMPTY_TYPES_ARRAY;
            }

            @Override
            public boolean isAssignableFrom(Class<?> other) {
                return (clazz == null || ClassUtils.isAssignable(clazz, other));
            }

            @Override
            public boolean isAssignableFrom(ResolvableType other) {
                Class<?> otherClass = other.getRawClass();
                return (otherClass != null && (clazz == null || ClassUtils.isAssignable(clazz, otherClass)));
            }
        };
    }

    public static ResolvableType forType(@Nullable Type type) {
        return forType(type, null, null);
    }

    static void resolveMethodParameter(MethodParameter methodParameter) {
        Assert.notNull(methodParameter, "MethodParameter must not be null");

        ResolvableType owner = forType(methodParameter.getContainingClass()).as(methodParameter.getDeclaringClass());
        methodParameter.setParameterType(forType(null,
                new SerializableTypeWrapper.MethodParameterTypeProvider(methodParameter), owner.asVariableResolver()).resolve());

    }

    public static ResolvableType forMethodReturnType(Method method, Class<?> implementationClass) {
        Assert.notNull(method, "Method must not be null");
        MethodParameter methodParameter = new MethodParameter(method, -1);
        methodParameter.setContainingClass(implementationClass);
        return forMethodParameter(methodParameter);
    }


    public static ResolvableType forMethodReturnType(Method method) {
        Assert.notNull(method, "Method must not be null");
        return forMethodParameter(new MethodParameter(method, -1));
    }

    public static ResolvableType forMethodParameter(MethodParameter methodParameter) {
        return forMethodParameter(methodParameter, (Type) null);
    }

    public static ResolvableType forMethodParameter(MethodParameter methodParameter, @Nullable Type targetType) {
        Assert.notNull(methodParameter, "MethodParameter must not be null");
        ResolvableType owner = forType(methodParameter.getContainingClass()).as(methodParameter.getDeclaringClass());
        return forType(targetType, new SerializableTypeWrapper.MethodParameterTypeProvider(methodParameter), owner.asVariableResolver()).
                getNested(methodParameter.getNestingLevel(), methodParameter.typeIndexesPerLevel);

    }

    public ResolvableType getNested(int nestingLevel, @Nullable Map<Integer, Integer> typeIndexesPerLevel) {
        ResolvableType result = this;
        for (int i = 2; i <= nestingLevel; i++) {
            if (result.isArray()) {
                result = result.getComponentType();
            } else {
                // Handle derived types
                while (result != ResolvableType.NONE && !result.hasGenerics()) {
                    result = result.getSuperType();
                }
                Integer index = (typeIndexesPerLevel != null ? typeIndexesPerLevel.get(i) : null);
                index = (index == null ? result.getGenerics().length - 1 : index);
                result = result.getGeneric(index);
            }
        }
        return result;
    }

    public ResolvableType getGeneric(@Nullable int... indexes) {
        //在indexes==null or indexes.length==0的时候需要用到
        ResolvableType[] generics = getGenerics();
        if (indexes == null || indexes.length == 0) {
            return (generics.length == 0 ? NONE : generics[0]);
        }
        //初始化单个generic
        ResolvableType generic = this;
        for (int index : indexes) {
            generics = generic.getGenerics();
            //index范围检查
            if (index < 0 || index >= generics.length) {
                return NONE;
            }
            generic = generics[index];
        }
        return generic;
    }


    boolean isWildcardWithoutBounds() {
        if (this.type instanceof WildcardType) {
            WildcardType wt = (WildcardType) this.type;
            if (wt.getLowerBounds().length == 0) {
                Type[] upperBounds = wt.getUpperBounds();
                if (upperBounds.length == 0 || (upperBounds.length == 1 && Object.class == upperBounds[0])) {
                    return true;
                }
            }
        }
        return false;

    }

    boolean isEntirelyUnresolvable() {
        if (this == NONE) {
            return false;
        }
        ResolvableType[] generics = getGenerics();
        //检查每个ResolvableType 是否可解析和当他是WildcardType时，是否有bounds
        for (ResolvableType generic : generics) {
            if (!generic.isUnresolvableTypeVariable() && !generic.isWildcardWithoutBounds()) {
                return false;
            }

        }
        return true;
    }


    public boolean hasUnresolvableGenerics() {
        if (this == NONE) {
            return false;
        }
        ResolvableType[] generics = getGenerics();
        for (ResolvableType generic : generics) {

            if (generic.isUnresolvableTypeVariable() || generic.isWildcardWithoutBounds()) {
                return true;//存在
            }

        }

        Class<?> resolved = resolve();

        if (resolved != null) {
            for (Type gIfc : resolved.getGenericInterfaces()) {

                if (gIfc instanceof Class) {
                    if (forClass((Class<?>) gIfc).hasGenerics()) {
                        return true;//存在
                    }
                }

            }
            return getSuperType().hasUnresolvableGenerics();
        }
        return false;
    }

    private static class SyntheticParameterizedType implements ParameterizedType, Serializable {

        private final Type rawType;
        private final Type[] typeArguments;

        public SyntheticParameterizedType(Type rawType, Type[] typeArguments) {
            this.rawType = rawType;
            this.typeArguments = typeArguments;
        }

        @Override
        @Nullable
        public Type getOwnerType() {
            return null;
        }

        @Override
        public String getTypeName() {
            StringBuilder result = new StringBuilder(this.rawType.getTypeName());

            if (this.typeArguments.length > 0) {
                result.append('<');
                for (int i = 0; i < typeArguments.length; i++) {

                    if (i > 0) {
                        result.append(", ");
                    }
                    result.append(this.typeArguments[i].getTypeName());

                }
            }
            result.append('>');
            return result.toString();
        }

        @Override
        public Type[] getActualTypeArguments() {
            return this.typeArguments;
        }

        @Override
        public Type getRawType() {
            return this.rawType;
        }

        @Override
        public boolean equals(Object other) {

            if (this == other) {
                return true;
            }

            if (!(other instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType otherType = (ParameterizedType) other;
            //Arrays.equals比较2个数组是否相等
            return (otherType.getOwnerType() == null && (this.rawType.equals(otherType.getRawType())) &&
                    Arrays.equals(this.typeArguments, otherType.getActualTypeArguments()));
        }

        //Arrays.hashCode计算一个数组的hash
        @Override
        public int hashCode() {
            return this.rawType.hashCode() * 31 + Arrays.hashCode(this.typeArguments);
        }

        @Override
        public String toString() {
            return getTypeName();
        }
    }

    public static ResolvableType forClassWithGenerics(Class<?> clazz, ResolvableType... generics) {

        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(generics, "Generics array must not be null");
        TypeVariable<? extends Class<?>>[] variables = clazz.getTypeParameters();
        Assert.isTrue(variables.length == generics.length, "Mismatched number of generics specified");
        Type[] arguments = new Type[generics.length];

        for (int i = 0; i < generics.length; i++) {
            ResolvableType generic = generics[i];
            Type argument = (generic != null ? generic.getType() : null);
            arguments[i] = (argument != null && !(argument instanceof TypeVariable) ? argument : variables[i]);

        }
        ParameterizedType syntheticParameterizedType = new SyntheticParameterizedType(clazz, arguments);

        return forType(syntheticParameterizedType, new TypeVariablesVariableResolver(variables, generics));
    }

    public static ResolvableType forClassWithGenerics(Class<?> clazz, Class<?>... generics) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(generics, "Generics array must not be null");
        ResolvableType[] resolvableTypes = new ResolvableType[generics.length];

        for (int i = 0; i < generics.length; i++) {
            resolvableTypes[i] = forType(generics[i]);
        }
        return forClassWithGenerics(clazz, resolvableTypes);

    }

}
