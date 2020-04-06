package tool.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

public class AdderDemo2 {
    private static class Task implements Runnable {
        LongAdder counter;

        public Task(LongAdder counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {

                counter.increment();

            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        LongAdder counter = new LongAdder();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 200; i++) {
            executorService.submit(new Task(counter));
        }
        long adderDiff = System.currentTimeMillis() - start;
        System.out.println("adder diff:" + adderDiff);
    }
}
