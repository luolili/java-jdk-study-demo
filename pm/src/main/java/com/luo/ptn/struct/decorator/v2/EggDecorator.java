package com.luo.ptn.struct.decorator.v2;

public class EggDecorator extends AbstractDecorator {

    public EggDecorator(ABattercake aBattercake) {
        super(aBattercake);
    }

    @Override
    protected String getDesc() {
        return super.getDesc() + " egg";
    }

    @Override
    protected int cost() {
        return super.cost() + 2;
    }
}
