package com.luo.ptn.creational.singleton;

public class Test {
    public static void main(String[] args) {
        //java product group:扩展性不强
        LazySingleton instance = LazySingleton.getInstance();

        Thread t1 = new Thread(new T());
        Thread t2 = new Thread(new T());
        t1.start();
        t2.start();
        System.out.println("");


    }
}
