package threadcoreknowledge.singleton;

/**
 * singleton03:  thread-unsafe
 */
public class Singleton05 {

    private static Singleton05 INSTANCE;

    private Singleton05() {

    }

    /**
     * 多个instance 被创建
     *
     * @return INSTANCE
     */
    public static Singleton05 getInstance() {
        if (INSTANCE == null) {
            synchronized (Singleton05.class) {
                INSTANCE = new Singleton05();
            }

        }
        return INSTANCE;
    }
}
