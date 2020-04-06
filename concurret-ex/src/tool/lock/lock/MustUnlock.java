package tool.lock.lock;

import java.util.concurrent.locks.ReentrantLock;

public class MustUnlock {
    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        //不可中断
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " run");
        } catch (Exception e) {

        } finally {
            lock.unlock();
        }
    }
}
