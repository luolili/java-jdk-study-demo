package com.luo.generic;

import java.io.Serializable;

public class AdditionalBound {
    public static <T extends Object> void test(T t) {
        System.out.println(t.getClass());
    }

    //必须同时实现Serializable，Comparable
    public static <T extends Object & Serializable & Comparable> void test2(T t) {
        System.out.println(t.getClass());
    }

    public static Object test3() {
        //return (Object & Number) "rt";//编译不通过
        //return (Object) "er";
        return (Object & CharSequence & Comparable) "we";
    }

    public static void main(String[] args) {
        test(1);//Integer
        test("1");//String
        test2(2);
        test3();
    }
}
