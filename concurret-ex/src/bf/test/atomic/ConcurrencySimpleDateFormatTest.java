package bf.test.atomic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用代码进行压测
 */
public class ConcurrencySimpleDateFormatTest {
    //     static SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
    // 请求次数
    public static final int clientTotal = 1000;
    // 线程和数
    public static final int threadTotal = 50;
    public static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws Throwable {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch cdl = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    // 判断当前 thread 是否可运行
                    semaphore.acquire();
                    format();
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

    public static void format() {
        //定义全局变量出现异常
        try {
            //定义局部变量 thread 封闭
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            f.parse("2018-02-02");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
