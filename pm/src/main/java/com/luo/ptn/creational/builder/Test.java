package com.luo.ptn.creational.builder;

public class Test {
    public static void main(String[] args) {
        //java product group:扩展性不强
        CourseActualBuilder builder = new CourseActualBuilder();
        Coach coach = new Coach();

        coach.setCourseBuilder(builder);
        Course course = coach.makeCourse("java", "ppt", "article",
                "v", "qa");
        System.out.println(course);

    }
}
