package com.luo.java8time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * java8之前的SimpleDateFormat 是线程不安全的
 * java8之前的设计：
 * 1.java.util 和java.sql有一个同名的Date,前者包含日期+时间，后者包含日期
 * 2.所有的日期类都不是 final
 */
public class Demo {

    public static void main(String[] args) {

        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        //2019-12-07T16:28:20.047331300
        System.out.println("now:" + now);
        //2019-12-07
        LocalDate now1 = LocalDate.now();
        System.out.println("now:" + now1 + "year:" + now1.getYear() + "mo:" + now1.getMonth());
        LocalDate bir = LocalDate.of(2009, 1, 2);
        System.out.println("bir:" + bir);
        System.out.println("equals:" + bir.equals(now1));//false

        System.out.println("plus:" + bir.plus(2, ChronoUnit.DAYS));
        System.out.println("after:" + bir.isAfter(now1));
        System.out.println("before:" + bir.isBefore(now1));
        //格式化
        //string to date
        String dateStr = "20190203";
        LocalDate d = LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);
        System.out.println("str to date:" + d);

        //date to string
        System.out.println("date to str:" + d.format(DateTimeFormatter.BASIC_ISO_DATE));
    }
}
