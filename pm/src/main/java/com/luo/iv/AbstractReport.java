package com.luo.iv;

public abstract class AbstractReport {
    public void print() {
        printTitle();
        printBody();
        printTail();
        printWatermark();
    }

    // 表头
    public abstract void printTitle();

    // 表内容
    public abstract void printBody();

    // 表尾巴
    public abstract void printTail();

    // 水印
    public abstract void printWatermark();
}
