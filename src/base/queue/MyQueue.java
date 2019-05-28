package base.queue;

import java.util.Arrays;
import java.util.Comparator;

/**
 * write a PriorityQueue that has only add method
 * to explore this class
 *
 * @param <E>
 */
public class MyQueue<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 11;

    transient Object[] queue;

    private int size = 0;

    private Comparator<? super E> comparator;

    transient int modCount;

    //constructor withdefault capacity
    public MyQueue() {
        this(DEFAULT_INITIAL_CAPACITY, null);
    }


    public MyQueue(int initialCapacity, Comparator<? super E> comparator) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException();
        this.queue = new Object[initialCapacity];
        this.comparator = comparator;

    }

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    //resize the array queue
    public void grow(int minCapacity) {
        int oldCapacity = queue.length;
        int newCapacity = oldCapacity + ((oldCapacity < 64) ?
                (oldCapacity + 2) : (oldCapacity >> 1));

        //overflow
        if (newCapacity > MAX_ARRAY_SIZE) {
            newCapacity = hugeCapacity(minCapacity);
            queue = Arrays.copyOf(queue, newCapacity);


        }
    }

    //the largest capacity
    public static int hugeCapacity(int minCapacity) {
        //it is a overflow too
        if (minCapacity < 0) {
            throw new OutOfMemoryError();
        }

        return minCapacity > MAX_ARRAY_SIZE ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
    }

    public boolean add(E e) {
        return offer(e);
    }

    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        modCount++;
        int i = size;
        if (i > queue.length) {
            grow(i + 1);
        }

        if (i == 0) {
            queue[0] = e;//no element at
        } else {
            siftUp(i, e);
        }

        return true;
    }

    //to keep the queue attribute balanced binary tree
    //1:parent value < child value
    private void siftUp(int k, E e) {
        if (comparator != null) {

            siftUpUsingComparator(k, e);
        } else {
            siftUpUsingComparable(k, e);
        }
    }

    private void siftUpUsingComparable(int k, E e) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object o = queue[parent];
            if (comparator.compare(e, (E) o) >= 0) {
                break;
            }
            queue[k] = o;//use parent to swap its child
            k = parent;// to get its parent element
        }
        queue[k] = e;//use the child to swap its parent
    }

    //it is the same with siftUpUsingComparable
    private void siftUpUsingComparator(int k, E e) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object o = queue[parent];
            if (comparator.compare(e, (E) o) >= 0) {
                break;
            }
            queue[k] = o;//use parent to swap its child
            k = parent;// to get its parent element
        }
        queue[k] = e;//use the child to swap its parent
    }
}
