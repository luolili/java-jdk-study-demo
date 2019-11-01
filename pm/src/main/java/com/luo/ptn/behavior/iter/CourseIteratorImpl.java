package com.luo.ptn.behavior.iter;

import java.util.List;

public class CourseIteratorImpl implements CourseIterator {
    private List courseList;
    int position;
    Course course;

    public CourseIteratorImpl(List courseList) {
        this.courseList = courseList;
    }

    @Override
    public Course netCourse() {
        System.out.println("--" + position);
        Course course = (Course) courseList.get(position);
        position++;
        return course;
    }

    @Override
    public boolean isLastCourse() {
        if (position < courseList.size()) {
            return false;
        }
        return true;
    }
}
