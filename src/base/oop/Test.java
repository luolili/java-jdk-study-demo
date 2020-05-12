package base.oop;

import java.math.BigDecimal;

public class Test {

    public static void main(String[] args) {
        // false
        System.out.println(Integer.MAX_VALUE > Float.MAX_VALUE);
        System.out.println(Integer.MAX_VALUE < Float.MAX_VALUE);
        //false
        System.out.println(new BigDecimal("5.5").equals(5.5));
        //true
        System.out.println(1.23 == new Double(1.23));
    }
}
