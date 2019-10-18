package com.luo.ptn.creational.simplefactory;

public class Test {
    public static void main(String[] args) {

        VideoFactory videoFactory = new VideoFactory();
        Video video = videoFactory.getVideo("java");
        video.produce();


    }
}
