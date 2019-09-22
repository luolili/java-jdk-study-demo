package threadcoreknowledge.singleton;

/**
 * singleton03:  懒汉，thread-unsafe
 */
public class Singleton03 {

    private static Singleton03 INSTANCE;

    private Singleton03() {

    }

    /**
     * 2 个 thread同时访问他，创建2个对象 而返回
     *
     * @return
     */
    public static Singleton03 getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Singleton03();
        }
        return INSTANCE;
    }
}
