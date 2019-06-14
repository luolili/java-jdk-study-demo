package base.iv;

import com.sun.org.apache.xpath.internal.operations.Bool;

public class FinalTest {

    private static boolean isGood;

    public static void main(String[] args) {
        System.out.println(isGood);//bollean default value is false
    }

    public static int f(final int m) {
        //return m++;//compile error
        return 1;
    }

    public static void f1() {
        String s = "true";
        Boolean s2 = Boolean.valueOf("true");

        System.out.println(s.equals(s2));
    }

    public float d() {
        return 1.2f;
    }

    private float d(float s) {
        return 1.2f;
    }

}
