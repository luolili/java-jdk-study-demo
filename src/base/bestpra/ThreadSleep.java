package base.bestpra;

import java.util.concurrent.TimeUnit;

public class ThreadSleep {

    public static void main(String[] args) {
        sleepNew();
    }

    public static void sleepOld() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sleep old");
    }

    public static void sleepNew() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sleep new");
    }
}
