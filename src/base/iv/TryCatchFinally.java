package base.iv;

public class TryCatchFinally {

    public static void main(String[] args) {
        int res = test();
        //3
        System.out.println(res);
    }

    public static int test() {
        try {
            return 1;
        } catch (Exception e) {
            return 2;
        } finally {
            return 3;
        }
    }
}
