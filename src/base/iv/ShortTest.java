package base.iv;

public class ShortTest {

    public static void main(String[] args) {
        //incompatible type
       /* short s = 1;
        s = s + 1;*/

        tryTest();
        //System.out.println(2<<2);//8

    }

    public static int tryTest() {
        try {
            System.out.println("try");
            int m = 1 / 0;
            return 1;
        } catch (Exception e) {
            System.out.println("--");

        } finally {
            System.out.println("finally");
        }

        return 6;
    }

    public void nonStaticMethod() {
        st();
    }

    public static void st() {

    }
}
