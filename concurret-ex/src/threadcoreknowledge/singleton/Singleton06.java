package threadcoreknowledge.singleton;

/**
 * singleton03:  懒汉，thread-safe, double check
 */
public class Singleton06 {

    private static Singleton06 INSTANCE;

    private Singleton06() {

    }

    public static Singleton06 getInstance() {
        if (INSTANCE == null) {
            synchronized (Singleton06.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Singleton06();
                }

            }

        }
        return INSTANCE;
    }
}
