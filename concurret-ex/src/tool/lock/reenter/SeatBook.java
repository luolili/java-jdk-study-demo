package tool.lock.reenter;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 电影院预定座位
 */
public class SeatBook {
    static ReentrantLock lock = new ReentrantLock();

    public static void book() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "开始预定");
            Thread.sleep(100);
            System.out.println(Thread.currentThread().getName() + "预定结束");
        } catch (Exception e) {

        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        new Thread(() -> book()).start();
        new Thread(() -> book()).start();
        new Thread(() -> book()).start();
    }
}
