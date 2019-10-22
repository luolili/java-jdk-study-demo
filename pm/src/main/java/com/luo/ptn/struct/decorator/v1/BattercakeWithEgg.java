package com.luo.ptn.struct.decorator.v1;

public class BattercakeWithEgg extends Battercake {

    @Override
    public String getDesc() {
        return super.getDesc() + " 香肠";
    }

    @Override
    public int cost() {
        return super.cost() + 2;
    }
}
