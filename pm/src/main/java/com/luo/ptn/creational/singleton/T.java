package com.luo.ptn.creational.singleton;

import sun.awt.image.PixelConverter;

public class T implements Runnable {
    @Override
    public void run() {
        //LazySingleton instance = LazySingleton.getInstance();

        ThreadLocalInstance instance = ThreadLocalInstance.getInstance();
        System.out.println(Thread.currentThread().getName() + "-" + PixelConverter.Argb.instance);


    }
}
