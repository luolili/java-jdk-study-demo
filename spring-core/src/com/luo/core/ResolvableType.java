package com.luo.core;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * 封装java反射中的Type
 */
public class ResolvableType implements Serializable {


    //将要被解析的类型
    //private final Type type;


    //实现了java 反射里面的Type接口，这里没有实现里面的方法
    @SuppressWarnings("serial")
    static class EmptyType implements Type, Serializable {

        static final Type INSTANCE = new EmptyType();

        Object readResolve() {
            return INSTANCE;
        }
    }
}
