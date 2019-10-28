package com.luo.ptn.struct.flyweight;

public class Manager implements Employee {
    private String department;
    private String reportContent;

    public Manager(String department) {
        this.department = department;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    @Override
    public void report() {
        System.out.println(reportContent);
    }
}
