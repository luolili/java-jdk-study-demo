package base.iv;

import com.sun.org.apache.xpath.internal.operations.Bool;

public class FinalTest {

    private static boolean isGood;

    private int num;
    public static void main(String[] args) {
        //System.out.println(isGood);//bollean default value is false
        //Stringbuilder +
        StringBuilder sb1 = new StringBuilder("dd");
        StringBuilder sb2 = new StringBuilder("hh");
        //sb1 += "f";//compile error
        // sb1+=sb2;//imcompitible type
        //tryTest();
        //f1();//false
        change(sb1, sb2);
        System.out.println(sb1 + "--" + sb2);

        //System.out.println( "num is : "+changeFinalObj( new FinalTest()));//0
        FinalTest o = new FinalTest();
        changeFinalObj(o);
        System.out.println(o.num);//1
    }

    public static int changeFinalObj(final FinalTest o) {
        return o.num++;
    }

    public static void change(StringBuilder x, StringBuilder y) {
        y.append(x);
        y = x;
    }

    public static int f(final int m) {
        //return m++;//compile error
        return 1;
    }

    public static void f1() {
        String s = "true";
        Boolean s2 = Boolean.valueOf("true");

        System.out.println(s2.equals(s));
        /*if (s.equals(s2)) {
            System.out.println("--");
        }*/
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
