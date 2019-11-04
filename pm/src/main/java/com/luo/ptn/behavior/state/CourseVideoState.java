package com.luo.ptn.behavior.state;

public abstract class CourseVideoState {
    protected CourseVideoContext courseVideoContext;

    public CourseVideoContext getCourseVideoContext() {
        return courseVideoContext;
    }

    public void setCourseVideoContext(CourseVideoContext courseVideoContext) {
        this.courseVideoContext = courseVideoContext;
    }

    public abstract void play();

    public abstract void speed();

    public abstract void pause();

    public abstract void stop();
}
