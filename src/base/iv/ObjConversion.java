package base.iv;

public class ObjConversion {
    public static void main(String[] args) {
        class Foo {
            int m = 2;
        }

        //
        Object o = (Object) new Foo();
        Foo foo = (Foo) o;
        System.out.println("m: " + foo.m);//2
    }
}
