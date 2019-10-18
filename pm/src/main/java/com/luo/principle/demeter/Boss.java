package com.luo.principle.demeter;

public class Boss {
    public void commandCheckNumber(TeamLeader leader) {
        //Course 不是 boss friend
/*        List<Course> courseList = new ArrayList<Course>();
        for (int i = 0; i < 20; i++) {
            courseList.add(new Course());

        }*/
        leader.checkNumberOfCourses();
    }
}
