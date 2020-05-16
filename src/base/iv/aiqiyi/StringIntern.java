package base.iv.aiqiyi;

public class StringIntern {
    public static void main(String[] args) {

        String s2 = new StringBuffer("计算机").append("软件").toString();
        System.out.println(s2.intern() == s2);
        // 必须是单词才是TRUE
        String s1 = new StringBuffer("char").append("he").toString();

        System.out.println(s1.intern() == s1);
        //String s3 = new StringBuffer("计算机").toString();
        //System.out.println(s3.intern()==s3);
        String s4 = "ja" + "va";
        System.out.println(s1.intern() == s4);
        System.out.println(s4 == "java");

    }
}
