package com.luo.ptn.behavior.template;

public class Test {
    public static void main(String[] args) {
        System.out.println("pm start--");
        ACourse aCourse = new DPCourse();
        aCourse.makeCourse();
        System.out.println("pm end--");

        System.out.println("fe start--");
        ACourse bCourse = new FECourse();
        bCourse.makeCourse();
        System.out.println("fe end--");
    }
}
