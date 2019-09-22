package threadcoreknowledge.singleton;

/**
 * singleton03:  加volatile，thread-safe, double check
 * 创建对象不是原子操作：
 * 1. 构造一个空对象
 * 2. 调用构造方法：比较耗时
 * 3. 赋值给引用
 * <p>
 * 上面可能会发生重排序 volatile ：avoid重排序 /可见性
 */
public class Singleton06Plus {

    private volatile static Singleton06Plus INSTANCE;

    private Singleton06Plus() {

    }

    public static Singleton06Plus getInstance() {
        if (INSTANCE == null) {
            synchronized (Singleton06Plus.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Singleton06Plus();
                }

            }

        }
        return INSTANCE;
    }
}
