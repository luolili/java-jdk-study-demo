package bf.stream;

import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * 如何用并行流：计算密集型的计算；需要测试
 */
public class ParallelStreamDemo {

    public static long parallelSum(long n) {
        // 需要拆箱
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .parallel()
                .reduce(0L, Long::sum);
    }

    /**
     * 高效的版本
     *
     * @param n
     * @return
     */
    public static long parallelSumV2(long n) {
        return LongStream.rangeClosed(0, n)

                .reduce(0L, Long::sum);
    }

    public static long sequenceSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .reduce(0L, Long::sum);
    }

    public static long sequenceSumV2(long n) {
        return LongStream.rangeClosed(0, n)
                .reduce(0L, Long::sum);
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        long res = parallelSumV2(100);
        //long res = sequenceSumV2(100);
        long diff = System.currentTimeMillis() - start;
        System.out.println("diff:" + diff);
    }
}
