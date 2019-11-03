package com.luo.ptn.behavior.visitor;

public class Visitor implements IVisitor {
    @Override
    public void visit(FreeCourse course) {
        System.out.println("course name:" + course.getName());
    }

    @Override
    public void visit(CodingCourse course) {
        System.out.println("course name:" + course.getName() + ",price:" + course.getPrice());
    }
}
