package com.luo.ptn.behavior.dutychain;

import org.apache.commons.lang3.StringUtils;

public class VideoApprover extends Approver {

    @Override
    public void deploy(Course course) {
        if (StringUtils.isNotBlank(course.getVideo())) {
            System.out.println(course.getName() + "有 video,批准");
            if (approver != null) {
                approver.deploy(course);
            }
        } else {
            System.out.println(course.getName() + "no video,no 批准,end");
        }

    }
}
