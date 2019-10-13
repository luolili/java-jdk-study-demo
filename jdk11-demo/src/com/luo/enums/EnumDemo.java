package com.luo.enums;

public enum EnumDemo {

    MALE("male") {
        @Override
        public void tellSex() {
            System.out.println("i like man");
        }
    },
    FEMALE("female") {
        @Override
        public void tellSex() {
            System.out.println("i like girl");
        }
    },
    ;
    private String sex;

    //构造方法默认private
    EnumDemo(String sex) {
        this.sex = sex;
    }

    
    //抽象方法必须被实现
    public abstract void tellSex();

    public static void main(String[] args) {

    }
    
}
