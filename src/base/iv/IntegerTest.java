package base.iv;

public class IntegerTest {
    public static void main(String[] args) {
        Integer a = 129;
        int a1 = 129;
        Integer a2 = 129;
        System.out.println(a == a1);
        System.out.println(a == a2);//false
        System.out.println(a.equals(a2));//true
        System.out.println(a.equals(a1));
    }
}
