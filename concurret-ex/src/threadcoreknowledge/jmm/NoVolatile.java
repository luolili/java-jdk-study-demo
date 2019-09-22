package threadcoreknowledge.jmm;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * volatile 不适合a++
 */
public class NoVolatile implements Runnable {

    volatile int a = 0;
    AtomicInteger realA = new AtomicInteger();

    @Override
    public void run() {
        for (int i = 0; i < 1000000; i++) {
            a++;
            realA.incrementAndGet();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable r = new NoVolatile();

        Thread thread1 = new Thread(r);
        Thread thread2 = new Thread(r);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        System.out.println("a:" + ((NoVolatile) r).a);
        System.out.println("a:" + ((NoVolatile) r).realA.get());
    }
}
