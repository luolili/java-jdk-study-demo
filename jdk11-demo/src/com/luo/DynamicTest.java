package com.luo;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * 动态语言api test
 * 类型阿紫运行时检查
 */
public class DynamicTest {

    public static void main(String[] args) throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle mh = lookup.findStatic(DynamicTest.class,
                "test", MethodType.methodType(void.class));//方法返回类型


        mh.invokeExact();//执行test方法
    }

    private static void test() {
        System.out.println("--");
    }
}
