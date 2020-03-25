package bf.iv;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.*;

public class ThreadPoolException {
    public static void main(String[] args) throws Throwable {
        //捕获异常
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1,
                1,
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>()
        ) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                System.out.printf("线程[%s], ex:[%s]", Thread.currentThread().getName(), t.getMessage());
            }
        };
        executor.execute(() -> {
            throw new RuntimeException("erro");
        });
//        executorService.execute(()->{
//            throw new RuntimeException("erro");
//        });
        //阻塞1 秒
        executor.awaitTermination(1, TimeUnit.SECONDS);
//        executorService.awaitTermination(1, TimeUnit.SECONDS);
        // 关闭线程池
        executor.shutdown();
//        executorService.shutdown();

        getAllThreadsIDs();

    }

    public static void getAllThreadsIDs() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        long[] allThreadIds = threadMXBean.getAllThreadIds();
        for (long id : allThreadIds) {

            System.out.println(threadMXBean.getThreadInfo(id).toString());

        }


    }
}
