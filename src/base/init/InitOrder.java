package base.init;

public class InitOrder {

    static {
        System.out.println("static");
    }

    {
        System.out.println("plain");
    }

    public InitOrder() {
        System.out.println("con");
    }

    public static void main(String[] args) {
        new InitOrder();
    }
}
