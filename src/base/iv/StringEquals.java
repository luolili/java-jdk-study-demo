package base.iv;

public class StringEquals {

    public static void main(String[] args) {
        String s = new String("Test");
        if (s == "Test") {
            System.out.println("ff");
        }
        System.out.println(s.equals("Test"));
        if (s.equals("Test")) {
            System.out.println("2");
        }
    }
}
