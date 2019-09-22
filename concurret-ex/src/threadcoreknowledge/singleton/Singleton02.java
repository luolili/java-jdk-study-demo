package threadcoreknowledge.singleton;

/**
 * singleton02: 静态代码块
 */
public class Singleton02 {

    private final static Singleton02 INSTANCE;

    static {
        INSTANCE = new Singleton02();
    }

    private Singleton02() {

    }

    public static Singleton02 getInstance() {
        return INSTANCE;
    }
}
