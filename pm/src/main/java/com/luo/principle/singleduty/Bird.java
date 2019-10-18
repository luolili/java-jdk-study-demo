package com.luo.principle.singleduty;

public class Bird {
    public void mainMoveMode(String birdName) {
        if ("鸵鸟".equals(birdName)) {
            System.out.println(birdName + " go");
        } else {
            System.out.println(birdName + " fly");
        }
    }
}
