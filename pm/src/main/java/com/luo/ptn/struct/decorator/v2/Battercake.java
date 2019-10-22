package com.luo.ptn.struct.decorator.v2;

public class Battercake extends ABattercake {
    @Override
    protected String getDesc() {
        return "jian bing";
    }

    @Override
    protected int cost() {
        return 8;
    }
}
