package threadcoreknowledge.stopthread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * volatile的局限
 */
public class WrongwayVolatileCantStop {
    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue queue = new ArrayBlockingQueue(10);

        Producer producer = new Producer(queue);

        Thread thread = new Thread(producer);

        thread.start();
        Thread.sleep(1000);
        Consumer consumer = new Consumer(queue);

        while (consumer.needMoreNums()) {
            System.out.println(consumer.storage.take() + " consume");
            Thread.sleep(100);
        }
        System.out.println("not need");
        producer.canceled = true;//停止生产
        System.out.println(producer.canceled);
    }


}

class Producer implements Runnable {

    BlockingQueue storage;
    public volatile boolean canceled;

    public Producer(BlockingQueue storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        int num = 0;
        try {
            while (num < 10000 && !canceled) {
                if (num % 100 == 0) {
                    storage.put(num);
                    System.out.println(num + "  yes");
                }
                num++;
                Thread.sleep(1);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("stop");
        }
    }
}

class Consumer {
    BlockingQueue storage;

    public Consumer(BlockingQueue storage) {
        this.storage = storage;
    }

    public boolean needMoreNums() {
        if (Math.random() > 0.93) {
            return false;
        }
        return true;

    }
}