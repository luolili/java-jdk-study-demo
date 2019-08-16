package threadcoreknowledge.stopthread;

/**
 * while 内try catch
 * interrupt标记被清除，导致程序继续执行
 */
public class CantInterrupt {
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            int num = 0;
            while (num < 3000) {
                if (num % 100 == 0) {
                    System.out.println("--");
                }
                num++;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        Thread.sleep(500);
        thread.interrupt();
    }
}
