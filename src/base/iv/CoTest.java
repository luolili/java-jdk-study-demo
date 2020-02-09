package base.iv;

public class CoTest {

    public static void main(String[] args) {
        boolean res1 = checkValue(6144);
        boolean res2 = checkValue(5073);
        boolean res3 = checkValue(4831);
        boolean res4 = checkValue(8300);

        char ch1 = 97;
        char ch2 = 'a';
        System.out.println("ch1=" + ch1);
        System.out.println("ch2=" + ch2);
    }

    public void test() {
        checkValue(9);
    }

    public static boolean checkValue(int num) {
        int d1, d2, d3;
        int checkNum, nRemain, rem;
        checkNum = num % 10;
        nRemain = num / 10;
        d3 = nRemain % 10;
        nRemain /= 10;
        d2 = nRemain % 10;
        nRemain /= 10;
        d1 = nRemain % 10;
        rem = (d1 + d2 + d3) % 7;
        return rem == checkNum;
    }
}
