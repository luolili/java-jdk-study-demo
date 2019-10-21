package com.luo.ptn.creational.prototype;

import java.io.Serializable;

public class HungrySingleton01 implements Serializable, Cloneable {
    private final static HungrySingleton01 singleton = new HungrySingleton01();

    private HungrySingleton01() {
        if (singleton != null) {
            throw new RuntimeException("单利构造器禁止反射调用");
        }
    }

    public static HungrySingleton01 getInstance() {
        return singleton;
    }

    //新加
    private Object readResolve() {
        return singleton;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
