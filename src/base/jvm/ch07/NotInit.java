package base.jvm.ch07;

/**
 * 不会初始化子类
 * 1. 子类引用父类的静态字段,不会导致子类初始化
 *
 * 接口的初始化和类的初始化的不同：
 * 当一个类在初始化的时候，要求其父类全部被都已经初始化；
 * 接口没有这个要求，只有在真正用到父接口的时候才会初始化
 *  */
public class NotInit {
    public static void main(String[] args) {
        //condition 1: 子类引用父类的静态字段,不会导致子类初始化
        //System.out.println(SubClass.value);//super class init 123
        // condition 2
        //SuperClass[] sca = new SuperClass[10];
        /*
        在编译阶段，类的常量存储到了类的常量池，
        对常量的引用就是对常量池的引用
         */
        System.out.println(SubClass.HU);//没有打印出super class init


    }
}
