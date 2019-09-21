package threadcoreknowledge.jmm;

import java.util.concurrent.CountDownLatch;

/**
 * 重新排序
 */
public class OutOfOrderExecution {
    private static int x = 0, y = 0;
    private static int a = 0, b = 0;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        int count = 0;
        for (; ; ) {
            count++;
            x = 0;
            y = 0;
            a = 0;
            b = 0;
            Thread one = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    a = 1;
                    x = b;
                }
            });

            Thread two = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    b = 1;
                    y = a;
                }
            });

            one.start();
            two.start();
            latch.countDown();
            one.join();
            two.join();
            System.out.println("第：" + count);
            if (x == 1 && y == 1) {
                System.out.println("x:" + x + ", y:" + y);
                break;
            } else {
                System.out.println("x:" + x + ", y:" + y);
            }
        }

    }
}
