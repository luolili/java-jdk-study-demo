package com.luo.iv;

public class BReportImpl extends AbstractReport {


    @Override
    public void printTitle() {
        System.out.println("print B title");
    }

    @Override
    public void printBody() {
        System.out.println("print B body");
    }

    @Override
    public void printTail() {
        System.out.println("print B tail");
    }

    @Override
    public void printWatermark() {
        System.out.println("print B watermark");
    }
}
