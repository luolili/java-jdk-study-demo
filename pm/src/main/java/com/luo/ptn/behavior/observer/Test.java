package com.luo.ptn.behavior.observer;

/**
 * 一个可观察的对象Course（继承 Observerable）:具有一个可发生改变的方法，调用
 * setChanged() 和 notifyObservers()方法；
 * 一个观察人 Teacher （实现Observer）实现 update（Course, question）
 */
public class Test {
    public static void main(String[] args) {
        Course course = new Course("java");

        Teacher teacher1 = new Teacher("mee1");
        Teacher teacher2 = new Teacher("mee2");
        course.addObserver(teacher1);
        course.addObserver(teacher2);

        Question question = new Question();
        question.setUsername("hu");
        question.setQuestionContent("how to do it");
        course.produceQuestion(course, question);


    }
}
