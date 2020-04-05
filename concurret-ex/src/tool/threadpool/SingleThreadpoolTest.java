package tool.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程数是1
 */
public class SingleThreadpoolTest {
    static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
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
