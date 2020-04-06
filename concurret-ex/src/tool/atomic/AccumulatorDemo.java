package tool.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.LongBinaryOperator;
import java.util.stream.IntStream;

public class AccumulatorDemo {
    public static void main(String[] args) {
        LongBinaryOperator op1 = (a, b) -> Math.max(a, b);
        LongBinaryOperator op2 = (a, b) -> a + b;
        LongBinaryOperator op3 = (a, b) -> a * b;
        LongAccumulator longAccumulator = new LongAccumulator(op3, 1);
        //+2
        //longAccumulator.accumulate(2);
        //在+3
        //longAccumulator.accumulate(3);
        ExecutorService e = Executors.newFixedThreadPool(1);
        IntStream.range(1, 10).forEach(i ->
                e.submit(() -> longAccumulator.accumulate(i)));
        e.shutdown();
        while (!e.isTerminated()) {

        }
        System.out.println(longAccumulator.get());
    }
}
