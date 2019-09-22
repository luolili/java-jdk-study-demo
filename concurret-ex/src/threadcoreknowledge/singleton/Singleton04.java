package threadcoreknowledge.singleton;

/**
 * singleton03:  懒汉，thread-safe
 */
public class Singleton04 {

    private static Singleton04 INSTANCE;

    private Singleton04() {

    }

    /**
     * 效率低
     *
     * @return
     */
    public synchronized static Singleton04 getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Singleton04();
        }
        return INSTANCE;
    }
}
