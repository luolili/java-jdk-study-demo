package bf.test.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用代码进行压测
 */
public class ConcurrencyLongTest {

    // 请求次数
    public static final int clientTotal = 1000;
    // 线程和数
    public static final int threadTotal = 50;
    //AtomicLong 不断的去循环：先获取 主内存的值，然后进行更新，当并发不高的时候，成功概率高，反之很低
    public static AtomicLong count = new AtomicLong(0);

    public static void main(String[] args) throws Throwable {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch cdl = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    // 判断当前 thread 是否可运行
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (Exception e) {

                }
                cdl.countDown();
            });

        }
        cdl.await();
        executorService.shutdown();
        System.out.println("count=" + count);

    }

    public static void add() {

        count.incrementAndGet();
    }
}
