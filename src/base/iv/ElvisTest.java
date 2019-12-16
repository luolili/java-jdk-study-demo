package base.iv;

public class ElvisTest {

    private static final ElvisTest e = new ElvisTest();

    private ElvisTest() {

    }

    private static final Boolean living = true;
    private final Boolean alive = true;

    public final boolean lives() {
        return alive;
    }

    public static void main(String[] args) {
        System.out.println(e.lives() ? "o" : "dd");
    }
}
