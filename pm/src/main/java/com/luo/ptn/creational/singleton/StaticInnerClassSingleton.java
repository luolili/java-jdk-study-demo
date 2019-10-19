package com.luo.ptn.creational.singleton;

//延迟 初始化
public class StaticInnerClassSingleton {
    private static class InnerClass {
        private static StaticInnerClassSingleton staticInnerClassSingleton =
                new StaticInnerClassSingleton();
    }

    public static StaticInnerClassSingleton getInstance() {
        return InnerClass.staticInnerClassSingleton;
    }
}
