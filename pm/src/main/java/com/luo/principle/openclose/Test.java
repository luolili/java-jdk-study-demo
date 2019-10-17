package com.luo.principle.openclose;

public class Test {
    public static void main(String[] args) {
        ICourse course = new JavaCourse(1, "ew", 22.2);
        System.out.println(course.getName());
        System.out.println(course.getPrice());
        ICourse course2 = new JavaDiscountCourse(1, "ew", 22.2);
        System.out.println(course2.getPrice());
    }
}
