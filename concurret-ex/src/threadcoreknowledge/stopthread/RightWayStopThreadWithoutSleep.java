package threadcoreknowledge.stopthread;

/**
 * run方法里面没有sleep/wait方法
 */
public class RightWayStopThreadWithoutSleep implements Runnable {


    @Override
    public void run() {
        int num = 0;
        while (!Thread.currentThread().isInterrupted() &&
                num <= Integer.MAX_VALUE / 2) {
            if (num % 10000 == 0) {
                System.out.println(num + " yes");
            }
            num++;
        }
        System.out.println("--end");
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread =
                new Thread(new RightWayStopThreadWithoutSleep());

        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }
}
