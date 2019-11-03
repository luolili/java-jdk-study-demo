package com.luo.ptn.behavior.visitor;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Course> courseList = new ArrayList<>();

        FreeCourse freeCourse = new FreeCourse();

        freeCourse.setName("java");
        CodingCourse codingCourse = new CodingCourse();
        codingCourse.setName("javase");
        codingCourse.setPrice(2);
        courseList.add(freeCourse);
        courseList.add(codingCourse);
        for (Course course : courseList) {
            course.accept(new Visitor());

        }

    }
}
