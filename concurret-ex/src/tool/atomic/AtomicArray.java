package tool.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicArray {

    public static void main(String[] args) throws Exception {
        AtomicIntegerArray array = new AtomicIntegerArray(1000);
        Decr decr = new Decr(array);
        Incr incr = new Incr(array);
        Thread[] ti = new Thread[10];
        Thread[] td = new Thread[10];
        for (int i = 0; i < 10; i++) {
            td[i] = new Thread(decr);
            ti[i] = new Thread(incr);
        }
        for (int i = 0; i < 10; i++) {
            td[i].start();
            ti[i].start();
        }
        for (int i = 0; i < 10; i++) {
            td[i].join();
            ti[i].join();
        }

        for (int i = 0; i < array.length(); i++) {
            if (array.get(i) != 0) {
                System.out.println("error");
            }
        }
        System.out.println("right end");
    }
}

class Decr implements Runnable {
    AtomicIntegerArray array;

    public Decr(AtomicIntegerArray array) {
        this.array = array;
    }

    @Override
    public void run() {
        for (int i = 0; i < array.length(); i++) {
            array.getAndDecrement(i);
        }
    }
}

class Incr implements Runnable {
    AtomicIntegerArray array;

    public Incr(AtomicIntegerArray array) {
        this.array = array;
    }

    @Override
    public void run() {
        for (int i = 0; i < array.length(); i++) {
            array.getAndIncrement(i);
        }
    }
}
