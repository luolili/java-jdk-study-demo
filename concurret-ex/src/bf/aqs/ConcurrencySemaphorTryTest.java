package bf.aqs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 用代码进行压测
 * 一块一块的输出
 */
public class ConcurrencySemaphorTryTest {

    // 线程和数
    public static final int threadCount = 50;
    public static int count = 0;

    public static void main(String[] args) throws Throwable {
        ExecutorService exec = Executors.newCachedThreadPool();
        //阻塞当前 thread
        final Semaphore semaphore = new Semaphore(20);
        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            exec.execute(() -> {
                try {
                    //获取许可，每次获取3个,可丢弃一些请求
                    //semaphore.tryAcquire(3);
                    semaphore.tryAcquire(2, TimeUnit.MILLISECONDS);
                    add(threadNum);
                    semaphore.release(3);
                } catch (Exception e) {
                    System.out.println("exeception");
                }
            });

        }
        exec.shutdown();
        // 最后执行
        System.out.println("finish");

    }

    public static void add(int threadNum) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
        System.out.println("thread num:" + threadNum);
    }
}
