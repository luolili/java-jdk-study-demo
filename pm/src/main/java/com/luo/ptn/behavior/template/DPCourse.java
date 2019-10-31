package com.luo.ptn.behavior.template;

public class DPCourse extends ACourse {


    @Override
    void packageCourse() {
        System.out.println("pa java pro");
    }

    @Override
    protected boolean needArticle() {
        return true;
    }
}
