package com.luo.ptn.behavior.observer;

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
