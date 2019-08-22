package threadcoreknowledge.objectmethods;

/**
 * 2个thread交替打印odd/even
 * 拿到锁，就打印；
 * 打印完，唤醒其他thread, 就休眠
 */
public class WaitNotifyOddEvenWait {
    private static int count;
    private static final Object lock = new Object();

    public static void main(String[] args) {
        new Thread(new TurningRunner(), "even").start();
        new Thread(new TurningRunner(), "odd").start();
    }

    static class TurningRunner implements Runnable {
        @Override
        public void run() {
            while (count <= 100) {
                synchronized (lock) {
                    System.out.println(Thread.currentThread().getName() + ": " + count++);
                    lock.notify();
                    if (count <= 100) {
                        try {
                            lock.wait();//释放锁
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }
}
