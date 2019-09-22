package threadcoreknowledge.singleton;

/**
 * singleton01: 静态var
 */
public class Singleton01 {

    private final static Singleton01 INSTANCE = new Singleton01();

    private Singleton01() {

    }

    public static Singleton01 getInstance() {
        return INSTANCE;
    }
}
