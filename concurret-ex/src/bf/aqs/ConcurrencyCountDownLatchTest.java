package bf.aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用代码进行压测
 */
public class ConcurrencyCountDownLatchTest {

    // 线程数
    public static final int threadCount = 50;

    public static void main(String[] args) throws Throwable {
        ExecutorService exec = Executors.newCachedThreadPool();
        //阻塞当前 thread
        final CountDownLatch cdl = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            exec.execute(() -> {
                try {
                    add(threadNum);
                } catch (Exception e) {
                    System.out.println("exeception");
                } finally {
                    // 一定会执行
                    cdl.countDown();
                }
            });

        }
        cdl.await();
        exec.shutdown();
        // 最后执行
        System.out.println("finish");

    }

    public static void add(int threadNum) {
        try {
            Thread.sleep(100);
        } catch (Exception e) {

        }
        System.out.println("thread num:" + threadNum);
    }
}
