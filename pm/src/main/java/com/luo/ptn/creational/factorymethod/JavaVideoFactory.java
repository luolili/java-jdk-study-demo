package com.luo.ptn.creational.factorymethod;

//子类
public class JavaVideoFactory extends VideoFactory {
    @Override
    public Video getVideo() {
        return new JavaVideo();
    }
}
