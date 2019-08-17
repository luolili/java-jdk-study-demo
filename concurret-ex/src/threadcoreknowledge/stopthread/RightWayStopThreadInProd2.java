package threadcoreknowledge.stopthread;

/**
 * 在 catch里面调用interrupt
 */
public class RightWayStopThreadInProd2 implements Runnable {


    @Override
    public void run() {
        while (true) {
            System.out.println("i");
            reInterrupt();
        }
    }

    private void reInterrupt() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new RightWayStopThreadInProd2());
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }
}
