package com.luo.test;

import com.luo.util.StringUtils;

public class StringTest {
    public static void main(String[] args) {

        //abstract cannot be intantiated
        //new StringUtils();
        String re = StringUtils.capitalize("heloo");
        System.out.println(re);
        String re1 = StringUtils.uncapitalize("Heloo");

        System.out.println(re1);
    }
}
