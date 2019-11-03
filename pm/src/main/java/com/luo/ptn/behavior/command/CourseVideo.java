package com.luo.ptn.behavior.command;

public class CourseVideo {
    private String name;

    public CourseVideo(String name) {
        this.name = name;
    }

    //命令
    public void open() {
        System.out.println(this.name + " open");
    }

    public void close() {
        System.out.println(this.name + " close");
    }

}
