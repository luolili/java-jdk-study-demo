package base.iv;

/**
 * value didivery for java
 */
public class StrChange {

    public static void main(String[] args) {
        String s = "hello";
        change(s);
        //System.out.println(s);//hello

        int x = 2;
        int y = 9;
        change(x, y);
        System.out.println("x:" + x + "," + "y:" + y);
        System.out.println("--------");
        String t1 = "as";
        String t2 = new String("as");
        System.out.println(t1 == t2);
        System.out.println(t1.equals(t2));
        System.out.println("------");
        String m1 = "halo";
        String m2 = "ha" + "lo";
        String m3 = new String("halo");
        //true
        System.out.println(m1 == m2);
        //false
        System.out.println(m1 == m3);

    }

    public static void change(String s) {
        s += "hu";
    }

    public static void change(int x, int y) {
        int temp = x;
        x = y;
        y = temp;
        System.out.println("x:" + x + "," + "y:" + y);
    }
}
