package threadcoreknowledge.stopthread;

/**
 * volatile的局限
 */
public class WrongwayVolatile implements Runnable {

    private volatile boolean canceled = false;

    public static void main(String[] args) throws InterruptedException {
        WrongwayVolatile r = new WrongwayVolatile();

        Thread thread = new Thread(r);

        thread.start();
        Thread.sleep(1000);
        r.canceled = true;
    }

    @Override
    public void run() {
        int num = 0;
        try {
            while (num < 10000 && !canceled) {
                if (num % 100 == 0) {
                    System.out.println(num + "  yes");
                }
                num++;
                Thread.sleep(1);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
