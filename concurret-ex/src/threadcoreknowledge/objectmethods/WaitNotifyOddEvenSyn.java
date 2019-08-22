package threadcoreknowledge.objectmethods;

/**
 * 2个thread交替打印odd/even
 */
public class WaitNotifyOddEvenSyn {
    private static int count;
    private static final Object lock = new Object();

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (count < 100) {
                    synchronized (lock) {
                        if ((count & 1) == 0) {
                            System.out.println(Thread.currentThread().getName() + ": " + count++);
                        }
                    }
                }
            }
        }, "even").start();

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (count < 100) {
                    synchronized (lock) {
                        if ((count & 1) == 1) {
                            System.out.println(Thread.currentThread().getName() + ": " + count++);
                        }
                    }
                }
            }
        }, "odd").start();
    }
}
