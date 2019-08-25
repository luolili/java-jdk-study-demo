package threadcoreknowledge.objectmethods;

/**
 * join 期间的thread 状态：waiting
 */
public class JoinInterrupt {

    public static void main(String[] args) {
        Thread mainThread = Thread.currentThread();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mainThread.interrupt();
                    Thread.sleep(2000);
                    System.out.println("thread 1 finished ");
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        });
        thread1.start();
        System.out.println("waiting ");
        try {
            thread1.join();
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " interrupted");
            //e.printStackTrace();
        }
        System.out.println("子thread运行完");
    }
}
