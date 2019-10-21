package com.luo.ptn.creational.prototype;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Mail implements Cloneable {
    private String name;
    private String mailAddress;
    private String content;

    public Mail() {
        System.out.println("mail class con");
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        System.out.println("clone mail obj");
        return super.clone();
    }
}
