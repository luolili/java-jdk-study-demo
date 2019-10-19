package com.luo.ptn.creational.factorymethod;

public class PyVideoFactory extends VideoFactory {
    @Override
    public Video getVideo() {
        return new PyVideo();
    }
}
