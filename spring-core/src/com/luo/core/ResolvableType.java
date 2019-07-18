package com.luo.core;

import com.luo.lang.Nullable;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * 封装java反射中的Type
 */
@SuppressWarnings("serial")
public class ResolvableType implements Serializable {

    @Nullable
    private final SerializableTypeWrapper.TypeProvider typeProvider;

    //将要被解析的类型
    private final Type type;


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

}
