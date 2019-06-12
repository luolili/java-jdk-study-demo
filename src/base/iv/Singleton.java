package base.iv;

public class Singleton {

    private static Singleton instance;

    //private constructor
    private Singleton() {

    }

    public static synchronized Singleton getInstnce() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }


}
