package threadcoreknowledge.stopthread;

/**
 * run方法里面有sleep/wait方法
 * 循环里面有sleep/wait
 */
public class RightWayStopThreadWithSleepPerLoop {

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            int num = 0;
            try {
                while (num <= 10000 &&
                        !Thread.currentThread().isInterrupted()) {
                    if (num % 100 == 0) {
                        System.out.println(num + " yes");
                    }
                    num++;
                    Thread.sleep(10);
                }

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
