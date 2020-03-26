package bf.test.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 用代码进行压测
 */
public class ConcurrencyIntUpdaterTest {

    public volatile int count = 10;
    // 更新类里面volitile 的非static字段
    public static AtomicIntegerFieldUpdater<ConcurrencyIntUpdaterTest> updater = AtomicIntegerFieldUpdater.newUpdater(ConcurrencyIntUpdaterTest.class, "count");

    public static ConcurrencyIntUpdaterTest instance = new ConcurrencyIntUpdaterTest();

    public static void main(String[] args) throws Throwable {
        // 若 instance 的count 字段是10，就更新20，返回true
        if (updater.compareAndSet(instance, 10, 20)) {
            System.out.println("count=" + instance.getCount());
        }
        if (updater.compareAndSet(instance, 10, 20)) {
            System.out.println("count=" + instance.getCount());
        } else {
            System.out.println("not update count=" + instance.getCount());
        }

    }

    public int getCount() {
        return count;
    }
}
