package tool.lock.reenter;

import java.util.concurrent.locks.ReentrantLock;

public class GetHoldCount {
    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        System.out.println(lock.getHoldCount());

        lock.lock();
        lock.lock();
        System.out.println(lock.getHoldCount());
        try {

        } catch (Exception e) {

        } finally {
            lock.unlock();
        }
        System.out.println(lock.getHoldCount());
        lock.unlock();
        System.out.println(lock.getHoldCount());

    }
}
