package com.luo.ptn.struct.decorator.v1;

public class Test {

    public static void main(String[] args) {
        Battercake battercake = new Battercake();
        System.out.println(battercake.getDesc() + ": " + battercake.cost());
        BattercakeWithEgg battercakeWithEgg = new BattercakeWithEgg();
        System.out.println(battercakeWithEgg.getDesc() + ": " + battercakeWithEgg.cost());


    }
}
