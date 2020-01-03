package base.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintABCWithLockCondition {
    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    private static int num = 0;

    public void exec() {
        lock.lock();
        try {

            while (num < 100) {
                if (num % 3 == 0) {
                    System.out.println(Thread.currentThread().getName() + "num :" + num++);
                    c2.signal();
                    c1.await();
                }
                if (num % 3 == 1) {
                    System.out.println(Thread.currentThread().getName() + "num :" + num++);
                    c3.signal();
                    c2.await();
                }
                if (num % 3 == 2) {
                    System.out.println(Thread.currentThread().getName() + "num :" + num++);
                    c1.signal();
                    c3.await();
                }
            }

        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        PrintABCWithLockCondition task = new PrintABCWithLockCondition();

        new Thread(new Runnable() {
            @Override
            public void run() {
                task.exec();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                task.exec();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                task.exec();
            }
        }).start();


    }
}
