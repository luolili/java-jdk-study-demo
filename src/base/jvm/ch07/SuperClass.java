package base.jvm.ch07;

public class SuperClass {
    static {
        System.out.println("super class init");
    }

    // 静态字段
    public static int value = 123;
    //常量
    public static final String HU = "hu";
}
