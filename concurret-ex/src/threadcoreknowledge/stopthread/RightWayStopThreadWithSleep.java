package threadcoreknowledge.stopthread;

/**
 * run方法里面有sleep/wait方法
 * java.lang.InterruptedException: sleep interrupted
 */
public class RightWayStopThreadWithSleep {


    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            int num = 0;
            while (num <= 300 &&
                    !Thread.currentThread().isInterrupted()) {
                if (num % 100 == 0) {
                    System.out.println(num + " yes");
                }
                num++;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        Thread.sleep(500);
        thread.interrupt();

    }
}
