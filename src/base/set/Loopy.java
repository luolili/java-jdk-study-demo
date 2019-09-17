package base.set;

public class Loopy {
    public static void main(String[] args) {
        int start = Integer.MAX_VALUE - 2;
        int end = Integer.MAX_VALUE;
        int count = 0;
        System.out.println(end + 1);//-2147483648
        for (int i = start; i < end + 1; i++) {
            count++;
        }
        System.out.println(count);//100
    }
}
