package tool.lock.reenter;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SeatBookReadWrite {

    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    public static void read() {
        readLock.lock();
        try {
            System.out.println("得到了读锁" + Thread.currentThread().getName());
            Thread.sleep(100);
        } catch (Exception e) {

        } finally {
            readLock.unlock();
        }
    }

    public static void write() {
        writeLock.lock();
        try {
            System.out.println("得到了写锁" + Thread.currentThread().getName());
            Thread.sleep(100);
        } catch (Exception e) {

        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        new Thread(() -> read(), "r1").start();
        new Thread(() -> read(), "r2").start();
        new Thread(() -> write(), "w1").start();
        new Thread(() -> write(), "w2").start();
    }
}
