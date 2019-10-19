package com.luo.ptn.creational.singleton;

public class Lazy2CheckSingleton {

    private volatile static Lazy2CheckSingleton singleton = null;

    private Lazy2CheckSingleton() {

    }

    public static Lazy2CheckSingleton getInstance() {
        if (singleton == null) {
            synchronized (Lazy2CheckSingleton.class) {
                if (singleton == null) {
                    //1.分配内存给 singleton
                    //2.初始化 or 3
                    //3.设置 singleton 指向 内存地址
                    singleton = new Lazy2CheckSingleton();
                }
            }

        }
        return singleton;
    }


}
