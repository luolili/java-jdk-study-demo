package bf.test.atomic;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用代码进行压测
 */
public class ConcurrencyHashSetTest {

    // 请求次数
    public static final int clientTotal = 1000;
    // 线程和数
    public static final int threadTotal = 50;
    public static AtomicInteger count = new AtomicInteger(0);
    static Set<Integer> list = new HashSet<>();

    public static void main(String[] args) throws Throwable {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch cdl = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            final int num = i;
            executorService.execute(() -> {
                try {
                    // 判断当前 thread 是否可运行
                    semaphore.acquire();
                    add(num);
                    semaphore.release();
                } catch (Exception e) {

                }
                cdl.countDown();
            });

        }
        cdl.await();
        executorService.shutdown();
        System.out.println("len=" + list.size());

    }

    public static void add(int i) {

        list.add(i);
    }
}
