package com.luo.ptn.behavior.dutychain;

public class Test {
    public static void main(String[] args) {
        ArticleApprover articleApprover = new ArticleApprover();

        VideoApprover videoApprover = new VideoApprover();

        Course course = new Course("java", "a1", "v1");
        Course course2 = new Course("java", "", "v1");
        Course course3 = new Course("java", "a1", "  ");
        articleApprover.setNextApprover(videoApprover);
        articleApprover.deploy(course);
        System.out.println("---");
        articleApprover.deploy(course2);
        System.out.println("---");
        articleApprover.deploy(course3);

    }
}
