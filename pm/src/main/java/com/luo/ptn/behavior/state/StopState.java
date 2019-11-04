package com.luo.ptn.behavior.state;

public class StopState extends CourseVideoState {
    @Override
    public void play() {
        super.courseVideoContext.setCourseVideoState(CourseVideoContext.PLAY_STATE);
    }

    @Override
    public void speed() {
        System.out.println("err speed--");
    }

    @Override
    public void pause() {
        System.out.println(" err pause--");

    }

    @Override
    public void stop() {
        System.out.println("stop--");
    }
}
