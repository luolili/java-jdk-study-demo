package base.thread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    private BlockingQueue<Data> queue;
    private Random r = new Random();

    public Consumer(BlockingQueue<Data> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Data data = this.queue.take();
                Thread.sleep(r.nextInt(10));

                System.out.println(Thread.currentThread().getName() + "get " + data);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
