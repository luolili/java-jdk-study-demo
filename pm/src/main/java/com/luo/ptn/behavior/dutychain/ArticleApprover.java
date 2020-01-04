package com.luo.ptn.behavior.dutychain;

import org.apache.commons.lang3.StringUtils;

public class ArticleApprover extends Approver {

    @Override
    public void deploy(Course course) {
        if (StringUtils.isNotBlank(course.getArticle())) {
            System.out.println(course.getName() + " 有 手记,批准");
            if (approver != null) {
                approver.deploy(course);
            }
        } else {
            System.out.println(course.getName() + " no 手记,no 批准,end");
        }

    }
}
