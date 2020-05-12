package base.oop;

public class Child extends Parent {

    public static void main(String[] args) {
        Child child = new Child();

        //子类可继承父亲的public 属性/方法
        String name = child.getName();
        String parentName = child.name;
    }
}
