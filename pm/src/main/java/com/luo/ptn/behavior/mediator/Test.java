package com.luo.ptn.behavior.mediator;

public class Test {
    public static void main(String[] args) {
        User mee = new User("mee");
        User tom = new User("tom");

        mee.sendMsg("it is mee");
        tom.sendMsg("it is tom");

    }
}
