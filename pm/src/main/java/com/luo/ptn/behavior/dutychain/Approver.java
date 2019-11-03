package com.luo.ptn.behavior.dutychain;

/**
 * 审核人
 */
public abstract class Approver {

    protected Approver approver;//包含自己

    public void setNextApprover(Approver approver) {
        this.approver = approver;

    }

    public abstract void deploy(Course course);


}
