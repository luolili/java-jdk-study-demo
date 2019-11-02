package com.luo.ptn.behavior.observer;

public class Test {
    public static void main(String[] args) {
        Course course = new Course("java");

        Teacher teacher = new Teacher("mee");
        course.addObserver(teacher);

        Question question = new Question();
        question.setUsername("hu");
        question.setQuestionContent("how to do it");
        course.produceQuestion(course, question);


    }
}
