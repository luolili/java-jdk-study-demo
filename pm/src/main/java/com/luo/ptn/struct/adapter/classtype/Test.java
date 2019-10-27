package com.luo.ptn.struct.adapter.classtype;

public class Test {
    public static void main(String[] args) {
        Target target = new ConcreteTarget();
        target.request();

        Target adapterTar = new Adapter();
        adapterTar.request();
    }
}
