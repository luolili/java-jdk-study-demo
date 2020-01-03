package base.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class PCTest {
    public static void main(String[] args) {

        LinkedBlockingQueue<Data> queue = new LinkedBlockingQueue<>(10);

        Producer p1 = new Producer(queue);
        Producer p2 = new Producer(queue);
        Producer p3 = new Producer(queue);

        Consumer c1 = new Consumer(queue);
        Consumer c2 = new Consumer(queue);
        Consumer c3 = new Consumer(queue);

        ExecutorService pool = Executors.newCachedThreadPool();
        pool.execute(p1);
        pool.execute(p2);
        pool.execute(p3);
        pool.execute(c1);
        pool.execute(c2);
        pool.execute(c3);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        p1.stop();
        p2.stop();
        p3.stop();


    }
}
