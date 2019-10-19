package com.luo.ptn.creational.abstractfactory;

public class PyCourseFactory implements CourseFactory {
    @Override
    public Video getVideo() {
        return new PyVideo();
    }

    @Override
    public Article getArticle() {
        return new PyArticle();
    }
}
