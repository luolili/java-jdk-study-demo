package base.iv;

public class IntScope {

    public static void scope() {
        int count = 0;
        int v = 1;
        for (int i = 0; i < v; i++) {
            v += 1000;
            System.out.println("t");
            count += 1;
        }
        System.out.println("count:" + count);
    }

    public static void main(String[] args) {
        scope();
    }
}
