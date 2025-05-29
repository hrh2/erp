package com.erp.services;

import com.erp.models.Employee;
import com.erp.models.User;

import java.util.List;
import java.util.UUID;

public interface IEmployeeService {
    Employee createEmployee(Employee employee);
    Employee updateEmployee(UUID id, Employee employee);
    Employee findEmployeeById(UUID id);
    Employee findEmployeeByCode(String code);
    Employee findEmployeeByUser(User user);
    List<Employee> findAllEmployees();
    void deleteEmployee(UUID id);
}