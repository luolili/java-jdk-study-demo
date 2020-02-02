package base.queue;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * write a PriorityQueue that has only add method
 * to explore this class
 *
 */
public class MyQueue {

    public static void main(String[] args) {
        ArrayBlockingQueue queue = new ArrayBlockingQueue<Integer>(2);
        queue.add(1);
        queue.add(2);

    }
}