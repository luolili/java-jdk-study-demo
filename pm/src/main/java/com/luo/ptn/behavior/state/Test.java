package com.luo.ptn.behavior.state;

public class Test {
    public static void main(String[] args) {
        CourseVideoContext courseVideoContext = new CourseVideoContext();

        courseVideoContext.setCourseVideoState(new PlayState());
        System.out.println("cur state:" +
                courseVideoContext.getCourseVideoState().getClass().getSimpleName());

        courseVideoContext.pause();
        System.out.println("cur state:" +
                courseVideoContext.getCourseVideoState().getClass().getSimpleName());

        courseVideoContext.speed();
        System.out.println("cur state:" +
                courseVideoContext.getCourseVideoState().getClass().getSimpleName());

        courseVideoContext.stop();
        System.out.println("cur state:" +
                courseVideoContext.getCourseVideoState().getClass().getSimpleName());


    }

}
