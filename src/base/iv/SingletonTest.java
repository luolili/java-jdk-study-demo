package base.iv;

public class SingletonTest {
    public static void main(String[] args) {
        Singleton s1 = Singleton.getInstnce();
        Singleton s2 = Singleton.getInstnce();
        System.out.println(s1 == s2);


    }
}
