package com.luo.ptn.struct.adapter.classtype;

public class ConcreteTarget implements Target {
    @Override
    public void request() {
        System.out.println("target impl");
    }
}
