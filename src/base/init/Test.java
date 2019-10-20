package base.init;

/**
 * clinit: 类构造器 ：不需要手动调用父类的构造器，jvm做了这个调用，
 * 这意味着父类 里面的静态语句块 先于子类的var
 * <p>
 * 接口：执行接口的clinint不会先之心那个父类的clinit
 * <p>
 * clinit是线程安全的
 * ，init实例构造器
 */
public class Test {
    static {
        i = 0;
        //System.out.println(i);//illegal forward ref

    }

    static int i = 2;
}
