package tool.lock.reenter;

import java.util.concurrent.locks.ReentrantLock;

public class RecurseDemo {
    static ReentrantLock lock = new ReentrantLock();

    //资源需要被处理5次
    public static void getResource() {
        lock.lock();
        try {
            System.out.println("资源被处理了" + (lock.getHoldCount()) + "次");
            if (lock.getHoldCount() < 5) {
                getResource();
            }
        } catch (Exception e) {

        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        getResource();
    }
}
