package threadcoreknowledge.threadsafe;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 运行结果error
 */
public class MultiThreadsError implements Runnable {
    int index = 0;
    static MultiThreadsError instance = new MultiThreadsError();

    boolean[] marked = new boolean[1000000];
    static AtomicInteger realIndex = new AtomicInteger();
    static AtomicInteger wrongIndex = new AtomicInteger();
    static volatile CyclicBarrier cyclicBarrier1 = new CyclicBarrier(2);
    static volatile CyclicBarrier cyclicBarrier2 = new CyclicBarrier(2);
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(instance);
        Thread thread2 = new Thread(instance);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(instance.index);
        System.out.println(realIndex.get());
        System.out.println(wrongIndex.get());
    }

    @Override
    public void run() {
       /* while (index < 1000) {
            index++;
        }*/
        for (int i = 0; i < 100000; i++) {
            try {
                cyclicBarrier1.reset();
                cyclicBarrier1.await();
            } catch (InterruptedException e) {
                //e.printStackTrace();
            } catch (BrokenBarrierException e) {
                //e.printStackTrace();
            }
            index++;
            try {
                cyclicBarrier2.reset();
                cyclicBarrier2.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            realIndex.incrementAndGet();
            synchronized (instance) {//需要同步
                if (marked[index]) {
                    System.out.println("发生了error：" + index);
                    wrongIndex.incrementAndGet();
                }
                marked[index] = true;
            }
        }
    }
}
