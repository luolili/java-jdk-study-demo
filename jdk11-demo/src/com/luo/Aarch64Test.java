package com.luo;

import java.util.concurrent.TimeUnit;

public class Aarch64Test {
    public static void main(String[] args) {
        mathOnJDK11();
    }

    @SuppressWarnings("all")
    public static void mathOnJDK11() {
        long startTime = System.nanoTime();
        for (int i = 0; i < 10000000; i++) {
            Math.sin(i);
            Math.cos(i);
        }

        long endTime = System.nanoTime();
        System.out.println("time " + (endTime - startTime));
        System.out.println(TimeUnit
                .NANOSECONDS.toMillis(endTime - startTime));
    }
}
