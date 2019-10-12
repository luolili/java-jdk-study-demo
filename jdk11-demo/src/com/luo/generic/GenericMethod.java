package com.luo.generic;

import com.luo.JDK11Test;

import java.util.ArrayList;
import java.util.List;

public class GenericMethod {

    public static <T> void methodPrint(T t) {
        System.out.println(t.getClass());
    }

    //? 表示各种类型的实参，不是形参；用于实例化
    public static void test(List<?> list) {
        System.out.println(list);
    }

    public static void main(String[] args) {
        methodPrint(new JDK11Test());
        methodPrint(new String());
        List<String> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        test(list1);
        test(list2);
    }
}
