package com.luo.ptn.creational.builder;

public class Coach {

    private CourseBuilder courseBuilder;

    public void setCourseBuilder(CourseBuilder courseBuilder) {
        this.courseBuilder = courseBuilder;
    }

    public Course makeCourse(String courseName, String courseArticle, String coursePPT,
                             String courseVideo, String couresQA) {

        courseBuilder.buildCourseName(courseName);
        courseBuilder.buildCourseArticle(courseArticle);
        courseBuilder.buildCoursePPT(coursePPT);
        courseBuilder.buildCourseVideo(courseVideo);
        courseBuilder.buildCourseQA(couresQA);
        return this.courseBuilder.makeCourse();
    }
}
