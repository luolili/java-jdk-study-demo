package threadcoreknowledge.singleton;

/**
 * singleton07: 私有静态内部类
 */
public class Singleton07 {

    private Singleton07() {

    }

    public static Singleton07 getInstance() {
        return SingletonInstance.INSTANCE;
    }

    private static class SingletonInstance {
        //懒汉
        private static final Singleton07 INSTANCE = new Singleton07();
    }

    public static void main(String[] args) {
        Singleton07 s = Singleton07.getInstance();
        Singleton07 s1 = Singleton07.getInstance();
        Singleton07 s2 = Singleton07.getInstance();
        System.out.println(s == s1);
        System.out.println(s == s2);

    }
}
