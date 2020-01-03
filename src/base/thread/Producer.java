package base.thread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {
    private BlockingQueue<Data> queue;
    private volatile boolean running = true;
    private static AtomicInteger count = new AtomicInteger();
    private Random r = new Random();

    public Producer(BlockingQueue<Data> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(r.nextInt(10));
                int id = count.incrementAndGet();
                Data data = new Data();

                data.setId(id);
                data.setName("name:" + id);
                System.out.println(Thread.currentThread().getName() + "produce " + data);
                if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
                    System.out.println("fail");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.running = false;
    }
}
