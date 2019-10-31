package com.luo.ptn.behavior.template;

public abstract class ACourse {

    protected final void makeCourse() {
        this.makePPT();
        this.makeVideo();
        if (needArticle()) {
            makeArticle();
        }
        packageCourse();
    }

    final void makePPT() {
        System.out.println("make ppt");
    }

    final void makeVideo() {
        System.out.println("make vi");
    }

    final void makeArticle() {
        System.out.println("make ar");
    }

    protected boolean needArticle() {
        return false;
    }

    abstract void packageCourse();
}
