package base.alg.bf;

import java.util.function.IntConsumer;

public class ZeroEvenOddV2 {
    private int n;
    private volatile int flag;


    public ZeroEvenOddV2(int n) {
        this.n = n;
    }

    public void zero(IntConsumer printNumber) {
        for (int i = 0; i < n; i++) {
            while (flag != 0) {
                Thread.yield();
            }
            printNumber.accept(0);
            if (i % 2 == 0) {
                flag = 2;
            } else {
                flag = 1;
            }
        }
    }

    public void even(IntConsumer printNumber) {
        for (int i = 2; i <= n; i += 2) {
            while (flag != 2) {
                Thread.yield();
            }
            printNumber.accept(i);
            flag = 0;
        }
    }

    public void odd(IntConsumer printNumber) {
        for (int i = 1; i <= n; i += 1) {
            while (flag != 1) {
                Thread.yield();
            }
            printNumber.accept(i);
            flag = 0;
        }
    }


}
