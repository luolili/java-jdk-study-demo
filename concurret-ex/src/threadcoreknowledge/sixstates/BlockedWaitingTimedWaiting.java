package threadcoreknowledge.sixstates;

/**
 *
 */
public class BlockedWaitingTimedWaiting implements Runnable {

    public static void main(String[] args) {
        BlockedWaitingTimedWaiting runnable = new BlockedWaitingTimedWaiting();
        Thread thread1 = new Thread(runnable);
        thread1.start();
        Thread thread2 = new Thread(runnable);
        thread2.start();
        System.out.println(thread1.getState());
        System.out.println(thread2.getState());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(thread1.getState());
    }

    private synchronized void syn() {
        try {
            Thread.sleep(10000);
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        syn();
    }
}
