package base.iv;

/**
 * 三木运算符
 */
@SuppressWarnings("uncheck")
public class TernaryOpTest {
    public static void main(String[] args) {

        System.out.println(1 == 2 ? 'a' : 102);//f
        System.out.println((int) 'f');//102
        System.out.println((1 == 2) ? 'a' : 102);//f
        System.out.println((1.1f == 2.2f) ? 'a' : 102);//f
        System.out.println((1.1d == 2.2d) ? 'a' : 102);//f

        System.out.println((1.1 == 2.2) ? 1 : 102);//102

        System.out.println((1.1 == 2.2) ? 1.1f : 102);//102.0
        System.out.println((1.1 == 2.2) ? 1.1d : 102);//102.0
        System.out.println((1.1 == 2.2) ? 1L : 102);//102
        System.out.println("--");
        short sh = 1;
        System.out.println(1 == 1 ? sh : 102);//1

        System.out.println((int) 'a');//97
    }

}
