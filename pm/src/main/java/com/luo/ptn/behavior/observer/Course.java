package com.luo.ptn.behavior.observer;

import java.util.Observable;

public class Course extends Observable {
    private String courseName;

    public Course(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void produceQuestion(Course course, Question question) {
        System.out.println(question.getUsername() + "在" + course.getCourseName()
                + "提出:" + question.getQuestionContent());
        setChanged();//状态 改变
        notifyObservers(question);
    }
}
