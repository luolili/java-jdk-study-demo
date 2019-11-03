package com.luo.ptn.behavior.command;

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
