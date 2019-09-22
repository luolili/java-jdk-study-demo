package threadcoreknowledge.jmm;

import java.util.concurrent.CountDownLatch;

/**
 * 重新排序
 * 好处：提高处理速度
 * a=3;  load a, set to 3, store a
 * b=2;
 * a=a+1;
 *
 * a=3;
 * a=a+1; load a,set to 3,set to 4, store a
 * b=2
 *
 * 发生重排序的3个情况：
 * 1. 编译器优化： jvm,jit等；
 *
 * 2.cpu
 *
 * 3. 内存的重排序：线程A的修改内容，对于线程B 不可见；可见性
 *
 */
public class OutOfOrderExecution {
    /*private static int x = 0, y = 0;
    private static int a = 0, b = 0;
*/
    //禁止重排序
    private volatile static int x = 0, y = 0;
    private volatile static int a = 0, b = 0;

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
          /*  if (x == 1 && y == 1) {
                System.out.println("x:" + x + ", y:" + y);
                break;
            }*/
            //x=b 先于a=1执行， 重排序
            if (x == 0 && y == 0) {
                System.out.println("x:" + x + ", y:" + y);
                break;
            } else {
                System.out.println("x:" + x + ", y:" + y);
            }
        }

    }
}
