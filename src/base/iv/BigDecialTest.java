package base.iv;

import java.math.BigDecimal;

public class BigDecialTest {
    public static void main(String[] args) {
        //0
        compare(new BigDecimal("1.0"), new BigDecimal("1.000"));
        add(new BigDecimal("1.0"), new BigDecimal("2.000"));
        substract(new BigDecimal("1.0"), new BigDecimal("2.000"));
        multiply(new BigDecimal("1.0"), new BigDecimal("2.000"));
        divide(new BigDecimal("1.0"), new BigDecimal("2.000"));
        max(new BigDecimal("1.0"), new BigDecimal("2.000"));
        min(new BigDecimal("1.0"), new BigDecimal("2.000"));

    }

    public static void compare(BigDecimal a, BigDecimal b) {
        System.out.println(a.compareTo(b));
    }

    public static void add(BigDecimal a, BigDecimal b) {
        System.out.println(a.add(b));
    }

    public static void substract(BigDecimal a, BigDecimal b) {
        System.out.println(a.subtract(b));
    }

    public static void multiply(BigDecimal a, BigDecimal b) {
        System.out.println(a.multiply(b));
    }

    public static void divide(BigDecimal a, BigDecimal b) {
        System.out.println(a.multiply(b));
    }

    public static void max(BigDecimal a, BigDecimal b) {
        System.out.println(a.max(b));
    }

    public static void min(BigDecimal a, BigDecimal b) {
        System.out.println(a.min(b));
    }
}
