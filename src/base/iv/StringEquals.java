package base.iv;

public class StringEquals {

    public static void main(String[] args) {
        //分配到堆内存
        String s = new String("Test");
        if (s == "Test") {
            System.out.println("ff");
        }
        System.out.println(s.equals("Test"));
        if (s.equals("Test")) {
            System.out.println("2");
        }

        String s1 = new StringBuilder("go").append("od").toString();
        System.out.println(s1.intern() == s1);//true

        //--
        System.out.println("----");
        //分配到常量池
        String m = "ed";
        String m2 = new String("ed");
        System.out.println(m == m2);//false
        System.out.println(m.equals(m2));//true

        String m3 = "e";
        String m4 = "d";
        String m5 = "e" + "d";
        String m6 = m3 + m4;
        //-1
        System.out.println(m == m5);//true
        System.out.println("-n-" + (m2 == m5));//false
        System.out.println(m == m6);//false
        System.out.println(m == m2.intern());//true
        System.out.println(m == m6.intern());//true



    }
}
