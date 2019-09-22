package threadcoreknowledge.singleton;

/**
 * singleton08: 私有静态内部类
 */
public enum Singleton08 {

    INSTANCE;//懒加载，避免反序列化破坏单例

    public static void main(String[] args) {

        Singleton08 s = Singleton08.INSTANCE;
        Singleton08 s1 = Singleton08.INSTANCE;
        Singleton08 s2 = Singleton08.INSTANCE;
        System.out.println(s == s1);
        System.out.println(s == s2);


    }
}
