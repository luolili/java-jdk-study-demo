package threadcoreknowledge.jmm;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * volatile 不适合a++
 * 适合 纯赋值:原子操作
 * 与syn的区别：
 * 1. 只要volatile后面的操作具有原子性，那么就可以做到thread-safe,
 * 2. volatile 的读写操作是没有加锁的
 * 3. volatile only作用在属性上
 * 4. 始终从主存里读取， happens-before
 * 5. 对long/double 的修饰是原子的
 *
 * 哪些方法可以做到可见性？
 * 1. syn/lock/thread-safe class/join/start
 * 2. happens-before 原则
 * 3. 
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
