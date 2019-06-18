package base.iv;

/**
 * value dedivery for java
 */
public class StrChange {

    public static void main(String[] args) {
        String s = "hello";
        change(s);
        System.out.println(s);//hello
    }

    public static void change(String s) {
        s += "hu";
    }
}
