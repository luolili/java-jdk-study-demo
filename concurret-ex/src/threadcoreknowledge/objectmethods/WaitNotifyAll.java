package threadcoreknowledge.objectmethods;

/**
 * 3个线程：2个thread阻塞，第三个thread唤醒他们
 */
public class WaitNotifyAll implements Runnable {

    private static final Object resourceA = new Object();

    @Override
    public void run() {
        synchronized (resourceA) {
            System.out.println(Thread.currentThread().getName() + "get resourceA lock");
            try {
                System.out.println(Thread.currentThread().getName() + "start to givee out resourceA lock");
                resourceA.wait();
                System.out.println(Thread.currentThread().getName() + " end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        WaitNotifyAll runnable = new WaitNotifyAll();
        Thread threadA = new Thread(runnable);
        Thread threadB = new Thread(runnable);

        Thread threadC = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (resourceA) {
                    resourceA.notifyAll();
                    //resourceA.notify();//一直等待
                    System.out.println("ThreadC notified");
                }
            }
        });

        threadA.start();
        threadB.start();
        Thread.sleep(2000);//保证3个thread的执行顺序
        threadC.start();

    }
}
