package tool.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 无界线程池，多余的线程可回收，没有workQueue
 */
public class CachedThreadpoolTest {
    static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Task());

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
