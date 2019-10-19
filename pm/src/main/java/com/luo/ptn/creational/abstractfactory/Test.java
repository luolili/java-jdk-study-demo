package com.luo.ptn.creational.abstractfactory;

public class Test {
    public static void main(String[] args) {
        //java product group:扩展性不强
        CourseFactory courseFactory = new JavaCourseFactory();
        Video video = courseFactory.getVideo();
        Article article = courseFactory.getArticle();
        video.produce();
        article.produce();

    }
}
