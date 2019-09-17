package base.set;

public class Loopy {
    public static void main(String[] args) {
        int start = Integer.MAX_VALUE - 100;
        int end = Integer.MAX_VALUE;
        int count = 0;
        for (int i = start; i < end; i++) {
            count++;
        }
        System.out.println(count);//100
    }
}
