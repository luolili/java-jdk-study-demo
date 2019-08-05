package com.luo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * lmd的参数本地化
 * 类型 推导，可以不用直接申明类型，减少冗余代码
 */
public class LocalVarTest {
    public static void main(String[] args) {
        //lmdInJava8();
        test();
    }

    private static void test() {
        List<Integer> nums = Arrays.asList(5, 2, 1, 3);

        /*nums.sort( (Integer s1, Integer s2)->{
            if (s1.equals(s2)) {
                return 0;
            }
            else {
                return s1>s2?1:-1;
            }
        });*/

        //可以在var前面加@NNotNull等注解
        nums.sort((var s1, var s2) -> {
            if (s1.equals(s2)) {
                return 0;
            } else {
                return s1 > s2 ? 1 : -1;
            }
        });
        System.out.println(nums);

    }

    private static void varIJava10() {
        int var = 9;//var是保留字，不是关键字
        //var j;//var 必须初始化
        var i = 8;
        var str = "ju";
        var list = new ArrayList<String>();
        var map = Map.of(1, "w", 2, "r");
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            System.out.println(entry);
        }
        for (var entry : map.entrySet()) {
            System.out.println(entry);
        }
    }

    private static void lmdInJava8() {
        new Thread(() -> System.out.println("hu"));
        List<String> list = Arrays.asList("er", "tg");
        list.forEach(System.out::println);
    }

    class ErrUseVar {
        //var n=0;//只可用于局部变量
       /* var f() {
            return 2;
        }*/
      /* int f(var h){

       }*/
    }
}
