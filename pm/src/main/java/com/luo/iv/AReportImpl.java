package com.luo.iv;

public class AReportImpl extends AbstractReport {


    @Override
    public void printTitle() {
        System.out.println("print A title");
    }

    @Override
    public void printBody() {
        System.out.println("print A body");
    }

    @Override
    public void printTail() {
        System.out.println("print A tail");
    }

    @Override
    public void printWatermark() {
        System.out.println("print A watermark");
    }
}
