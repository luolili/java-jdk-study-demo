package tool.lock.reenter;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class FairLock {

    public static void main(String[] args) {
        PrintQueue printQueue = new PrintQueue();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new Job(printQueue));

        }
        for (Thread t : threads) {
            t.start();
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }

        }
    }
}

class PrintQueue {
    ReentrantLock lock = new ReentrantLock(true);

    public void print(Object o) {
        lock.lock();
        try {
            int duration = new Random().nextInt() * 1000;
            System.out.println(Thread.currentThread().getName() + "正在打印，需要" + duration / 1000);
            Thread.sleep(duration);
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
        //
        lock.lock();
        try {
            Long duration = (long) Math.random() * 1000;
            System.out.println(Thread.currentThread().getName() + "正在打印，需要" + duration / 1000);
            Thread.sleep(duration);
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }
}

class Job implements Runnable {
    PrintQueue printQueue;

    public Job(PrintQueue printQueue) {
        this.printQueue = printQueue;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "开始打印");
        printQueue.print(new Object());
        System.out.println(Thread.currentThread().getName() + "结束打印");
    }
}