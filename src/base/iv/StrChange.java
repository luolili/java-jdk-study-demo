package base.iv;

/**
 * value dedivery for java
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
