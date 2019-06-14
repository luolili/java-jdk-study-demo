package base.iv;

import com.sun.org.apache.xpath.internal.operations.Bool;

public class FinalTest {

    private static boolean isGood;

    public static void main(String[] args) {
        System.out.println(isGood);//bollean default value is false
        //Stringbuilder +
        StringBuilder sb1 = new StringBuilder("k");
        StringBuilder sb2 = new StringBuilder("k");
        //sb1 += "f";//compile error
        // sb1+=sb2;//imcompitible type
        tryTest();
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

    //only finally
    public static void tryTest() {
        try {

            int n = 1 / 0;
            System.out.println("try");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("arr");
        } finally {
            System.out.println("finally");
        }
    }

}
