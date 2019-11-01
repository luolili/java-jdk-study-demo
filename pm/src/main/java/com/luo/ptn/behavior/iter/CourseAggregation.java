package com.luo.ptn.behavior.iter;

public interface CourseAggregation {
    void addCourse(Course course);

    void removeCourse(Course course);

    CourseIterator getCourseIterator();
}
