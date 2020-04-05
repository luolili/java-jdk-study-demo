package tool.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixThreadpoolTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 1000; i++) {

            executorService.execute(new Task());

        }
    }

    static class Task implements Runnable {
        @Override
        public void run() {
            System.out.println("run--" + Thread.currentThread().getName());
        }
    }
}
