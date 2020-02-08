package com.luo.ptn.behavior.command;

/**
 * 命令的接口，命令接口的实现类（包含命令的调用人），在exec 里面通过调用人调用命令
 */
public class Test {
    public static void main(String[] args) {
        CourseVideo courseVideo = new CourseVideo("java");
        OpenCourseVideoCommand openCourseVideoCommand = new OpenCourseVideoCommand(courseVideo);
        CloseCourseVideoCommand closeCourseVideoCommand = new CloseCourseVideoCommand(courseVideo);
        Staff staff = new Staff();
        //员工
        staff.addCommand(openCourseVideoCommand);
        staff.addCommand(closeCourseVideoCommand);

        staff.execute();

    }
}
