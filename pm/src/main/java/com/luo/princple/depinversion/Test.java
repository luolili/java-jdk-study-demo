package com.luo.princple.depinversion;

//高层模块
public class Test {
    public static void main(String[] args) {
        //Me me = new Me();
        //v1
        /*me.studyJavaCourse();
        me.studyFECourse();*/

        //v2:参数注入
       /* me.studyITCourse(new JavaCourse());
        me.studyITCourse(new FECourse());*/
        //v3：构造方法注入
       /* Me me = new Me(new JavaCourse());
        me.studyITCourse();*/

        //v4:setter
        Me me = new Me();

        me.setCourse(new JavaCourse());
        me.studyITCourse();
    }
}
