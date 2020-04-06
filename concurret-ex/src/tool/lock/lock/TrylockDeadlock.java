package tool.lock.lock;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 避免死锁
 */
public class TrylockDeadlock implements Runnable {
    static ReentrantLock lock1 = new ReentrantLock();
    static ReentrantLock lock2 = new ReentrantLock();
    int flag = 1;

    @Override
    public void run() {
        if (flag == 1) {
            try {
                if (lock1.tryLock(5, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("");
                        Thread.sleep(new Random().nextInt(100));
                    } catch (Exception e) {

                    } finally {
                        lock1.unlock();
                        Thread.sleep(new Random().nextInt(100));
                    }
                }
            } catch (Exception e) {

            }
        } else {

        }
    }

    public static void main(String[] args) {

    }
}
