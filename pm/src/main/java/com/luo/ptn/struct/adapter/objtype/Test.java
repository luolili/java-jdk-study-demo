package com.luo.ptn.struct.adapter.objtype;


public class Test {
    public static void main(String[] args) {
        Target target = new ConcreteTarget();
        target.request();

        Target adapterTar = new Adapter(new Adaptee());
        adapterTar.request();
    }
}
