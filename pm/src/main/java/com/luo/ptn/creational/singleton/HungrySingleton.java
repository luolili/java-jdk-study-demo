package com.luo.ptn.creational.singleton;

import java.io.Serializable;

public class HungrySingleton implements Serializable {
    private final static HungrySingleton singleton = new HungrySingleton();

    private HungrySingleton() {
        if (singleton != null) {
            throw new RuntimeException("单利构造器禁止反射调用");
        }
    }

    public static HungrySingleton getInstance() {
        return singleton;
    }

    //新加
    private Object readResolve() {
        return singleton;
    }
}
