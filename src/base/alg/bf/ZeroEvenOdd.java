package base.alg.bf;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

public class ZeroEvenOdd {
    private int n;
    private volatile int who;
    private volatile int start = 1;
    private Lock lock = new ReentrantLock();

    private Condition zero = lock.newCondition();
    private Condition even = lock.newCondition();
    private Condition odd = lock.newCondition();

    public ZeroEvenOdd(int n) {
        this.n = n;
    }

    public void zero(IntConsumer printNumber) {
        lock.lock();
        try {
            while (start <= n) {
                if (who != 0) {
                    zero.await();
                }
                printNumber.accept(0);
                if (start % 2 == 0) {
                    who = 2;
                    even.signal();
                } else {
                    who = 1;
                    odd.signal();
                }
                zero.await();

            }
            odd.signal();
            even.signal();
        } catch (Exception e) {

        } finally {
            lock.unlock();
        }
    }

    public void even(IntConsumer printNumber) {
        lock.lock();
        try {
            while (start <= n) {
                if (who != 2) {
                    even.await();
                } else {
                    who = 0;
                    printNumber.accept(start++);
                    zero.signal();
                }

                if (start % 2 == 0) {
                    who = 2;
                    even.signal();
                } else {
                    who = 1;
                    odd.signal();
                }
            }
        } catch (Exception e) {

        } finally {
            lock.unlock();
        }
    }

    public void odd(IntConsumer printNumber) {
        lock.lock();
        try {
            while (start <= n) {
                if (who != 1) {
                    odd.await();
                } else {
                    who = 0;
                    printNumber.accept(start++);
                    zero.signal();
                }

            }
        } catch (Exception e) {

        } finally {
            lock.unlock();
        }
    }


}
