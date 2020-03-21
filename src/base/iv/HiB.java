package base.iv;

public class HiB extends HiA {

    public HiB() {
        System.out.println("con b");
    }

    {
        System.out.println("plain b");
    }

    static {
        System.out.println("static b");
    }

    public static void main(String[] args) {
        new HiB();
    }
}
