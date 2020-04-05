package tool.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * OOM 演示  VM : -Xmx8m -Xms8m
 */
public class FixThreadpoolOOM {
    static ExecutorService executorService = Executors.newFixedThreadPool(1);

    public static void main(String[] args) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            executorService.execute(new Task());

        }
    }

    static class Task implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (Exception e) {

            }


        }
    }
}
