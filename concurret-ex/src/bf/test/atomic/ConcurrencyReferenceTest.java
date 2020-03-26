package bf.test.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 用代码进行压测
 */
public class ConcurrencyReferenceTest {

    // 线程和数
    public static final int threadTotal = 50;
    public static AtomicReference<Integer> count = new AtomicReference(0);

    public static void main(String[] args) throws Throwable {
        // count =2
        count.compareAndSet(0, 1);
        //no op
        count.compareAndSet(0, 2);
        count.compareAndSet(1, 3);
        count.compareAndSet(2, 4);
        count.compareAndSet(3, 5);
        System.out.println("count=" + count);
        System.out.println("count=" + count.get());

        AtomicReference<String> ref = new AtomicReference<>();
        String val = "he";
        ref.set(val);
        ref.compareAndSet("he", "hu");
        // 必须是对象的引用一样
        ref.compareAndSet(new String("he"), "hu");
        System.out.println(val);


    }


}
