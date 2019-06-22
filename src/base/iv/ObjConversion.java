package base.iv;

public class ObjConversion {
    public static void main(String[] args) {
        class Foo {
            int m = 2;
        }

        //
        Object o = (Object) new Foo();
        Foo foo = (Foo) o;
        //System.out.println("m: " + foo.m);//2
        for (int i = 0; i <= 22; ) {
            if (i < 10) {
                int j = 2 + i;
                i++;

                System.out.println(i + "," + j);
            }
        }
    }
}
