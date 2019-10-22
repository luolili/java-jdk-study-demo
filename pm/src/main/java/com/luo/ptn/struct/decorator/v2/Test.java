package com.luo.ptn.struct.decorator.v2;

public class Test {
    public static void main(String[] args) {
        ABattercake aBattercake;
        aBattercake = new Battercake();
        aBattercake = new EggDecorator(aBattercake);
        aBattercake = new EggDecorator(aBattercake);
        System.out.println(aBattercake.getDesc() + ":" + aBattercake.cost());


    }
}
