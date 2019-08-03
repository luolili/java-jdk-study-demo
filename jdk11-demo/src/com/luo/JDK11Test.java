package com.luo;

import java.lang.reflect.Field;

/**
 *
 */
public class JDK11Test {

    public static class X {
        void test() throws Exception {
            Y y = new Y();

            y.y = 1;
            //java8 : IllegalAccessException:
            // Class com.luo.JDK11Test$X can not access a member of class com.luo.JDK11Test$Y with modifiers "private static"
            //	at sun.reflect.Reflection.ensureMemberAccess(Reflection.java:102)
            Field field = Y.class.getDeclaredField("y");
            field.setInt(y, 2);
        }
    }

    private static class Y {
        private static int y;
    }

    public static void main(String[] args) throws Exception {
        new X().test();
    }
}
