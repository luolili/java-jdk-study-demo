package tool.atomic;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicRef {
    AtomicReference<Thread> sign = new AtomicReference<>();

    public void lock() {
        Thread t = Thread.currentThread();
        while (!sign.compareAndSet(null, t)) {
            System.out.println("自旋失败，继续尝试");
        }
    }

    public void unlock() {
        Thread t = Thread.currentThread();
        while (!sign.compareAndSet(t, null)) {
            System.out.println("自旋失败，继续尝试");
        }
    }

    public static void main(String[] args) {
        AtomicRef atomicRef = new AtomicRef();


    }
}
