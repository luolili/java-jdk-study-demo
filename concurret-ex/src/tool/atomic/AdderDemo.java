package tool.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class AdderDemo {
    private static class Task implements Runnable {
        AtomicLong counter;

        public Task(AtomicLong counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {

                counter.getAndIncrement();

            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        AtomicLong counter = new AtomicLong();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 200; i++) {
            executorService.submit(new Task(counter));
        }
        long diff = System.currentTimeMillis() - start;
        System.out.println("diff:" + diff);
    }
}
