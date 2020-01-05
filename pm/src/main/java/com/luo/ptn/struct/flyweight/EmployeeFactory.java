package com.luo.ptn.struct.flyweight;

import java.util.HashMap;
import java.util.Map;

public class EmployeeFactory {
    private static final Map<String, Employee> EMPLOYEE_MAP = new HashMap<>();

    public static Employee getManager(String department) {
        Manager manager = (Manager) EMPLOYEE_MAP.get(department);
        if (manager == null) {
            String reportContent = department + ",some";
            manager = new Manager(department);
            System.out.print("create manager:" + department);
            manager.setReportContent(reportContent);
            EMPLOYEE_MAP.put(department, manager);
        }
        return manager;

    }
}
