package com.mooveit.greetings.model;

import org.json.simple.JSONObject;

public class EmployeeJson {

    private final JSONObject employee;

    public EmployeeJson(JSONObject employee) {
        this.employee = employee;
    }

    public String getName() {
        return getEmployeeProperty("employee_name");
    }

    public String getAge() {
        return getEmployeeProperty("employee_age");
    }

    public String getSalary() {
        return getEmployeeProperty("employee_salary");
    }

    @SuppressWarnings("unchecked")
    private <T> T getEmployeeProperty(String propertyName) {
        return (T) employee.get(propertyName);
    }
}
