package com.luo;

/*
新加的方法
 */
public class StringTest {
    public static void main(String[] args) {
        String str = "helo,  java11\u3000";
        String str02 = "\u3000";
        System.out.println(str.strip());//去掉了unicode空格符
        System.out.println(str.trim());
        System.out.println(str02.isBlank());//true
        System.out.println(str02.isEmpty());//false
        String lines = "he\nhu\ner";
        lines.lines().forEach(System.out::println);
        System.out.println("$".repeat(11));
        System.out.println(str.subSequence(0, 3));
        System.out.println(Void.class == null);
        //System.out.println(new Void() == null.);
    }
}
