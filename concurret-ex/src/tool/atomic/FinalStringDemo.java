package tool.atomic;

public class FinalStringDemo {

    public static void main(String[] args) {
        String a = "halo2";
        final String b = "halo";
        String c = "halo";
        String d = b + 2;
        String d1 = getString() + 2;
        String e = d + 2;
        //true
        System.out.println(a == d);
        //false
        System.out.println(a == d1);
        //false
        System.out.println(a == e);
    }

    private static String getString() {

        return "halo";
    }
}
