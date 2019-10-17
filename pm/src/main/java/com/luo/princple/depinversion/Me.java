package com.luo.princple.depinversion;

public class Me {

    //面向 实现编程
    public void studyJavaCourse() {
        System.out.println("java");
    }

    public void studyFECourse() {
        System.out.println("fe");
    }

    public void studyPyCourse() {
        System.out.println("py");
    }

    //面向interface
    /*public void studyITCourse(ICourse course) {
        course.studyCourse();
    }*/

    private ICourse course;

    public Me(ICourse course) {
        this.course = course;
    }

    public void studyITCourse() {
        course.studyCourse();
    }
    //setter注入

    public Me() {
    }

    public void setCourse(ICourse course) {
        this.course = course;
    }
}
