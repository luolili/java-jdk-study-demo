package com.luo.ptn.behavior.visitor;

public interface IVisitor {
    void visit(FreeCourse course);

    void visit(CodingCourse course);
}
