package com.luo.ptn.behavior.iter;

import java.util.ArrayList;
import java.util.List;

public class CourseAggregationImpl implements CourseAggregation {
    private List courseList;

    public CourseAggregationImpl() {
        this.courseList = new ArrayList();
    }

    @Override
    public void addCourse(Course course) {
        courseList.add(course);
    }

    @Override
    public void removeCourse(Course course) {
        courseList.remove(course);
    }

    @Override
    public CourseIterator getCourseIterator() {
        return new CourseIteratorImpl(courseList);
    }
}
