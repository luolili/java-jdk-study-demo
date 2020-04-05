package tool.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 无界线程池，多余的线程可回收，没有workQueue
 * cpu密集型：
 * cpu8核，线程数8-16
 * IO型：cpu的很多倍
 * cpu * (1+ 平均等待时间/执行时间)  如8*(1+100/1)
 */
public class ScheduledThreadpoolTest {
    static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            //executorService.schedule(new Task(),3, TimeUnit.SECONDS);
            //executorService.scheduleAtFixedRate(new Task(),1,3, TimeUnit.SECONDS);

        }
    }

    static class Task implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println("run--" + Thread.currentThread().getName());
            } catch (Exception e) {

            }


        }
    }
}
