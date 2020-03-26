package bf.test.atomic;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 用代码进行压测
 * 解决 cas 的aba问题，加版本号
 */
public class ConcurrencyStampReferenceTest {

    // 线程和数
    public static final int threadTotal = 50;
    public static AtomicStampedReference<Integer> count = new AtomicStampedReference(1, 0);

    public static void main(String[] args) throws Throwable {
        System.out.println("count=" + count);

    }


}
